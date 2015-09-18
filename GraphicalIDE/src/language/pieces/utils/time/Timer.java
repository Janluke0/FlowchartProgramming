package language.pieces.utils.time;

import java.awt.Point;

import javax.swing.JOptionPane;

import language.Connection;
import language.Piece;
import language.ProgramContext;
import language.type.Type;
import language.value.ProgramValueBoolean;

/**
 * The Class Time.
 */
public class Timer extends Piece {

	private long lastTime;
	private int interval;

	/**
	 * Instantiates a new time piece.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public Timer(final int interval, final int x, final int y) {
		super(0, 1, x, y);
		setInterval(interval);
	}

	/**
	 * Instantiates a new time piece.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public Timer(final int x, final int y) {
		this(1000, 0, 0);
	}

	/**
	 * returns the piece name.
	 *
	 * @return the string
	 */
	public static String name() {
		return "Utils.Time.Timer";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see language.Piece#update(language.ProgramContext)
	 */
	@Override
	public void updatePiece() {
		if (ProgramContext.getTime() > lastTime + interval) {
			lastTime = ProgramContext.getTime();
			for (final Connection c : getOutputs()) {
				c.changeInput(ProgramValueBoolean.TRUE);
			}
		} else {
			for (final Connection c : getOutputs()) {
				c.changeInput(ProgramValueBoolean.FALSE);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see language.Piece#doubleClicked(java.awt.Point)
	 */
	@Override
	public void doubleClicked(final Point p) {
		try {
			final String input = JOptionPane.showInputDialog("Set Value: ",
					String.valueOf(interval));
			if (input != null) {
				setInterval(Integer.parseInt(input));
			}
		} catch (final NumberFormatException e) {
			// If the input is malformed, we don't have to do anything
		}
	}

	private void setInterval(final int interval) {
		this.interval = interval;
		setOutputText(0, String.valueOf(interval));

	}

	@Override
	public boolean shouldUpdateEveryTick() {
		return true;
	}

	@Override
	public Type getOutputType() {
		return Type.BOOLEAN;
	}
}
