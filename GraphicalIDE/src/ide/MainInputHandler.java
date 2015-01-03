package ide;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import language.Piece;

public class MainInputHandler implements MouseListener, MouseMotionListener {

	private final MainPanel mainPanel;

	private Optional<Point> pressedPosition = Optional.empty();
	private Optional<Point> initialPosition = Optional.empty();
	private Optional<Piece> pieceDragged = Optional.empty();
	private Optional<Point> pieceInitialPosition = Optional.empty();

	public MainInputHandler(final MainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(final MouseEvent e) {
		final List<Piece> collidingPieces = new ArrayList<>(mainPanel.getPieces());
		final Point worldCoord = mainPanel.getWorldCoordFromMouse(e.getPoint());
		collidingPieces.removeIf((final Piece p) -> !p.containsPoint(worldCoord));
		if (!collidingPieces.isEmpty()) {
			// we are clicking on one or more pieces, drag the top piece (last in the list)
			pieceDragged = Optional.of(collidingPieces.get(collidingPieces.size() - 1));
			pieceInitialPosition = Optional.of(pieceDragged.get().getPosition());
		} else {
			pieceDragged = Optional.empty();
			pieceInitialPosition = Optional.empty();
		}

		pressedPosition = Optional.of(e.getPoint());
		initialPosition = Optional.of(mainPanel.getSpacePosition());
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		pressedPosition = Optional.empty();
		initialPosition = Optional.empty();
		pieceDragged = Optional.empty();
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(final MouseEvent e) {
		if (pieceDragged.isPresent() && pieceInitialPosition.isPresent()) {
			// Drag a piece

			final int x = pieceInitialPosition.get().x + e.getPoint().x - pressedPosition.get().x;
			final int y = pieceInitialPosition.get().y + e.getPoint().y - pressedPosition.get().y;
			pieceDragged.get().setPosition(x, y);
			mainPanel.repaint();
		} else if (pressedPosition.isPresent() && initialPosition.isPresent()) {
			// Move the background
			final int x = initialPosition.get().x + pressedPosition.get().x - e.getPoint().x;
			final int y = initialPosition.get().y + pressedPosition.get().y - e.getPoint().y;
			mainPanel.setSpacePosition(x, y);
			mainPanel.repaint();
		}
	}

	@Override
	public void mouseMoved(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
