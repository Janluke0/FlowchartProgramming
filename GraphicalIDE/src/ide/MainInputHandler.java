package ide;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import language.Connection;
import language.Piece;

public class MainInputHandler implements MouseListener, MouseMotionListener {

	private final MainPanel mainPanel;

	private Optional<Point> pressedPosition = Optional.empty();
	private Optional<Point> initialPosition = Optional.empty();
	private Optional<Piece> pieceDragged = Optional.empty();
	private Optional<Point> pieceInitialPosition = Optional.empty();

	private Optional<Integer> portSelected = Optional.empty();

	public MainInputHandler(final MainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		final List<Piece> collidingPieces = new ArrayList<>(mainPanel.getPieces());
		final Point worldCoord = mainPanel.getWorldCoordFromMouse(e.getPoint());
		collidingPieces.removeIf((final Piece p) -> !p.containsPoint(worldCoord));
		if (!collidingPieces.isEmpty()) {
			// we are clicking on one or more pieces, drag the top piece (last in the list)
			final Piece selected = collidingPieces.get(collidingPieces.size() - 1);

			final Optional<Integer> outputPortSelected = selected.outputPortContainingPoint(worldCoord);
			if (outputPortSelected.isPresent()) {
				// we selected a port
				portSelected = outputPortSelected;
			}
			pieceDragged = Optional.of(selected);
			pieceInitialPosition = Optional.of(pieceDragged.get().getPosition());

		} else {
			// we aren't clicking on anything
			pieceDragged = Optional.empty();
			pieceInitialPosition = Optional.empty();
		}

		pressedPosition = Optional.of(e.getPoint());
		initialPosition = Optional.of(mainPanel.getSpacePosition());
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		if (portSelected.isPresent()) {
			// we are dragging from a port
			final List<Piece> collidingPieces = new ArrayList<>(mainPanel.getPieces());
			final Point worldCoord = mainPanel.getWorldCoordFromMouse(e.getPoint());
			// remove all we aren't colliding with so we don't check all of them
			collidingPieces.removeIf((final Piece p) -> !p.containsPoint(worldCoord));
			if (!collidingPieces.isEmpty()) {
				// if we are in a piece
				for (int i = collidingPieces.size() - 1; i >= 0; --i) {
					// check if we're touching an output, backwards so we check from top to bottom
					final Piece p = collidingPieces.get(i);
					final Point pieceCoord = new Point(worldCoord);
					pieceCoord.translate(-p.getX(), -p.getY());
					for (int j = 0; j < p.getInputs().length; j++) {
						if (p.inputContainsPoint(j, pieceCoord)) {
							pieceDragged.get().setOutput(portSelected.get(), new Connection(pieceDragged.get(), portSelected.get(), p, j));
						}
					}
				}
			}
		}

		pressedPosition = Optional.empty();
		initialPosition = Optional.empty();
		pieceDragged = Optional.empty();
		pieceInitialPosition = Optional.empty();
		portSelected = Optional.empty();
		mainPanel.setPortToMouse(Optional.empty());
		mainPanel.repaint();
	}

	@Override
	public void mouseDragged(final MouseEvent e) {
		if (portSelected.isPresent()) {
			// Drag a connection
			mainPanel.setPortToMouse(Optional.of(new Line2D.Float(pressedPosition.get(), e.getPoint())));
		} else if (pieceDragged.isPresent() && pieceInitialPosition.isPresent()) {
			// Drag a piece

			final int x = pieceInitialPosition.get().x + e.getPoint().x - pressedPosition.get().x;
			final int y = pieceInitialPosition.get().y + e.getPoint().y - pressedPosition.get().y;
			pieceDragged.get().setPosition(x, y);
		} else if (pressedPosition.isPresent() && initialPosition.isPresent()) {
			// Move the background
			final int x = initialPosition.get().x + pressedPosition.get().x - e.getPoint().x;
			final int y = initialPosition.get().y + pressedPosition.get().y - e.getPoint().y;
			mainPanel.setSpacePosition(x, y);
		}

		mainPanel.repaint();
	}

	@Override
	public void mouseMoved(final MouseEvent e) {

	}

	@Override
	public void mouseEntered(final MouseEvent e) {

	}

	@Override
	public void mouseExited(final MouseEvent e) {

	}

	@Override
	public void mouseClicked(final MouseEvent e) {

	}

}
