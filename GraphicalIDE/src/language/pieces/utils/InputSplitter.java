package language.pieces.utils;

import java.awt.Graphics2D;
import java.awt.Point;

import language.Piece;
import language.ProgramContext;
import language.type.Type;

/**
 * The Splitter class. Takes an input and copies it to all outputs.
 */
public class InputSplitter extends Piece {

	private static final int OUTPUTS = 4;

	/**
	 * Instantiates a new display.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public InputSplitter(final int x, final int y) {
		super(1, OUTPUTS, x, y);
	}

	/**
	 * Name.
	 *
	 * @return the string
	 */
	public static String name() {
		return "Utils.Input Splitter";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see language.Piece#draw(java.awt.Graphics2D)
	 */
	@Override
	public void draw(final Graphics2D g) {
		super.draw(g);
		setInputText(0, String.valueOf(getInputs()[0]));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see language.Piece#update(language.ProgramContext)
	 */
	@Override
	public void updatePiece(final ProgramContext context) {
		for (int i = 0; i < OUTPUTS; i++) {
			getOutputs()[i].changeInput(getInputs()[0]);
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
		return false;
	}

	@Override
	public Type getOutputType() {
		return getInputs()[0].getType();
	}

}
