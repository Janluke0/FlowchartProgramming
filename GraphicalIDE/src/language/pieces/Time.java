package language.pieces;

import ide.graphics.GraphicsConstants;

import java.awt.Graphics2D;
import java.awt.Point;
import java.math.BigDecimal;

import language.Connection;
import language.Piece;
import language.ProgramContext;
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
		minWidth = 2 * PORT_SIZE + 160;
	}

	/**
	 * returns the piece name.
	 *
	 * @return the string
	 */
	public static String name() {
		return "Utils.Time";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see language.Piece#draw(java.awt.Graphics2D)
	 */
	@Override
	public void draw(final Graphics2D g) {
		final String timeString = String.valueOf(lastTime);
		minWidth = getStringWidth(timeString) + PORT_SIZE + 2 * BORDER_SPACE;

		super.draw(g);
		g.translate(getX(), getY());

		g.setColor(GraphicsConstants.PIECE_TEXT);
		g.drawString(timeString, BORDER_SPACE, 2 * fontMetrics.getMaxAscent());

		g.translate(-getX(), -getY());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see language.Piece#update(language.ProgramContext)
	 */
	@Override
	public void update(final ProgramContext pc) {
		lastTime = pc.TIME;
		final ProgramValue<?> value = new ProgramValueNum(new BigDecimal(
				lastTime));
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

}
