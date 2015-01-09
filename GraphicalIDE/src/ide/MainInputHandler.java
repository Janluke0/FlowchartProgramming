package ide;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
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
public class MainInputHandler implements MouseListener, MouseMotionListener {

	/** The main panel. */
	private final MainPanel mainPanel;

	/** The pressed position. */
	private Optional<Point> pressedPosition = Optional.empty();

	/** The initial position. */
	private Optional<Point> initialPosition = Optional.empty();

	/** The piece dragged. */
	private Optional<Piece> pieceDragged = Optional.empty();

	/** The piece initial position. */
	private Optional<Point> pieceInitialPosition = Optional.empty();

	/** The port selected. */
	private Optional<Integer> portSelected = Optional.empty();

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
		final List<Piece> collidingPieces = new ArrayList<>(
				mainPanel.getPieces());
		final Point worldCoord = mainPanel.getWorldCoordFromMouse(e.getPoint());
		collidingPieces.removeIf((final Piece piece) -> !piece
				.containsPoint(worldCoord));
		if (collidingPieces.isEmpty()) {
			// we aren't clicking on anything
			mainPanel.getGraphicsHandler().draggingPiece = false;
			pieceDragged = Optional.empty();
			pieceInitialPosition = Optional.empty();

		} else {

			// we are clicking on one or more pieces, drag the top piece (last
			// in the list)
			final Piece selected = collidingPieces
					.get(collidingPieces.size() - 1);

			final Optional<Integer> outputPortSelected = selected
					.outputPortContainingPoint(worldCoord);
			if (outputPortSelected.isPresent()) {
				// we selected a port
				portSelected = outputPortSelected;
			} else {
				mainPanel.getGraphicsHandler().draggingPiece = true;
			}
			pieceDragged = Optional.of(selected);
			pieceInitialPosition = Optional
					.of(pieceDragged.get().getPosition());
		}

		pressedPosition = Optional.of(e.getPoint());
		initialPosition = Optional.of(mainPanel.getViewPosition());
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
			// we are dragging from a port
			final List<Piece> collidingPieces = new ArrayList<>(
					mainPanel.getPieces());
			final Point worldCoord = mainPanel.getWorldCoordFromMouse(e
					.getPoint());
			// remove all we aren't colliding with so we don't check all of them
			collidingPieces.removeIf((final Piece piece) -> !piece
					.containsPoint(worldCoord));
			if (!collidingPieces.isEmpty()) {
				// if we are in a piece
				for (int i = collidingPieces.size() - 1; i >= 0; --i) {
					// check if we're touching an output, backwards so we check
					// from top to bottom
					final Piece piece = collidingPieces.get(i);
					final Point pieceCoord = new Point(worldCoord);
					pieceCoord.translate(-piece.getX(), -piece.getY());
					for (int j = 0; j < piece.getInputs().length; j++) {
						if (piece.inputContainsPoint(j, pieceCoord)) {
							synchronized (mainPanel.getPieces()) {
								// check if any other pieces are using this as
								// an output
								OUTER: for (final Piece p : mainPanel
										.getPieces()) {
									for (int outputIndex = 0; outputIndex < p
											.getOutputs().length; outputIndex++) {

										// if the piece we are trying to output
										// to already has a piece outputting to
										// it
										if (piece == p.getOutput(outputIndex)
												.getOutput()
												&& j == p
												.getOutput(outputIndex)
												.getOutputPort()) {
											// make the other piece output to
											// nothing
											p.setOutput(outputIndex,
													new Connection(null, 0));
											break OUTER;
										}
									}
								}

								pieceDragged
							.get()
							.getOutput(portSelected.get())
							.changeInput(
									ProgramValueNothing.NOTHING);
								pieceDragged.get().setOutput(
										portSelected.get(),
										new Connection(piece, j));
							}
						}
					}
				}
			}

		} else if (pieceDragged.isPresent() && !portSelected.isPresent()) {
			// if we just released a piece from dragging it
			if (mainPanel.pointIsInTrash(mainPanel.getWorldCoordFromMouse(e
					.getPoint()))) {
				// if the mouse is in the trash
				synchronized (mainPanel.getPieces()) {
					final Iterator<Piece> it = mainPanel.getPieces().iterator();
					// remove it
					while (it.hasNext()) {
						final Piece p = it.next();
						if (p == pieceDragged.get()) {
							for (final Connection c : p.getOutputs()) {
								if (c.getOutput() != null) {
									c.changeInput(ProgramValueNothing.NOTHING);
								}
							}
							it.remove();
							break;
						}
					}
				}
			}

		}
		pressedPosition = Optional.empty();
		initialPosition = Optional.empty();
		pieceDragged = Optional.empty();

		mainPanel.getGraphicsHandler().draggingPiece = false;
		pieceInitialPosition = Optional.empty();
		portSelected = Optional.empty();
		mainPanel.getGraphicsHandler().portToMouseLine = Optional.empty();
		mainPanel.repaint();
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

		if (portSelected.isPresent() && pressedPosition.isPresent()
				&& pieceInitialPosition.isPresent()) {
			// Drag a connection
			mainPanel.getGraphicsHandler().portToMouseLine = Optional
					.of(new Line2D.Float(pressedPosition.get(), e.getPoint()));
		} else if (pieceDragged.isPresent() && pieceInitialPosition.isPresent()) {
			// Drag a piece

			final int x = pieceInitialPosition.get().x + e.getPoint().x
					- pressedPosition.get().x;
			final int y = pieceInitialPosition.get().y + e.getPoint().y
					- pressedPosition.get().y;
			pieceDragged.get().setPosition(x, y);
		} else if (pressedPosition.isPresent() && initialPosition.isPresent()) {
			// Move the background
			final int x = initialPosition.get().x + pressedPosition.get().x
					- e.getPoint().x;
			final int y = initialPosition.get().y + pressedPosition.get().y
					- e.getPoint().y;
			mainPanel.setViewPosition(x, y);
		}

		mainPanel.repaint();
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
			final Point worldCoord = mainPanel.getWorldCoordFromMouse(e
					.getPoint());
			for (int i = mainPanel.getPieces().size() - 1; i >= 0; i--) {
				final Piece piece = mainPanel.getPieces().get(i);
				if (piece.containsPoint(worldCoord)) {
					piece.doubleClicked(worldCoord);
					return;
				}
			}
		} else if (SwingUtilities.isRightMouseButton(e)) {
			final Point worldCoord = mainPanel.getWorldCoordFromMouse(e
					.getPoint());
			for (int i = mainPanel.getPieces().size() - 1; i >= 0; i--) {
				final Piece piece = mainPanel.getPieces().get(i);
				if (piece.containsPoint(worldCoord)) {
					final Optional<Integer> port = piece
							.outputPortContainingPoint(worldCoord);
					if (port.isPresent()) {
						// Synchronized so that we do both operations together
						synchronized (mainPanel.getPieces()) {
							// set other input to nothing
							piece.getOutput(port.get()).changeInput(
									ProgramValueNothing.NOTHING);
							// set our output to nothing
							piece.setOutput(port.get(), new Connection(null, 0));
						}
					}
				}
			}
		}
	}

}
