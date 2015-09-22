package ide.graphics;

import java.awt.Canvas;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Optional;

import language.Piece;

public class PieceRenderer {

	/** Size of connection ports. */
	public static final int PORT_SIZE = 20;

	/** Size of gap between connection ports */
	public static final int GAP_SIZE = 10;

	/** Size of the space border around the whole piece */
	public static final int BORDER_SPACE = 5;

	/**
	 * Defaults to this so no null pointer exception, but changes in the draw method to the graphics' font metrics.
	 */
	protected FontMetrics fontMetrics = new Canvas().getFontMetrics(GraphicsConstants.APP_FONT);

	/**
	 * minimum width of a piece, definitely has to be at least 2 * port_size so that they don't overlap
	 */
	private int width = 2 * PORT_SIZE + 60;

	private final Piece piece;

	/** The strings to display the inputs */
	private final String[] inputDisplays;
	/** The strings to display the outputs */
	private final String[] outputDisplays;

	public PieceRenderer(final Piece piece) {
		this.piece = piece;
		inputDisplays = new String[piece.getInputs().length];
		outputDisplays = new String[piece.getOutputs().length];

		for (int i = 0; i < outputDisplays.length; i++) {
			outputDisplays[i] = "";
		}
		for (int i = 0; i < inputDisplays.length; i++) {
			inputDisplays[i] = "";
		}
		updateWidth();
	}

	private void updateWidth() {
		int newWidth = 0;
		newWidth += BORDER_SPACE;
		if (piece.getInputs().length != 0) {
			newWidth += PORT_SIZE + BORDER_SPACE;
		}
		int maxInputWidth = 0;
		for (final String s : inputDisplays) {
			if (fontMetrics.stringWidth(s) > maxInputWidth) {
				maxInputWidth = fontMetrics.stringWidth(s);
			}
		}
		newWidth += maxInputWidth + BORDER_SPACE;
		int maxOutputLength = 0;
		for (final String s : outputDisplays) {
			if (fontMetrics.stringWidth(s) > maxInputWidth) {
				maxOutputLength = fontMetrics.stringWidth(s);
			}
		}
		newWidth += maxOutputLength + BORDER_SPACE;
		if (piece.getOutputs().length != 0) {
			newWidth += PORT_SIZE + BORDER_SPACE;
		}

		// only grow, don't shrink to avoid flickering when the width alternates
		width = Math.max(width, Math.max(newWidth, fontMetrics.stringWidth(piece.getName())));
	}

	float connectionStrokeOffset = GraphicsConstants.DASH_LENGTH * 2;

	public void drawConnections(final Graphics2D g) {
		connectionStrokeOffset -= GraphicsConstants.DASH_SPEED;
		if (connectionStrokeOffset <= 0) {
			connectionStrokeOffset = GraphicsConstants.DASH_LENGTH * 2;
		}

		final int nameWidth = Math.max(getStringWidth(fontMetrics, piece.getName()), width);
		final int nameHeight = fontMetrics.getMaxAscent();

		for (int i = 0; i < piece.getOutputs().length; i++) {
			if (piece.getOutput(i) != null && piece.getOutput(i).getOutput() != null) {
				g.setColor(GraphicsConstants.TYPE_COLORS.get(piece.getOutputType()));
				final Point p1 = new Point(piece.getX() + nameWidth - PORT_SIZE - BORDER_SPACE + PORT_SIZE / 2, piece.getY() + nameHeight + GAP_SIZE
						+ (PORT_SIZE + GAP_SIZE) * i + PORT_SIZE / 2);
				final Point p2 = piece.getOutput(i).getOutput().getPosition();
				final int inputIndex = piece.getOutput(i).getOutputPort();
				p2.translate(BORDER_SPACE + PORT_SIZE / 2, nameHeight + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * inputIndex + PORT_SIZE / 2);

				if (piece.shouldUpdateEveryTick() || piece.shouldUpdateNextTick()) {
					GraphicsUtils.drawCurve(g, p1, p2, GraphicsConstants.getConnectionUpdateStroke((int) connectionStrokeOffset));

				} else {
					GraphicsUtils.drawCurve(g, p1, p2, GraphicsConstants.CONNECTION_NON_UPDATE_STROKE);
				}
			}
		}
	}

	public void drawPortText(final Graphics2D g) {

		for (int i = 0; i < inputDisplays.length; i++) {
			int x = BORDER_SPACE;
			if (piece.getInputs().length > 0) {
				x += PORT_SIZE + BORDER_SPACE;
			}

			g.drawString(inputDisplays[i], x,
					(int) (fontMetrics.getMaxAscent() + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i + PORT_SIZE * 1.5 - fontMetrics.getAscent()));
		}
		for (int i = 0; i < outputDisplays.length; i++) {
			int portWidth = 0;
			if (piece.getOutputs().length != 0) {
				portWidth = PORT_SIZE + BORDER_SPACE;
			}

			g.drawString(outputDisplays[i], width - fontMetrics.stringWidth(outputDisplays[i]) - BORDER_SPACE - portWidth,
					(int) (fontMetrics.getMaxAscent() + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i + PORT_SIZE * 1.5 - fontMetrics.getAscent()));
		}
	}

	public void draw(final Graphics2D g) {
		// Store this variable so other methods can use it without accessing
		// graphics
		fontMetrics = g.getFontMetrics();

		updateWidth();

		final int nameHeight = fontMetrics.getMaxAscent();
		final int nameWidth = Math.max(getStringWidth(fontMetrics, piece.getName()), width);

		g.translate(piece.getX(), piece.getY());

		g.setColor(GraphicsConstants.PIECE_BACKGROUND);
		g.fill(getBodyShape());

		g.setColor(GraphicsConstants.PORT_COLOR);
		for (int i = 0; i < piece.getInputs().length; i++) {
			g.drawOval(BORDER_SPACE, nameHeight + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i, PORT_SIZE, PORT_SIZE);
		}

		g.setColor(GraphicsConstants.PORT_COLOR);
		for (int i = 0; i < piece.getOutputs().length; i++) {
			g.drawOval(nameWidth - PORT_SIZE - BORDER_SPACE, nameHeight + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i, PORT_SIZE, PORT_SIZE);
		}

		g.setColor(GraphicsConstants.PIECE_TEXT);
		g.drawString(piece.getName(), BORDER_SPACE, nameHeight);

		drawPortText(g);

		g.translate(-piece.getX(), -piece.getY());
	}

	/**
	 * Output port containing point.
	 *
	 * @param worldCoord
	 *            the world coordinate
	 * @return the index of the connection that was clicked on
	 */
	public Optional<Integer> outputPortContainingPoint(final Point worldCoord) {
		final Point worldCoordCopy = new Point(worldCoord);
		// the body shape is at 0,0 so we have to translate that by its x and y
		// OR translate our point by -x and -y
		worldCoordCopy.translate(-piece.getX(), -piece.getY());

		final int nameWidth = Math.max(getStringWidth(fontMetrics, piece.getName()), width);
		final int nameHeight = fontMetrics.getMaxAscent();

		for (int i = 0; i < piece.getOutputs().length; i++) {
			if (new Ellipse2D.Float(nameWidth - PORT_SIZE - BORDER_SPACE, nameHeight + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i, PORT_SIZE, PORT_SIZE)
			.contains(worldCoordCopy)) {
				return Optional.of(i);
			}
		}
		return Optional.empty();
	}

	/**
	 * Gets the body shape.
	 *
	 * @param width
	 *            the width
	 * @return the body shape
	 */
	public RoundRectangle2D getBodyShape() {
		final int newWidth = Math.max(getStringWidth(fontMetrics, piece.getName()), width);
		final int curve = 5;
		final int height = fontMetrics.getMaxAscent() + GAP_SIZE + (PORT_SIZE + GAP_SIZE)
				* Math.max(piece.getInputs().length, piece.getOutputs().length);
		return new RoundRectangle2D.Float(0, 0, newWidth, height, curve, curve);
	}

	/**
	 * Gets the string width + 2 * BORDER_SPACE
	 *
	 * @param name
	 *            the name
	 * @return the string width
	 */
	protected static int getStringWidth(final FontMetrics fontMetrics, final String name) {
		return fontMetrics.stringWidth(name) + 2 * BORDER_SPACE;
	}

	/**
	 * Contains point.
	 *
	 * @param worldCoord
	 *            the world coord
	 * @return true, if successful
	 */
	public boolean containsPoint(final Point worldCoord) {
		final Point worldCoordCopy = new Point(worldCoord);
		// the body shape is at 0,0 so we have to translate that by its x and y
		// OR translate our point by -x and -y
		worldCoordCopy.translate(-piece.getX(), -piece.getY());
		return getBodyShape().contains(worldCoordCopy);
	}

	public boolean inputContainsPoint(final int i, final Point p) {
		return new Ellipse2D.Float(BORDER_SPACE, fontMetrics.getMaxAscent() + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i, PORT_SIZE, PORT_SIZE)
		.contains(p);
	}

	public void setInputDisplay(final int port, final String text) {
		inputDisplays[port] = text;
	}

	public void setOutputDisplay(final int port, final String text) {
		outputDisplays[port] = text;
	}
}
