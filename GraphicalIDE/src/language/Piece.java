package language;

import ide.PieceTreeRepresentation;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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

	// The longest piece name of all the pieces added in the static block. This
	// is used for determining how big we have
	// to make the list for picking pieces
	/** The Constant LONGEST_PIECE_NAME. */
	public static final String LONGEST_PIECE_NAME;
	// Size of connection ports
	/** The Constant PORT_SIZE. */
	protected static final int PORT_SIZE = 20;
	// Size of gap between connection ports
	/** The Constant GAP_SIZE. */
	protected static final int GAP_SIZE = 10;
	// Size of the space border around the whole piece
	/** The Constant BORDER_SPACE. */
	protected static final int BORDER_SPACE = 5;

	// A map of added piece classes to their names
	/** The piece names. */
	private static List<PieceTreeRepresentation> pieces = new ArrayList<>();

	/** The inputs. */
	private ProgramValue<?>[] inputs;
	/** The input buffer, every tick this overwrites the inputs */
	private ProgramValue<?>[] inputBuffer;

	/** The outputs. */
	private final Connection[] outputs;

	/** The x. */
	private int x;

	/** The y. */
	private int y;
	// Defaults to this so no null pointer exception, but changes in the draw
	// method to the graphics' font metrics
	/** The font metrics. */
	protected FontMetrics fontMetrics = new Canvas().getFontMetrics(GraphicsConstants.APP_FONT);

	/**
	 * minimum width of a piece, definitely has to be at least 2 * port_size so // that they don't overlap
	 */
	protected int minWidth = 2 * PORT_SIZE + 60;

	static {
		// For every signel class subtyping Piece, we add it
		final Reflections reflections = new Reflections(Piece.class.getPackage().getName());
		for (final Class<? extends Piece> c : reflections.getSubTypesOf(Piece.class)) {
			addPiece(c);
		}

		// set the longest piece name
		String longestString = "";
		final Iterator<PieceTreeRepresentation> it = getPieceNames().iterator();
		while (it.hasNext()) {
			final PieceTreeRepresentation next = it.next();
			if (next.name.length() > longestString.length()) {
				longestString = next.name;
			}
		}
		LONGEST_PIECE_NAME = longestString;
	}

	/**
	 * Instantiates a new piece.
	 *
	 * @param inputs
	 *            the inputs
	 * @param outputs
	 *            the outputs
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	protected Piece(final int inputs, final int outputs, final int x, final int y) {
		this.inputs = new ProgramValue[inputs];
		inputBuffer = new ProgramValue[inputs];
		this.outputs = new Connection[outputs];
		for (int i = 0; i < outputs; i++) {
			setOutput(i, new Connection(null, -1));
		}
		for (int i = 0; i < inputs; i++) {
			setInput(i, ProgramValueNothing.NOTHING);
			inputBuffer[i] = ProgramValueNothing.NOTHING;
		}

		setX(x);
		setY(y);
	}

	// Piece should take inputs and figure out its output
	/**
	 * Update.
	 *
	 * @param programContext
	 *            the ProgramContext for this tick
	 */
	protected abstract void updatePiece(ProgramContext programContext);

	public void update(final ProgramContext pc) {
		inputs = inputBuffer;
		inputBuffer = new ProgramValue[inputs.length];
		for (int i = 0; i < inputBuffer.length; i++) {
			inputBuffer[i] = ProgramValueNothing.NOTHING;
		}

		updatePiece(pc);
	}

	/**
	 * Double clicked.
	 *
	 * @param clickPoint
	 *            the point in world coordinates
	 */
	public abstract void doubleClicked(Point clickPoint);

	/**
	 * Assumes that this should draw at (0,0).
	 *
	 * @param g
	 *            the g
	 */
	public void draw(final Graphics2D g) {
		// Store this variable so other methods can use it without accessing
		// graphics
		fontMetrics = g.getFontMetrics();

		final int nameHeight = fontMetrics.getMaxAscent();
		final int nameWidth = Math.max(getStringWidth(getName()), minWidth);

		g.translate(getX(), getY());

		g.setColor(GraphicsConstants.PIECE_BACKGROUND);
		g.fill(getBodyShape());

		g.setColor(GraphicsConstants.PIECE_TEXT);
		g.drawString(getName(), BORDER_SPACE, nameHeight);

		g.translate(-getX(), -getY());

	}

	public void drawConnections(final Graphics2D g) {

		final int nameWidth = Math.max(getStringWidth(getName()), minWidth);
		final int nameHeight = fontMetrics.getMaxAscent();

		g.translate(getX(), getY());
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
	 * Output port containing point.
	 *
	 * @param worldCoord
	 *            the world coord
	 * @return the index of the connection that was clicked on
	 */
	public Optional<Integer> outputPortContainingPoint(final Point worldCoord) {
		final Point worldCoordCopy = new Point(worldCoord);
		// the body shape is at 0,0 so we have to translate that by its x and y
		// OR translate our point by -x and -y
		worldCoordCopy.translate(-getX(), -getY());

		final int nameWidth = Math.max(getStringWidth(getName()), minWidth);
		final int nameHeight = fontMetrics.getMaxAscent();

		for (int i = 0; i < outputs.length; i++) {
			if (new Ellipse2D.Float(nameWidth - PORT_SIZE - BORDER_SPACE, nameHeight + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i, PORT_SIZE, PORT_SIZE).contains(worldCoordCopy)) {
				return Optional.of(i);
			}
		}
		return Optional.empty();
	}

	/**
	 * Gets the string width + 2 * BORDER_SPACE
	 *
	 * @param name
	 *            the name
	 * @return the string width
	 */
	protected int getStringWidth(final String name) {
		return fontMetrics.stringWidth(name) + 2 * BORDER_SPACE;
	}

	/**
	 * Adds the piece.
	 *
	 *
	 * @param p
	 *            the p
	 */
	protected static void addPiece(final Class<? extends Piece> p) {
		String name = "";

		try {
			// Assumes subclass has a static method called name
			name = p.getMethod("name").invoke(null).toString();
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
			// if they didn't supply a name method, use the class name instead
			name = p.getSimpleName();
		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		}
		final String[] parts = name.split("\\.");
		final String[] packageString = new String[parts.length - 1];
		// don't get the name, just the folders
		for (int i = 0; i < packageString.length; i++) {
			packageString[i] = parts[i];
		}
		getPieceNames().add(new PieceTreeRepresentation(p, packageString, parts[parts.length - 1]));
	}

	/**
	 * Gets the body shape.
	 *
	 * @param width
	 *            the width
	 * @return the body shape
	 */
	private RoundRectangle2D getBodyShape() {
		final int width = Math.max(getStringWidth(getName()), minWidth);
		final int curve = 5;
		final int height = fontMetrics.getMaxAscent() + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * Math.max(getInputs().length, outputs.length);
		return new RoundRectangle2D.Float(0, 0, width, height, curve, curve);
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
		worldCoordCopy.translate(-getX(), -getY());
		return getBodyShape().contains(worldCoordCopy);
	}

	/**
	 * Gets the piece names.
	 *
	 * @return the piece names
	 */
	public static List<PieceTreeRepresentation> getPieceNames() {
		return pieces;
	}

	/**
	 * Change input.
	 *
	 * @param inputPort
	 *            the input port
	 * @param value
	 *            the value
	 */
	public void changeInput(final int inputPort, final ProgramValue<?> value) {
		assert value != null;
		setInput(inputPort, value);
	}

	private void setInput(final int inputPort, final ProgramValue<?> value) {
		inputBuffer[inputPort] = value;
	}

	/**
	 * Gets the outputs.
	 *
	 * @return the outputs
	 */
	public Connection[] getOutputs() {
		return Arrays.copyOf(outputs, outputs.length);
	}

	/**
	 * Sets the position.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void setPosition(final int x, final int y) {
		setX(x);
		setY(y);
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Point getPosition() {
		return new Point(getX(), getY());
	}

	/**
	 * Gets the inputs.
	 *
	 * @return the inputs
	 */
	public ProgramValue<?>[] getInputs() {
		return Arrays.copyOf(inputs, inputs.length);
	}

	/**
	 * p is translated so that the origin is (0,0) and the top left corner of this piece.
	 *
	 * @param i
	 *            the i
	 * @param p
	 *            the p
	 * @return true, if successful
	 */
	public boolean inputContainsPoint(final int i, final Point p) {
		return new Ellipse2D.Float(BORDER_SPACE, fontMetrics.getMaxAscent() + GAP_SIZE + (PORT_SIZE + GAP_SIZE) * i, PORT_SIZE, PORT_SIZE).contains(p);
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x
	 *            the new x
	 */
	public void setX(final int x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @param y
	 *            the new y
	 */
	public void setY(final int y) {
		this.y = y;
	}

	/**
	 * Sets the output.
	 *
	 * @param index
	 *            the index
	 * @param connection
	 *            the connection
	 */
	public void setOutput(final int index, final Connection connection) {
		outputs[index] = connection;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		String name = null;
		for (final PieceTreeRepresentation p : pieces) {
			if (p.clazz.equals(getClass())) {
				name = p.name;
			}
		}

		if (name == null) {
			return getClass().getSimpleName();
		}
		return name;
	}

	public Connection getOutput(final int index) {
		return outputs[index];
	}

}
