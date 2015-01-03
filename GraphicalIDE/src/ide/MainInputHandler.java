package ide;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Optional;

public class MainInputHandler implements MouseListener, MouseMotionListener {

	private final MainPanel mainPanel;

	private Optional<Point> pressedPosition = Optional.empty();
	private Optional<Point> initialPosition = Optional.empty();

	public MainInputHandler(final MainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(final MouseEvent e) {
		pressedPosition = Optional.of(e.getPoint());
		initialPosition = Optional.of(mainPanel.getSpacePosition());
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		pressedPosition = Optional.empty();
		initialPosition = Optional.empty();
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
		if (pressedPosition.isPresent() && initialPosition.isPresent()) {
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
