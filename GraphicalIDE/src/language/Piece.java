package language;

import ide.graphics.GraphicsConstants;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.RoundRectangle2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import language.value.ProgramValue;

/**
 * Abstract class. To implement this class, you must add a method with the signature<br>
 * <code>public static String name()</code>
 * */
public abstract class Piece {

	public static final String MAX_LENGTH_STRING;
	private static List<Class<? extends Piece>> pieces = new ArrayList<>();
	private static Map<Class<? extends Piece>, String> pieceNames = new HashMap<>();

	private final ProgramValue[] inputs;
	private final Connection[] outputs;

	private int x;
	private int y;
	private int nameWidth;

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
		this.x = x;
		this.y = y;
	}

	// Piece should take inputs and figure out its output
	public abstract void update(ProgramContext pc);

	/**
	 * Assumes that this should draw at (0,0)
	 * */
	public void draw(final Graphics2D g) {
		g.translate(x, y);
		g.setColor(GraphicsConstants.PIECE_BACKGROUND);
		final String name = pieceNames.get(getClass());
		nameWidth = (int) (g.getFontMetrics().stringWidth(name) * 1.5);
		g.fill(getBodyShape(nameWidth));

		g.setColor(GraphicsConstants.PIECE_TEXT);
		g.drawString(name, 5, g.getFontMetrics().getMaxAscent());
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
		return new RoundRectangle2D.Float(0, 0, nameWidth, 50, curve, curve);
	}

	public boolean containsPoint(final Point worldCoord) {
		final Point worldCoordCopy = new Point(worldCoord);
		// the body shape is at 0,0 so we have to translate that by its x and y OR translate our point by -x and -y
		worldCoordCopy.translate(-x, -y);
		return getBodyShape(nameWidth).contains(worldCoordCopy);
	}

	public static Map<Class<? extends Piece>, String> getPieceNames() {
		return pieceNames;
	}

	public void changeInput(final int inputPort, final ProgramValue value) {
		assert value != null;
		inputs[inputPort] = value;
	}

	protected Connection[] getOutputs() {
		return outputs;
	}

	public void setPosition(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public Point getPosition() {
		return new Point(x, y);
	}

}
