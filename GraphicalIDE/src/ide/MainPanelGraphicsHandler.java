package ide;

import ide.graphics.GraphicsConstants;
import ide.graphics.GraphicsUtils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Optional;

import language.Piece;

public class MainPanelGraphicsHandler {
	private static final int GRID_SEPARATOR_DISTANCE = 100;
	private static final int GRID_NUMBER_DISTANCE = 200;

	// if a user is dragging from a port, this is a line between the port and
	// the mouse
	public Optional<Line2D> portToMouseLine = Optional.empty();

	private final MainPanel parent;

	public MainPanelGraphicsHandler(final MainPanel parent) {
		this.parent = parent;
	}

	public void draw(final Graphics2D g) {
		GraphicsUtils.prettyGraphics(g);
		g.setColor(GraphicsConstants.MAIN_BACKROUND_COLOR);
		g.fillRect(0, 0, parent.getWidth(), parent.getHeight());

		drawGrid(g);

		g.translate(-parent.getSpaceX(), -parent.getSpaceY());
		for (final Piece p : parent.getPieces()) {
			p.draw(g);
		}
		g.translate(parent.getSpaceX(), parent.getSpaceY());

		if (portToMouseLine.isPresent()) {
			g.setColor(GraphicsConstants.LINE_DRAG_COLOR);
			final Line2D line = portToMouseLine.get();
			GraphicsUtils.drawCurve(g, line.getP1(), line.getP2());
		}
	}

	private void drawGrid(final Graphics g) {
		final int textSpaceBuffer = 5;

		g.setColor(GraphicsConstants.MAIN_GRID_COLOR);

		// Draws vertical lines
		for (int sepX = -(parent.getSpaceX() % GRID_SEPARATOR_DISTANCE)
				- GRID_SEPARATOR_DISTANCE; sepX < parent.getWidth(); sepX += GRID_SEPARATOR_DISTANCE) {
			if (sepX + parent.getSpaceX() == 0) {
				g.setColor(GraphicsConstants.MAIN_GRID_ORIGIN_COLOR);
			} else {
				g.setColor(GraphicsConstants.MAIN_GRID_COLOR);
			}

			g.drawLine(sepX, 0, sepX, parent.getHeight());
			// draws line coordinates if neccesary
			if ((sepX + parent.getSpaceX()) % GRID_NUMBER_DISTANCE == 0) {
				g.setColor(GraphicsConstants.MAIN_GRID_COLOR);
				g.drawString(String.valueOf(sepX + parent.getSpaceX()), sepX
						+ textSpaceBuffer, g.getFontMetrics().getMaxAscent()
						+ textSpaceBuffer);
			}
		}
		// draws horizontal lines
		for (int sepY = -(parent.getSpaceY() % GRID_SEPARATOR_DISTANCE); sepY < parent
				.getHeight() + GRID_SEPARATOR_DISTANCE; sepY += GRID_SEPARATOR_DISTANCE) {
			if (sepY + parent.getSpaceY() == 0) {
				g.setColor(GraphicsConstants.MAIN_GRID_ORIGIN_COLOR);
			} else {
				g.setColor(GraphicsConstants.MAIN_GRID_COLOR);
			}

			g.drawLine(0, sepY, parent.getWidth(), sepY);
			// draws line coordinates if necessary
			if ((sepY + parent.getSpaceY()) % GRID_NUMBER_DISTANCE == 0) {
				g.setColor(GraphicsConstants.MAIN_GRID_COLOR);
				g.drawString(String.valueOf(sepY + parent.getSpaceY()),
						textSpaceBuffer, sepY - textSpaceBuffer);
			}
		}
	}
}
