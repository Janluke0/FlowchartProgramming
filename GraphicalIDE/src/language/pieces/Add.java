package language.pieces;

import java.awt.Point;

import language.Connection;
import language.Piece;
import language.ProgramContext;
import language.value.ProgramValue;
import language.value.ProgramValueNothing;
import language.value.ProgramValueNum;

// TODO: Auto-generated Javadoc
/**
 * The Class Add.
 */
public class Add extends Piece {

	/**
	 * Instantiates a new adds the.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public Add(final int x, final int y) {
		super(2, 1, x, y);
	}

	/**
	 * Name.
	 *
	 * @return the string
	 */
	public static String name() {
		return "Numbers.Math.Add";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see language.Piece#update(language.ProgramContext)
	 */
	@Override
	public void update(final ProgramContext pc) {
		final ProgramValue<?> v1 = getInputs()[0];
		final ProgramValue<?> v2 = getInputs()[1];
		if (v1 instanceof ProgramValueNum && v2 instanceof ProgramValueNum) {
			final ProgramValueNum v3 = new ProgramValueNum(((ProgramValueNum) v1).getValue().add(((ProgramValueNum) v2).getValue()));
			for (final Connection c : getOutputs()) {
				c.changeInput(v3);
			}
		} else {
			for (final Connection c : getOutputs()) {
				c.changeInput(ProgramValueNothing.NOTHING);
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
		// TODO Auto-generated method stub

	}

}
