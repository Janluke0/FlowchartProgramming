package language.pieces.logic.operators;

import java.awt.Point;

import language.Connection;
import language.Piece;
import language.type.Type;
import language.value.ProgramValue;
import language.value.ProgramValueBoolean;
import language.value.ProgramValueNothing;

// TODO: Auto-generated Javadoc
/**
 * The Class Add.
 */
public class Not extends Piece {

	/**
	 * Instantiates a new adds the.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public Not(final int x, final int y) {
		super(1, 1, x, y);
	}

	/**
	 * Name.
	 *
	 * @return the string
	 */
	public static String name() {
		return "Logic.Operators.Not";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see language.Piece#update(language.ProgramContext)
	 */
	@Override
	public void updatePiece() {
		final ProgramValue<?> v1 = getInputs()[0];
		if (v1 instanceof ProgramValueBoolean) {
			final ProgramValueBoolean v3 = new ProgramValueBoolean(
					!((ProgramValueBoolean) v1).getValue());
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

	@Override
	public boolean shouldUpdateEveryTick() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Type getOutputType() {
		return Type.BOOLEAN;
	}

}
