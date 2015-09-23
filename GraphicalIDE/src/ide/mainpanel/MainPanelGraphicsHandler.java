package ide.mainpanel;

import ide.graphics.GraphicsConstants;
import ide.graphics.GraphicsUtils;
import ide.graphics.PieceRenderer;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Optional;

import language.Piece;

/**
 * The Class MainPanelGraphicsHandler.
 */
public class MainPanelGraphicsHandler {

	/** The Constant GRID_SEPARATOR_DISTANCE. */
	private static final int GRID_SEPARATOR_DISTANCE = 100;

	/** The Constant GRID_NUMBER_DISTANCE. */
	private static final int GRID_NUMBER_DISTANCE = 200;

	/**
	 * if a user is dragging from a port, this is a line between the port and the mouse
	 */
	public Optional<Line2D> portToMouseLine = Optional.empty();

	/** The parent. */
	private final MainPanel parent;

	public boolean draggingPiece = false;

	// world coordinates
	public Optional<Rectangle2D> selectionRectangle = Optional.empty();

	/**
	 * Instantiates a new main panel graphics handler.
	 *
	 * @param parent
	 *            the parent
	 */
	public MainPanelGraphicsHandler(final MainPanel parent) {
		this.parent = parent;
	}

	/**
	 * Draws the main panel.
	 *
	 * @param g
	 *            the Graphics2D object
	 */
	public void draw(final Graphics2D g) {
		GraphicsUtils.prettyGraphics(g);
		g.setColor(GraphicsConstants.MAIN_BACKROUND_COLOR);
		g.fillRect(0, 0, parent.getWidth(), parent.getHeight());

		drawGrid(g);

		g.translate(-parent.getViewX(), -parent.getViewY());
		for (final Piece p : parent.getPieces()) {
			p.draw(g);
		}
		for (final Piece p : parent.getPieces()) {
			p.drawConnections(g);
		}
		g.setColor(GraphicsConstants.SELECTION_COLOR);
		for (final Piece p : parent.getSelectedPieces()) {
			g.translate(p.getX(), p.getY());
			final Stroke oldStroke = g.getStroke();
			g.setStroke(new BasicStroke(GraphicsConstants.SELECTION_WIDTH));

			g.draw(p.getBodyShape());
			g.setStroke(oldStroke);
			g.translate(-p.getX(), -p.getY());
		}
		if (selectionRectangle.isPresent()) {
			g.setColor(GraphicsConstants.SELECTION_COLOR);
			g.fill(selectionRectangle.get());
		}

		g.translate(parent.getViewX(), parent.getViewY());

		if (parent.isFunction()) {
			g.setColor(GraphicsConstants.PORT_COLOR);
			for (int i = 0; i < parent.getFunctionData().getInputNumber(); i++) {
				g.fill(getInputPortOnScreen(i));
			}

			g.setColor(GraphicsConstants.PORT_COLOR);
			for (int i = 0; i < parent.getFunctionData().getOutputNumber(); i++) {
				g.fill(getOutputPortOnScreen(i));
			}
		}

		if (portToMouseLine.isPresent()) {
			g.setColor(GraphicsConstants.LINE_DRAG_COLOR);
			final Line2D line = portToMouseLine.get();
			GraphicsUtils.drawCurve(g, line.getP1(), line.getP2(), GraphicsConstants.DRAG_STROKE);
		}
		if (draggingPiece) {
			drawTrashCan(g);
		}
	}

	public Ellipse2D getInputPortOnScreen(final int input) {
		return new Ellipse2D.Double(PieceRenderer.BORDER_SPACE, parent.getHeight() / (parent.getFunctionData().getInputNumber() + 1) * (input + 1),
				PieceRenderer.PORT_SIZE, PieceRenderer.PORT_SIZE);
	}

	public Ellipse2D getOutputPortOnScreen(final int output) {
		return new Ellipse2D.Double(parent.getWidth() - PieceRenderer.PORT_SIZE - PieceRenderer.BORDER_SPACE, parent.getHeight()
				/ (parent.getFunctionData().getOutputNumber() + 1) * (output + 1), PieceRenderer.PORT_SIZE, PieceRenderer.PORT_SIZE);
	}

	private void drawTrashCan(final Graphics2D g) {
		final Image trash = GraphicsConstants.TRASH_ICON.getImage();
		g.drawImage(trash, parent.getWidth() - trash.getWidth(null) - GraphicsConstants.TRASH_BORDER_SIZE, parent.getHeight() - trash.getHeight(null)
				- GraphicsConstants.TRASH_BORDER_SIZE, null);
	}

	/**
	 * Draw grid.
	 *
	 * @param g
	 *            the g
	 */
	private void drawGrid(final Graphics g) {
		final int textSpaceBuffer = 5;

		g.setColor(GraphicsConstants.MAIN_GRID_COLOR);

		// Draws vertical lines
		for (int sepX = -(parent.getViewX() % GRID_SEPARATOR_DISTANCE) - GRID_SEPARATOR_DISTANCE; sepX < parent.getWidth(); sepX += GRID_SEPARATOR_DISTANCE) {
			if (sepX + parent.getViewX() == 0) {
				g.setColor(GraphicsConstants.MAIN_GRID_ORIGIN_COLOR);
			} else {
				g.setColor(GraphicsConstants.MAIN_GRID_COLOR);
			}

			g.drawLine(sepX, 0, sepX, parent.getHeight());
			// draws line coordinates if neccesary
			if ((sepX + parent.getViewX()) % GRID_NUMBER_DISTANCE == 0) {
				g.setColor(GraphicsConstants.MAIN_GRID_COLOR);
				g.drawString(String.valueOf(sepX + parent.getViewX()), sepX + textSpaceBuffer, g.getFontMetrics().getMaxAscent() + textSpaceBuffer);
			}
		}
		// draws horizontal lines
		for (int sepY = -(parent.getViewY() % GRID_SEPARATOR_DISTANCE); sepY < parent.getHeight() + GRID_SEPARATOR_DISTANCE; sepY += GRID_SEPARATOR_DISTANCE) {
			if (sepY + parent.getViewY() == 0) {
				g.setColor(GraphicsConstants.MAIN_GRID_ORIGIN_COLOR);
			} else {
				g.setColor(GraphicsConstants.MAIN_GRID_COLOR);
			}

			g.drawLine(0, sepY, parent.getWidth(), sepY);
			// draws line coordinates if necessary
			if ((sepY + parent.getViewY()) % GRID_NUMBER_DISTANCE == 0) {
				g.setColor(GraphicsConstants.MAIN_GRID_COLOR);
				g.drawString(String.valueOf(sepY + parent.getViewY()), textSpaceBuffer, sepY - textSpaceBuffer);
			}
		}
	}
}
