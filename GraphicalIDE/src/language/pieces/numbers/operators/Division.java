package language.pieces.numbers.operators;

import java.awt.Point;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import language.Connection;
import language.Piece;
import language.ProgramContext;
import language.type.Type;
import language.value.ProgramValue;
import language.value.ProgramValueNothing;
import language.value.ProgramValueNum;

/**
 * The Class Add.
 */
public class Division extends Piece {

	private static final MathContext MC = new MathContext(50, RoundingMode.HALF_UP);

	/**
	 * Instantiates a new adds the.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public Division(final int x, final int y) {
		super(2, 1, x, y);
	}

	/**
	 * Name.
	 *
	 * @return the string
	 */
	public static String name() {
		return "Numbers.Operators.Division";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see language.Piece#update(language.ProgramContext)
	 */
	@Override
	public void updatePiece(final ProgramContext context) {
		final ProgramValue<?> v1 = getInputs()[0];
		final ProgramValue<?> v2 = getInputs()[1];
		if (v1 instanceof ProgramValueNum && v2 instanceof ProgramValueNum) {
			final ProgramValue<?> v3;

			if (((ProgramValueNum) v2).getValue().compareTo(BigDecimal.ZERO) == 0) {
				v3 = ProgramValueNothing.NOTHING;
			} else {

				v3 = new ProgramValueNum(((ProgramValueNum) v1).getValue().divide(((ProgramValueNum) v2).getValue(), MC));
			}
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

	}

	@Override
	public boolean shouldUpdateEveryTick() {
		return false;
	}

	@Override
	public Type getOutputType() {
		return Type.NUMBER;
	}

}
