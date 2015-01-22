package language.pieces.utils;

import java.awt.Point;

import language.Piece;
import language.value.ProgramValueBoolean;

/**
 * The Class NumberConstant.
 */
public class Print extends Piece {

	private static final int INPUT_SHOULD_PRINT = 0;
	private static final int INPUT_TO_PRINT = 1;

	private static final int OUTPUT_PRINTED = 0;

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
	public Print(final int x, final int y) {
		super(2, 1, x, y);
	}

	/**
	 * returns the piece name.
	 *
	 * @return the string
	 */
	public static String name() {
		return "Utils.Print";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see language.Piece#update(language.ProgramContext)
	 */
	@Override
	public void updatePiece() {
		if (getInputs()[INPUT_SHOULD_PRINT] instanceof ProgramValueBoolean
				&& ((ProgramValueBoolean) getInputs()[INPUT_SHOULD_PRINT])
						.getValue() == true) {
			System.out.println(getInputs()[INPUT_TO_PRINT]);
			getOutputs()[OUTPUT_PRINTED].changeInput(ProgramValueBoolean.TRUE);
		} else {
			getOutputs()[OUTPUT_PRINTED].changeInput(ProgramValueBoolean.FALSE);
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
}
