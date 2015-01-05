package language;

import ide.graphics.GraphicsConstants;
import ide.graphics.GraphicsUtils;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import language.value.ProgramValue;

/**
 * Abstract class. To implement this class, you must add a method with the signature<br>
 * <code>public static String name()</code>
 * */
public abstract class Piece {

	public static final String MAX_LENGTH_STRING;
	private static final int PORT_SIZE = 20;
	private static final int GAP_SIZE = 10;
	private static final int BORDER_SPACE = 5;

	private static List<Class<? extends Piece>> pieces = new ArrayList<>();
	private static Map<Class<? extends Piece>, String> pieceNames = new HashMap<>();

	private final ProgramValue[] inputs;
	private final Connection[] outputs;

	private int x;
	private int y;
	private FontMetrics fontMetrics;

	static {
		addPiece(NumberConstant.class);

		String longestString = "";
		final Iterator<String> it = getPieceNames().values().iterator();
		while (it.hasNext()) {
			final String next = it.next();
			if (next.length() > longestString.length()) {
				longestString = next;
			}
		}
		MAX_LENGTH_STRING = longestString;
	}

	protected Piece(final int inputs, final int outputs, final int x, final int y) {
		this.inputs = new ProgramValue[inputs];
		this.outputs = new Connection[outputs];
		for (int i = 0; i < outputs; i++) {
			getOutputs()[i] = new Connection(this, i, null, 0);
		}
		setX(x);
		setY(y);
	}

	// Piece should take inputs and figure out its output
	public abstract void update(ProgramContext pc);

	/**
	 * Assumes that this should draw at (0,0)
	 * */
	public void draw(final Graphics2D g) {
		g.translate(getX(), getY());
		g.setColor(GraphicsConstants.PIECE_BACKGROUND);
		final String name = pieceNames.get(getClass());
		// Store this variable so other methods can use it without accessing graphics
		fontMetrics = g.getFontMetrics();
		final int nameWidth = getNameWidth(name);
		g.fill(getBodyShape(nameWidth));

		final int nameHeight = fontMetrics.getMaxAscent();

		g.setColor(GraphicsConstants.PIECE_TEXT);
		g.drawString(name, BORDER_SPACE, nameHeight);

		g.setColor(GraphicsConstants.PORT_COLOR);
		for (int i = 0; i < getInputs().length; i++) {
			g.drawOval(BORDER_SPACE, nameHeight + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i, PORT_SIZE, PORT_SIZE);
		}
		for (int i = 0; i < outputs.length; i++) {
			g.drawOval(nameWidth - PORT_SIZE - BORDER_SPACE, nameHeight + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i, PORT_SIZE, PORT_SIZE);
			if (outputs[i] != null && outputs[i].getOutput() != null) {
				g.setColor(GraphicsConstants.LINE_DRAG_COLOR);
				final Point p1 = new Point(nameWidth - PORT_SIZE - BORDER_SPACE + PORT_SIZE / 2, nameHeight + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i + PORT_SIZE / 2);
				final Point p2 = outputs[i].getOutput().getPosition();
				p2.translate(-x, -y);
				p2.translate(BORDER_SPACE + PORT_SIZE / 2, nameHeight + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i + PORT_SIZE / 2);
				GraphicsUtils.drawBezierCurve(g, p1, p2);
				g.setColor(GraphicsConstants.PORT_COLOR);
			}
		}

		g.translate(-getX(), -getY());
	}

	/**
	 * @param a
	 *            point in world space
	 * @return the index of the connection that was clicked on
	 * */
	public Optional<Integer> outputPortContainingPoint(final Point worldCoord) {
		final Point worldCoordCopy = new Point(worldCoord);
		// the body shape is at 0,0 so we have to translate that by its x and y OR translate our point by -x and -y
		worldCoordCopy.translate(-getX(), -getY());

		final int nameWidth = getNameWidth(pieceNames.get(getClass()));
		final int nameHeight = fontMetrics.getMaxAscent();

		for (int i = 0; i < outputs.length; i++) {
			if (new Ellipse2D.Float(nameWidth - PORT_SIZE - BORDER_SPACE, nameHeight + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i, PORT_SIZE, PORT_SIZE).contains(worldCoordCopy)) {
				return Optional.of(i);
			}
		}
		return Optional.empty();
	}

	private int getNameWidth(final String name) {
		return (int) (fontMetrics.stringWidth(name) * 1.5);
	}

	public static List<Class<? extends Piece>> values() {
		return pieces;
	}

	private static void addPiece(final Class<? extends Piece> p) {
		pieces.add(p);
		try {
			// Assumes subclass has a static method called name
			getPieceNames().put(p, p.getMethod("name").invoke(null).toString());
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
			// if they didn't supply a name method, use the class name instead
			getPieceNames().put(p, p.getSimpleName());
		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private RoundRectangle2D getBodyShape(final int nameWidth) {
		final int curve = 5;
		final int height = fontMetrics.getMaxAscent() + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * Math.max(getInputs().length, outputs.length);
		return new RoundRectangle2D.Float(0, 0, nameWidth, height, curve, curve);
	}

	public boolean containsPoint(final Point worldCoord) {
		final Point worldCoordCopy = new Point(worldCoord);
		// the body shape is at 0,0 so we have to translate that by its x and y OR translate our point by -x and -y
		worldCoordCopy.translate(-getX(), -getY());
		return getBodyShape(getNameWidth(pieceNames.get(getClass()))).contains(worldCoordCopy);
	}

	public static Map<Class<? extends Piece>, String> getPieceNames() {
		return pieceNames;
	}

	public void changeInput(final int inputPort, final ProgramValue value) {
		assert value != null;
		getInputs()[inputPort] = value;
	}

	protected Connection[] getOutputs() {
		return outputs;
	}

	public void setPosition(final int x, final int y) {
		setX(x);
		setY(y);
	}

	public Point getPosition() {
		return new Point(getX(), getY());
	}

	public ProgramValue[] getInputs() {
		return inputs;
	}

	/**
	 * p is translated so that the origin is (0,0) and the top left corner of this piece
	 * */
	public boolean inputContainsPoint(final int i, final Point p) {
		return new Ellipse2D.Float(BORDER_SPACE, fontMetrics.getMaxAscent() + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i, PORT_SIZE, PORT_SIZE).contains(p);
	}

	public int getX() {
		return x;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	public void setOutput(final int index, final Connection connection) {
		outputs[index] = connection;
	}

}
