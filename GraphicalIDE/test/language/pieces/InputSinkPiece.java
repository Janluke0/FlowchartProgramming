package language.pieces;

import java.awt.Point;

import language.Piece;
import language.type.Type;

/**
 * A piece that makes testing easier because it can be used as a result piece.
 * 
 * @author s-KADAMS
 *
 */
public class InputSinkPiece extends Piece {

	public InputSinkPiece() {
		super(1000, 0, 0, 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void updatePiece() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean shouldUpdateEveryTick() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void doubleClicked(final Point clickPoint) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Type getOutputType() {
		// TODO Auto-generated method stub
		return null;
	}

}
