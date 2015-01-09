package language.pieces.numbers;

import ide.graphics.GraphicsConstants;

import java.awt.Graphics2D;
import java.awt.Point;
import java.math.BigDecimal;

import javax.swing.JOptionPane;

import language.Connection;
import language.Piece;
import language.ProgramContext;
import language.value.ProgramValueNum;

/**
 * The Class NumberConstant.
 */
public class NumberConstant extends Piece {

	/** The value stored by this piece. */
	private ProgramValueNum value;

	/**
	 * Instantiates a new number constant.
	 *
	 * @param value
	 *            the value
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public NumberConstant(final BigDecimal value, final int x, final int y) {
		super(0, 1, x, y);
		this.value = new ProgramValueNum(value);
	}

	/**
	 * Instantiates a new number constant with a value of 0.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public NumberConstant(final int x, final int y) {
		this(BigDecimal.ZERO, x, y);
	}

	/**
	 * returns the piece name.
	 *
	 * @return the string
	 */
	public static String name() {
		return "Numbers.Number Constant";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see language.Piece#draw(java.awt.Graphics2D)
	 */
	@Override
	public void draw(final Graphics2D g) {
		minWidth = getStringWidth(value.toString()) + PORT_SIZE + 2
				* BORDER_SPACE;

		super.draw(g);
		g.translate(getX(), getY());

		g.setColor(GraphicsConstants.PIECE_TEXT);
		g.drawString(value.toString(), BORDER_SPACE,
				2 * fontMetrics.getMaxAscent());

		g.translate(-getX(), -getY());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see language.Piece#update(language.ProgramContext)
	 */
	@Override
	public void update(final ProgramContext pc) {
		for (final Connection c : getOutputs()) {
			c.changeInput(value);
		}
	}

	/**
	 * Sets the value stored by this piece..
	 *
	 * @param value
	 *            the new value
	 */
	public void setValue(final BigDecimal value) {
		this.value = new ProgramValueNum(value);
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
					String.valueOf(value));
			if (input != null) {
				setValue(new BigDecimal(input));
			}
		} catch (final NumberFormatException e) {
			// If the input is malformed, we don't have to do anything
		}
	}

}
