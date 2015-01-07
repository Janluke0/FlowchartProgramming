package language.pieces;

import ide.graphics.GraphicsConstants;

import java.awt.Graphics2D;
import java.awt.Point;

import language.Piece;
import language.ProgramContext;

// TODO: Auto-generated Javadoc
/**
 * The Class Display.
 */
public class Display extends Piece {

	/**
	 * Instantiates a new display.
	 *
	 * @param x the x
	 * @param y the y
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
		return "Display";
	}

	/* (non-Javadoc)
	 * @see language.Piece#draw(java.awt.Graphics2D)
	 */
	@Override
	public void draw(final Graphics2D g) {
		super.draw(g);
		g.translate(getX(), getY());

		g.setColor(GraphicsConstants.PIECE_TEXT);
		g.drawString(getInputs()[0].toString(), BORDER_SPACE * 2 + PORT_SIZE,
				3 * fontMetrics.getMaxAscent());

		g.translate(-getX(), -getY());
	}

	/* (non-Javadoc)
	 * @see language.Piece#update(language.ProgramContext)
	 */
	@Override
	public void update(final ProgramContext pc) {
	}

	/* (non-Javadoc)
	 * @see language.Piece#doubleClicked(java.awt.Point)
	 */
	@Override
	public void doubleClicked(final Point p) {
		// TODO Auto-generated method stub

	}

}
