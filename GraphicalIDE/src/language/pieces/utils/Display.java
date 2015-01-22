package language.pieces.utils;

import java.awt.Graphics2D;
import java.awt.Point;

import language.Piece;
import language.type.Type;

// TODO: Auto-generated Javadoc
/**
 * The Class Display.
 */
public class Display extends Piece {

	/**
	 * Instantiates a new display.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public Display(final int x, final int y) {
		super(1, 0, x, y);
	}

	/**
	 * Name.
	 *
	 * @return the string
	 */
	public static String name() {
		return "Utils.Display";
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
	public void updatePiece() {
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
	protected Type getOutputType() {
		return Type.BOOLEAN;
	}

}
