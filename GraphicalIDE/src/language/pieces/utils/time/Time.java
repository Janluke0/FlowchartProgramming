package language.pieces.utils.time;

import java.awt.Graphics2D;
import java.awt.Point;
import java.math.BigDecimal;

import language.Connection;
import language.Piece;
import language.ProgramContext;
import language.type.Type;
import language.value.ProgramValue;
import language.value.ProgramValueNum;

/**
 * The Class Time.
 */
public class Time extends Piece {

	private long lastTime = System.currentTimeMillis();

	/**
	 * Instantiates a new time piece.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public Time(final int x, final int y) {
		super(0, 1, x, y);
	}

	/**
	 * returns the piece name.
	 *
	 * @return the string
	 */
	public static String name() {
		return "Utils.Time.Time";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see language.Piece#draw(java.awt.Graphics2D)
	 */
	@Override
	public void draw(final Graphics2D g) {
		final String timeString = String.valueOf(lastTime);
		setOutputText(0, timeString);

		super.draw(g);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see language.Piece#update(language.ProgramContext)
	 */
	@Override
	public void updatePiece(final ProgramContext context) {
		lastTime = context.getTime();
		final ProgramValue<?> value = new ProgramValueNum(new BigDecimal(lastTime));
		for (final Connection c : getOutputs()) {
			c.changeInput(value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see language.Piece#doubleClicked(java.awt.Point)
	 */
	@Override
	public void doubleClicked(final Point p) {

	}

	@Override
	public boolean shouldUpdateEveryTick() {
		return true;
	}

	@Override
	public Type getOutputType() {
		return Type.NUMBER;
	}

}
