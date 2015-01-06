package language;

import ide.graphics.GraphicsConstants;
import ide.graphics.GraphicsUtils;

import java.awt.Canvas;
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
import language.value.ProgramValueNothing;

import org.reflections.Reflections;

/**
 * Abstract class. To implement this class, you must add a method with the signature<br>
 * <code>public static String name()</code> <br>
 * and a default constructor with two int parameters (position)<br>
 * <code>public PieceXXX(int x, int y)</code>
 * */
public abstract class Piece {

	// The longest piece name of all the pieces added in the static block. This is used for determining how big we have
	// to make the list for picking pieces
	public static final String LONGEST_PIECE_NAME;
	// Size of connection ports
	protected static final int PORT_SIZE = 20;
	// Size of gap between connection ports
	protected static final int GAP_SIZE = 10;
	// Size of the space border around the whole piece
	protected static final int BORDER_SPACE = 5;

	// minimum width of a piece, definitely has to be at least 2 * port_size so that they don't overlap
	private static final int MIN_WIDTH = 100;

	// A list of all added piece classes
	private static List<Class<? extends Piece>> pieces = new ArrayList<>();
	// A map of added piece classes to their names
	private static Map<Class<? extends Piece>, String> pieceNames = new HashMap<>();

	static {
		final Reflections reflections = new Reflections("language");
		for (final Class<? extends Piece> c : reflections.getSubTypesOf(Piece.class)) {
			addPiece(c);
		}

		// update the longest string
		String longestString = "";
		final Iterator<String> it = getPieceNames().values().iterator();
		while (it.hasNext()) {
			final String next = it.next();
			if (next.length() > longestString.length()) {
				longestString = next;
			}
		}
		LONGEST_PIECE_NAME = longestString;
	}

	private final ProgramValue[] inputs;
	private final Connection[] outputs;

	private int x;
	private int y;
	// Defaults to this so no null pointer exception, but changes in the draw method to the graphics' font metrics
	protected FontMetrics fontMetrics = new Canvas().getFontMetrics(GraphicsConstants.APP_FONT);

	protected Piece(final int inputs, final int outputs, final int x, final int y) {
		this.inputs = new ProgramValue[inputs];
		this.outputs = new Connection[outputs];
		for (int i = 0; i < outputs; i++) {
			getOutputs()[i] = new Connection(null, 0);
		}
		for (int i = 0; i < inputs; i++) {
			getInputs()[i] = new ProgramValueNothing();
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
		final String name = getName();
		// Store this variable so other methods can use it without accessing graphics
		fontMetrics = g.getFontMetrics();
		final int nameWidth = getStringWidth(name);
		g.fill(getBodyShape(nameWidth));

		final int nameHeight = fontMetrics.getMaxAscent();

		g.setColor(GraphicsConstants.PIECE_TEXT);
		g.drawString(name, BORDER_SPACE, nameHeight);

		g.setColor(GraphicsConstants.PORT_COLOR);
		for (int i = 0; i < getInputs().length; i++) {
			g.drawOval(BORDER_SPACE, nameHeight + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i, PORT_SIZE, PORT_SIZE);
		}
		g.translate(-getX(), -getY());

		for (int i = 0; i < outputs.length; i++) {
			g.drawOval(x + nameWidth - PORT_SIZE - BORDER_SPACE, y + nameHeight + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i, PORT_SIZE, PORT_SIZE);
			if (outputs[i] != null && outputs[i].getOutput() != null) {
				g.setColor(GraphicsConstants.LINE_DRAG_COLOR);
				final Point p1 = new Point(x + nameWidth - PORT_SIZE - BORDER_SPACE + PORT_SIZE / 2, y + nameHeight + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i + PORT_SIZE / 2);
				final Point p2 = outputs[i].getOutput().getPosition();
				final int inputIndex = outputs[i].getOutputPort();
				p2.translate(BORDER_SPACE + PORT_SIZE / 2, nameHeight + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * inputIndex + PORT_SIZE / 2);
				GraphicsUtils.drawCurve(g, p1, p2);
				g.setColor(GraphicsConstants.PORT_COLOR);
			}
		}
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

		final int nameWidth = getStringWidth(getName());
		final int nameHeight = fontMetrics.getMaxAscent();

		for (int i = 0; i < outputs.length; i++) {
			if (new Ellipse2D.Float(nameWidth - PORT_SIZE - BORDER_SPACE, nameHeight + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i, PORT_SIZE, PORT_SIZE).contains(worldCoordCopy)) {
				return Optional.of(i);
			}
		}
		return Optional.empty();
	}

	private int getStringWidth(final String name) {
		return (int) Math.max(MIN_WIDTH, fontMetrics.stringWidth(name) * 1.5);
	}

	public static List<Class<? extends Piece>> values() {
		return getPieces();
	}

	protected static void addPiece(final Class<? extends Piece> p) {
		getPieces().add(p);
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
		return getBodyShape(getStringWidth(getName())).contains(worldCoordCopy);
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

	public String getName() {
		final String name = pieceNames.get(getClass());
		if (name == null) {
			return getClass().getSimpleName();
		}
		return name;
	}

	public static List<Class<? extends Piece>> getPieces() {
		return pieces;
	}

	public static void setPieces(final List<Class<? extends Piece>> pieces) {
		Piece.pieces = pieces;
	}

}
