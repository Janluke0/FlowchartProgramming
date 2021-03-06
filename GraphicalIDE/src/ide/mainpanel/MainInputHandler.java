package ide.mainpanel;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.swing.SwingUtilities;

import language.Connection;
import language.Piece;
import language.value.ProgramValueNothing;

/**
 * The Class MainInputHandler.
 */
public class MainInputHandler implements MouseListener, MouseMotionListener, KeyListener {

	/** The main panel. */
	private final MainPanel mainPanel;

	/** The position the mouse is pressed down. */
	private Optional<Point> pressedPosition = Optional.empty();

	/**
	 * The initial position the screen was in when the mouse was pressed down.
	 */
	private Optional<Point> initialScreenPosition = Optional.empty();

	/**
	 * The list of initial positions of pieces when the mouse was initially
	 * pressed down.
	 */
	private Optional<List<Point>> initialPositions = Optional.empty();

	/** The port currently selected. */
	private Optional<Integer> portSelected = Optional.empty();

	/** The piece whose port is currently being dragged. */
	private Optional<Piece> piecePortDragged = Optional.empty();

	private boolean shiftPressed;

	/**
	 * Instantiates a new main input handler.
	 *
	 * @param mainPanel
	 *            the main panel
	 */
	public MainInputHandler(final MainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(final MouseEvent e) {
		mainPanel.requestFocusInWindow();

		final List<Piece> collidingPieces = new ArrayList<>(mainPanel.getPieces());
		final Point worldCoord = mainPanel.getWorldCoordFromMouse(e.getPoint());
		collidingPieces.removeIf((final Piece piece) -> !piece.containsPoint(worldCoord));
		if (collidingPieces.isEmpty()) {
			// we aren't clicking on anything
			mainPanel.getGraphicsHandler().draggingPiece = false;
			initialPositions = Optional.empty();

			if (!shiftPressed) {
				mainPanel.getSelectedPieces().clear();
			}
		} else {

			// we are clicking on one or more pieces, drag the top piece (last
			// in the list)
			final Piece selected = collidingPieces.get(collidingPieces.size() - 1);

			final Optional<Integer> outputPortSelected = selected.outputPortContainingPoint(worldCoord);
			if (outputPortSelected.isPresent()) {
				// we selected a port
				portSelected = outputPortSelected;
				piecePortDragged = Optional.of(selected);
			} else {
				mainPanel.getGraphicsHandler().draggingPiece = true;
			}
			if (!mainPanel.getSelectedPieces().contains(selected)) {
				if (!shiftPressed) {
					mainPanel.getSelectedPieces().clear();
				}
				mainPanel.getSelectedPieces().add(selected);
			}

			final List<Point> initialPositions = new ArrayList<>();
			for (final Piece p : mainPanel.getSelectedPieces()) {
				initialPositions.add(p.getPieceGraphics().getPosition());
			}
			this.initialPositions = Optional.of(initialPositions);
		}

		pressedPosition = Optional.of(e.getPoint());
		initialScreenPosition = Optional.of(mainPanel.getViewPosition());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(final MouseEvent e) {
		if (portSelected.isPresent()) {
			final Point worldCoord = mainPanel.getWorldCoordFromMouse(e.getPoint());

			// we are dragging from a port
			final List<Piece> collidingPieces = getTouchingPieces(worldCoord);
			if (!collidingPieces.isEmpty()) {
				// if we are in a piece
				for (int i = collidingPieces.size() - 1; i >= 0; --i) {
					// check if we're touching an output, backwards so we check
					// from top to bottom
					final Piece piece = collidingPieces.get(i);
					// get the coordinate of the mouse on the piece
					final Point pieceCoord = new Point(worldCoord);
					Point upperLeft = piece.getPieceGraphics().getPosition();
					pieceCoord.translate((int)-upperLeft.getX(), (int)-upperLeft.getY());

					for (int j = 0; j < piece.getInputs().length; j++) {
						if (piece.inputContainsPoint(j, pieceCoord)) {
							synchronized (mainPanel.getPieces()) {
								// check if any other pieces are using this as
								// an output
								OUTER: for (final Piece p : mainPanel.getPieces()) {
									for (int outputIndex = 0; outputIndex < p.getOutputs().length; outputIndex++) {

										// if the piece we are trying to output
										// to already has a piece outputting to
										// it
										if (piece == p.getOutput(outputIndex).getOutput()
												&& j == p.getOutput(outputIndex).getOutputPort()) {
											// make the other piece output to
											// nothing
											p.setOutput(outputIndex, new Connection(null, 0));
											break OUTER;
										}
									}
								}

								piecePortDragged.get().getOutput(portSelected.get())
										.changeInput(ProgramValueNothing.NOTHING);
								piecePortDragged.get().setOutput(portSelected.get(), new Connection(piece, j));
							}
						}
					}
				}
			}

		} else if (!mainPanel.getSelectedPieces().isEmpty() && !portSelected.isPresent()
				&& initialPositions.isPresent()) {
			// if we just released a piece from dragging it
			if (mainPanel.pointIsInTrash(mainPanel.getWorldCoordFromMouse(e.getPoint()))) {
				// if the mouse is in the trash
				deleteSelected();
			}

		}
		pressedPosition = Optional.empty();
		initialScreenPosition = Optional.empty();

		mainPanel.getGraphicsHandler().draggingPiece = false;
		initialPositions = Optional.empty();
		portSelected = Optional.empty();
		mainPanel.getGraphicsHandler().portToMouseLine = Optional.empty();
		mainPanel.repaint();
		mainPanel.getGraphicsHandler().selectionRectangle = Optional.empty();
	}

	private void deleteSelected() {
		synchronized (mainPanel.getPieces()) {
			final Iterator<Piece> it = mainPanel.getPieces().iterator();
			// remove it
			while (it.hasNext()) {
				final Piece p = it.next();
				if (mainPanel.getSelectedPieces().contains(p)) {
					for (final Connection c : p.getOutputs()) {
						if (c.getOutput() != null) {
							c.changeInput(ProgramValueNothing.NOTHING);
						}
					}
					it.remove();
				} else {
					// if this piece outputs to this piece, remove its
					// connection
					for (int i = 0; i < p.getOutputs().length; i++) {
						if (mainPanel.getSelectedPieces().contains(p.getOutput(i).getOutput())) {
							p.setOutput(i, new Connection(null, 0));
						}
					}
				}
			}
			mainPanel.getSelectedPieces().clear();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent
	 * )
	 */
	@Override
	public void mouseDragged(final MouseEvent e) {

		if (portSelected.isPresent() && pressedPosition.isPresent() && initialPositions.isPresent()) {
			// Drag a connection
			mainPanel.getGraphicsHandler().portToMouseLine = Optional
					.of(new Line2D.Float(pressedPosition.get(), e.getPoint()));
		} else if (!mainPanel.getSelectedPieces().isEmpty() && initialPositions.isPresent()) {
			// Drag a piece
			for (int i = 0; i < mainPanel.getSelectedPieces().size(); i++) {
				final int x = initialPositions.get().get(i).x + e.getPoint().x - pressedPosition.get().x;
				final int y = initialPositions.get().get(i).y + e.getPoint().y - pressedPosition.get().y;
				mainPanel.getSelectedPieces().get(i).getPieceGraphics().setPosition(x, y);
			}
		} else if (pressedPosition.isPresent() && initialScreenPosition.isPresent()
				&& SwingUtilities.isRightMouseButton(e)) {
			// Move the background
			final int x = initialScreenPosition.get().x + pressedPosition.get().x - e.getPoint().x;
			final int y = initialScreenPosition.get().y + pressedPosition.get().y - e.getPoint().y;
			mainPanel.setViewPosition(x, y);
		} else if (pressedPosition.isPresent() && SwingUtilities.isLeftMouseButton(e)) {
			mainPanel.getSelectedPieces().clear();
			final Point mouseWorldCoord = mainPanel.getWorldCoordFromMouse(e.getPoint());
			final Point pressedWorldCoord = mainPanel.getWorldCoordFromMouse(pressedPosition.get());

			final Rectangle2D selection = getRectangle(mouseWorldCoord, pressedWorldCoord);
			mainPanel.getGraphicsHandler().selectionRectangle = Optional.of(selection);
			// check pieces in selection boundaries
			for (final Piece p : mainPanel.getPieces()) {
				Point upperLeft = p.getPieceGraphics().getPosition();
				final Rectangle2D transformedSelection = new Rectangle2D.Double(selection.getX() - upperLeft.getX(),
						selection.getY() - upperLeft.getY(), selection.getWidth(), selection.getHeight());
				if (p.getBodyShape().intersects(transformedSelection)) {
					mainPanel.getSelectedPieces().add(p);
				}
			}
		}

		mainPanel.repaint();
	}

	// Rectangles can't have negative width for some reason, so we have to
	// compensate
	private static Rectangle2D getRectangle(final Point mouseWorldCoord, final Point pressedWorldCoord) {
		Rectangle2D selection = null;
		if (mouseWorldCoord.getX() >= pressedWorldCoord.getX() && mouseWorldCoord.getY() >= pressedWorldCoord.getY()) {
			// lower right
			selection = new Rectangle2D.Float(pressedWorldCoord.x, pressedWorldCoord.y,
					mouseWorldCoord.x - pressedWorldCoord.x, mouseWorldCoord.y - pressedWorldCoord.y);
		} else if (mouseWorldCoord.getX() <= pressedWorldCoord.getX()
				&& mouseWorldCoord.getY() >= pressedWorldCoord.getY()) {
			// lower left
			selection = new Rectangle2D.Float(mouseWorldCoord.x, pressedWorldCoord.y,
					pressedWorldCoord.x - mouseWorldCoord.x, mouseWorldCoord.y - pressedWorldCoord.y);
		} else if (mouseWorldCoord.getX() >= pressedWorldCoord.getX()
				&& mouseWorldCoord.getY() <= pressedWorldCoord.getY()) {
			// upper right
			selection = new Rectangle2D.Float(pressedWorldCoord.x, mouseWorldCoord.y,
					mouseWorldCoord.x - pressedWorldCoord.x, pressedWorldCoord.y - mouseWorldCoord.y);
		} else if (mouseWorldCoord.getX() <= pressedWorldCoord.getX()
				&& mouseWorldCoord.getY() <= pressedWorldCoord.getY()) {
			// upper left
			selection = new Rectangle2D.Float(mouseWorldCoord.x, mouseWorldCoord.y,
					pressedWorldCoord.x - mouseWorldCoord.x, pressedWorldCoord.y - mouseWorldCoord.y);
		} else {
			assert false : "Should never get here";
		}
		assert selection != null;
		return selection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(final MouseEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(final MouseEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(final MouseEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(final MouseEvent e) {

		// handle double left click
		if (e.getClickCount() >= 2 && SwingUtilities.isLeftMouseButton(e)) {
			final Point worldCoord = mainPanel.getWorldCoordFromMouse(e.getPoint());
			for (int i = mainPanel.getPieces().size() - 1; i >= 0; i--) {
				final Piece piece = mainPanel.getPieces().get(i);
				if (piece.containsPoint(worldCoord)) {
					piece.doubleClicked(worldCoord);
					// make this the only selected piece
					mainPanel.getSelectedPieces().clear();
					mainPanel.getSelectedPieces().add(piece);
					return;
				}
			}
		} else if (SwingUtilities.isRightMouseButton(e)) {
			final Point worldCoord = mainPanel.getWorldCoordFromMouse(e.getPoint());
			for (int i = mainPanel.getPieces().size() - 1; i >= 0; i--) {
				final Piece piece = mainPanel.getPieces().get(i);
				if (piece.containsPoint(worldCoord)) {
					final Optional<Integer> port = piece.outputPortContainingPoint(worldCoord);
					if (port.isPresent()) {
						// Synchronized so that we do both operations together
						synchronized (mainPanel.getPieces()) {
							// set other input to nothing
							piece.getOutput(port.get()).changeInput(ProgramValueNothing.NOTHING);
							// set our output to nothing
							piece.setOutput(port.get(), new Connection(null, 0));
						}
					}
				}
			}
		}
	}

	@Override
	public void keyTyped(final KeyEvent e) {
	}

	@Override
	public void keyPressed(final KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_DELETE:
			deleteSelected();
			break;
		case KeyEvent.VK_SHIFT:
			shiftPressed = true;
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(final KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SHIFT:
			shiftPressed = false;
			break;
		default:
			break;
		}
	}

	private List<Piece> getTouchingPieces(final Point worldCoord) {
		// we are dragging from a port
		final List<Piece> collidingPieces = new ArrayList<>(mainPanel.getPieces());
		// remove all we aren't colliding with so we don't check all of them
		collidingPieces.removeIf((final Piece piece) -> !piece.containsPoint(worldCoord));
		return collidingPieces;
	}

}
