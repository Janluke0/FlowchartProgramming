package language;

import ide.GraphicsConstants;

import java.awt.Graphics;
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

	public static String MAX_LENGTH_STRING = "";
	private static List<Class<? extends Piece>> pieces = new ArrayList<>();
	private static Map<Class<? extends Piece>, String> pieceNames = new HashMap<>();

	private final ProgramValue[] inputs;
	private final Connection[] outputs;

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

	protected Piece(final int inputs, final int outputs) {
		this.inputs = new ProgramValue[inputs];
		this.outputs = new Connection[outputs];
		for (int i = 0; i < outputs; i++) {
			getOutputs()[i] = new Connection(this, i, null, 0);
		}
	}

	// Piece should take inputs and figure out its output
	public abstract void update(ProgramContext pc);

	/**
	 * Assumes that this should draw at (0,0)
	 * */
	public void draw(final Graphics g) {
		final String name = pieceNames.get(getClass());
		final int nameWidth = (int) (g.getFontMetrics().stringWidth(name) * 1.5);
		g.setColor(GraphicsConstants.PIECE_BACKGROUND);
		final int curve = 10;
		g.fillRoundRect(0, 0, nameWidth, 50, curve, curve);
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

	public static Map<Class<? extends Piece>, String> getPieceNames() {
		return pieceNames;
	}

	public void changeInput(final int inputPort, final ProgramValue value) {
		assert value != null;
		inputs[inputPort] = value;
	}

	public Connection[] getOutputs() {
		return outputs;
	}

}
