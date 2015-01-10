package language.pieces.logic.operators;

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
	 * Instantiates a new adds the.
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
	public void update(final ProgramContext pc) {
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

	/*
	 * (non-Javadoc)
	 *
	 * @see language.Piece#doubleClicked(java.awt.Point)
	 */
	@Override
	public void doubleClicked(final Point p) {
		final String input = JOptionPane.showInputDialog("Set Value: ", String.valueOf(currentValue.getValue()));
		if (input != null) {
			currentValue = new ProgramValueBoolean(Boolean.valueOf(input));
		}
	}

}
