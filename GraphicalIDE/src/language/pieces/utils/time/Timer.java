package language.pieces.utils.time;

import ide.graphics.GraphicsConstants;

import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JOptionPane;

import language.Connection;
import language.Piece;
import language.ProgramContext;
import language.value.ProgramValueBoolean;

/**
 * The Class Time.
 */
public class Timer extends Piece {

	private long lastTime = System.currentTimeMillis();
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
		this.interval = interval;
		minWidth = 2 * PORT_SIZE + 160;
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
	 * @see language.Piece#draw(java.awt.Graphics2D)
	 */
	@Override
	public void draw(final Graphics2D g) {
		final String intervalString = String.valueOf(interval);
		minWidth = getStringWidth(intervalString) + PORT_SIZE + 2
				* BORDER_SPACE;

		super.draw(g);
		g.translate(getX(), getY());

		g.setColor(GraphicsConstants.PIECE_TEXT);
		g.drawString(intervalString, BORDER_SPACE,
				2 * fontMetrics.getMaxAscent());

		g.translate(-getX(), -getY());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see language.Piece#update(language.ProgramContext)
	 */
	@Override
	public void updatePiece(final ProgramContext pc) {
		if (pc.TIME > lastTime + interval) {
			lastTime = pc.TIME;
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
				interval = Integer.parseInt(input);
			}
		} catch (final NumberFormatException e) {
			// If the input is malformed, we don't have to do anything
		}
	}

}
