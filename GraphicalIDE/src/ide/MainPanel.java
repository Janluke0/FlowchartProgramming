package ide;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import language.NumberConstant;
import language.Piece;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {

	private static final int SEPARATOR_DISTANCE = 100;
	private static final int NUMBER_DISTANCE = 200;
	private final List<Piece> pieces = new ArrayList<>();
	private int x;
	private int y;

	public MainPanel() {
		super();
		pieces.add(new NumberConstant(5));
		x = y = 0;
		final MainInputHandler input = new MainInputHandler(this);

		addMouseListener(input);
		addMouseMotionListener(input);
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.setColor(GraphicsConstants.MAIN_BACKROUND_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());

		final int textSpaceBuffer = 5;

		g.setColor(GraphicsConstants.MAIN_GRID_COLOR);

		// Draws vertical lines
		for (int sepX = -(x % SEPARATOR_DISTANCE); sepX < getWidth(); sepX += SEPARATOR_DISTANCE) {
			if (sepX + x == 0) {
				g.setColor(GraphicsConstants.MAIN_GRID_ORIGIN_COLOR);
			} else {
				g.setColor(GraphicsConstants.MAIN_GRID_COLOR);
			}

			g.drawLine(sepX, 0, sepX, getHeight());
			// draws line coordinates if neccesary
			if ((sepX + x) % NUMBER_DISTANCE == 0) {
				g.setColor(GraphicsConstants.MAIN_GRID_COLOR);
				g.drawString(String.valueOf(sepX + x), sepX + textSpaceBuffer, g.getFontMetrics().getMaxAscent() + textSpaceBuffer);
			}
		}
		// draws horizontal lines
		for (int sepY = -(y % SEPARATOR_DISTANCE); sepY < getHeight(); sepY += SEPARATOR_DISTANCE) {
			if (sepY + y == 0) {
				g.setColor(GraphicsConstants.MAIN_GRID_ORIGIN_COLOR);
			} else {
				g.setColor(GraphicsConstants.MAIN_GRID_COLOR);
			}

			g.drawLine(0, sepY, getWidth(), sepY);
			// draws line coordinates if necessary
			if ((sepY + y) % NUMBER_DISTANCE == 0) {
				g.setColor(GraphicsConstants.MAIN_GRID_COLOR);
				g.drawString(String.valueOf(sepY + y), textSpaceBuffer, sepY - textSpaceBuffer);
			}
		}

		g.translate(-x, -y);
		for (final Piece p : pieces) {
			p.draw(g);
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

}
