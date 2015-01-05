package ide;

import ide.graphics.GraphicsConstants;
import ide.graphics.GraphicsUtils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JPanel;

import language.NumberConstant;
import language.Piece;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {

	private static final int GRID_SEPARATOR_DISTANCE = 100;
	private static final int GRID_NUMBER_DISTANCE = 200;
	private final List<Piece> pieces = new ArrayList<>();
	private int x;
	private int y;

	// if a user is dragging from a port, this is a line between the port and the mouse
	private Optional<Line2D.Float> portToMouse = Optional.empty();

	public MainPanel() {
		super();
		pieces.add(new NumberConstant(5, 100, 10));

		pieces.add(new NumberConstant(5, 100, 10));
		x = y = 0;
		final MainInputHandler input = new MainInputHandler(this);

		addMouseListener(input);
		addMouseMotionListener(input);
	}

	@Override
	protected void paintComponent(final Graphics g2) {
		final Graphics2D g = (Graphics2D) g2;
		GraphicsUtils.prettyGraphics(g);

		super.paintComponent(g);
		g.setColor(GraphicsConstants.MAIN_BACKROUND_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());

		drawGrid(g);

		g.translate(-x, -y);
		for (final Piece p : pieces) {
			p.draw(g);
		}
		g.translate(x, y);

		if (getPortToMouse().isPresent()) {
			g.setColor(GraphicsConstants.LINE_DRAG_COLOR);
			g.draw(getPortToMouse().get());
		}

	}

	private void drawGrid(final Graphics g) {
		final int textSpaceBuffer = 5;

		g.setColor(GraphicsConstants.MAIN_GRID_COLOR);

		// Draws vertical lines
		for (int sepX = -(x % GRID_SEPARATOR_DISTANCE) - GRID_SEPARATOR_DISTANCE; sepX < getWidth(); sepX += GRID_SEPARATOR_DISTANCE) {
			if (sepX + x == 0) {
				g.setColor(GraphicsConstants.MAIN_GRID_ORIGIN_COLOR);
			} else {
				g.setColor(GraphicsConstants.MAIN_GRID_COLOR);
			}

			g.drawLine(sepX, 0, sepX, getHeight());
			// draws line coordinates if neccesary
			if ((sepX + x) % GRID_NUMBER_DISTANCE == 0) {
				g.setColor(GraphicsConstants.MAIN_GRID_COLOR);
				g.drawString(String.valueOf(sepX + x), sepX + textSpaceBuffer, g.getFontMetrics().getMaxAscent() + textSpaceBuffer);
			}
		}
		// draws horizontal lines
		for (int sepY = -(y % GRID_SEPARATOR_DISTANCE); sepY < getHeight() + GRID_SEPARATOR_DISTANCE; sepY += GRID_SEPARATOR_DISTANCE) {
			if (sepY + y == 0) {
				g.setColor(GraphicsConstants.MAIN_GRID_ORIGIN_COLOR);
			} else {
				g.setColor(GraphicsConstants.MAIN_GRID_COLOR);
			}

			g.drawLine(0, sepY, getWidth(), sepY);
			// draws line coordinates if necessary
			if ((sepY + y) % GRID_NUMBER_DISTANCE == 0) {
				g.setColor(GraphicsConstants.MAIN_GRID_COLOR);
				g.drawString(String.valueOf(sepY + y), textSpaceBuffer, sepY - textSpaceBuffer);
			}
		}
	}

	public void centerOnOrigin() {
		x = -getWidth() / 2;
		y = -getHeight() / 2;
	}

	public void setSpacePosition(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public int getSpaceX() {
		return x;
	}

	public int getSpaceY() {
		return y;
	}

	public Point getSpacePosition() {
		return new Point(x, y);
	}

	public Point getWorldCoordFromMouse(final Point p) {
		return new Point(x + p.x, y + p.y);
	}

	public List<Piece> getPieces() {
		return pieces;
	}

	public Optional<Line2D.Float> getPortToMouse() {
		return portToMouse;
	}

	public void setPortToMouse(final Optional<Line2D.Float> line) {
		portToMouse = line;
	}

}
