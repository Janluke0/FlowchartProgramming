package language.pieces.logic.operators;

import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JOptionPane;

import language.Connection;
import language.Piece;
import language.ProgramContext;
import language.value.ProgramValue;
import language.value.ProgramValueBoolean;

// TODO: Auto-generated Javadoc
/**
 * The Class Add.
 */
public class FlipFlop extends Piece {

	ProgramValueBoolean currentValue = new ProgramValueBoolean(false);

	/**
	 * Instantiates a new flip flop piece
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public FlipFlop(final int x, final int y) {
		super(1, 1, x, y);
	}

	/**
	 * Name.
	 *
	 * @return the string
	 */
	public static String name() {
		return "Logic.Operators.FlipFlop";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see language.Piece#update(language.ProgramContext)
	 */
	@Override
	public void updatePiece(final ProgramContext pc) {
		final ProgramValue<?> v1 = getInputs()[0];
		if (v1 instanceof ProgramValueBoolean) {
			if (((ProgramValueBoolean) v1).getValue() == true) {
				currentValue = new ProgramValueBoolean(!currentValue.getValue());
			}

		}
		for (final Connection c : getOutputs()) {
			c.changeInput(currentValue);
		}
	}

	@Override
	public void draw(final Graphics2D g) {
		super.draw(g);
		drawInputPortText(g, 0, String.valueOf(currentValue.getValue()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see language.Piece#doubleClicked(java.awt.Point)
	 */
	@Override
	public void doubleClicked(final Point p) {
		final String input = JOptionPane.showInputDialog("Set Value: ",
				String.valueOf(currentValue.getValue()));
		if (input != null) {
			currentValue = new ProgramValueBoolean(Boolean.valueOf(input));
		}
	}

}
