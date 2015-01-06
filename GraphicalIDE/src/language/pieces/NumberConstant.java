package language.pieces;

import ide.graphics.GraphicsConstants;

import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JOptionPane;

import language.Connection;
import language.Piece;
import language.ProgramContext;
import language.value.ProgramValueInt;

public class NumberConstant extends Piece {

	private ProgramValueInt value;

	public NumberConstant(final int value, final int x, final int y) {
		super(0, 1, x, y);
		this.value = new ProgramValueInt(value);
	}

	public NumberConstant(final int x, final int y) {
		this(0, x, y);
	}

	public static String name() {
		return "Number Constant";
	}

	@Override
	public void draw(final Graphics2D g) {
		super.draw(g);
		g.translate(getX(), getY());

		g.setColor(GraphicsConstants.PIECE_TEXT);
		g.drawString(value.toString(), BORDER_SPACE,
				2 * fontMetrics.getMaxAscent());

		g.translate(-getX(), -getY());
	}

	@Override
	public void update(final ProgramContext pc) {
		for (final Connection c : getOutputs()) {
			c.changeInput(value);
		}
	}

	public void setValue(final int value) {
		this.value = new ProgramValueInt(value);
	}

	@Override
	public void doubleClicked(final Point p) {
		try {
			setValue(Integer.parseInt(JOptionPane.showInputDialog(
					"Set Value: ", String.valueOf(value))));
		} catch (final NumberFormatException e) {
			// If the input is malformed, we don't have to do anything
		}
	}

}
