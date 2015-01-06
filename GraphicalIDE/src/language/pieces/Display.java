package language.pieces;

import ide.graphics.GraphicsConstants;

import java.awt.Graphics2D;
import java.awt.Point;

import language.Piece;
import language.ProgramContext;

public class Display extends Piece {

	public Display(final int x, final int y) {
		super(1, 0, x, y);
	}

	public static String name() {
		return "Display";
	}

	@Override
	public void draw(final Graphics2D g) {
		super.draw(g);
		g.translate(getX(), getY());

		g.setColor(GraphicsConstants.PIECE_TEXT);
		g.drawString(getInputs()[0].toString(), BORDER_SPACE * 2 + PORT_SIZE,
				3 * fontMetrics.getMaxAscent());

		g.translate(-getX(), -getY());
	}

	@Override
	public void update(final ProgramContext pc) {
	}

	@Override
	public void doubleClicked(final Point p) {
		// TODO Auto-generated method stub

	}

}
