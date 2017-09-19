package language.pieces.utils;

import java.awt.Point;

import language.Piece;
import language.ProgramContext;
import language.type.Type;
import language.value.ProgramValueBoolean;

/**
 * The Class NumberConstant.
 */
public class Print extends Piece {

	private static final int INPUT_SHOULD_PRINT = 0;
	private static final int INPUT_TO_PRINT = 1;

	// the last input command value. Makes sure that this doesn't fire
	// continuously if the command doesn't change.
	private boolean lastValue = false;

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
		super(2, 0, x, y);
		setInputText(0, "Command");
		setInputText(1, "Value");
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
	public void updatePiece(final ProgramContext context) {
		if (getInputs()[INPUT_SHOULD_PRINT] instanceof ProgramValueBoolean
				&& ((ProgramValueBoolean) getInputs()[INPUT_SHOULD_PRINT]).getValue() == true) {
			if (!lastValue) {
				context.println(getInputs()[INPUT_TO_PRINT].toString());
				lastValue = true;
			}
		} else {
			lastValue = false;
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
		return Type.NONE;
	}
}
