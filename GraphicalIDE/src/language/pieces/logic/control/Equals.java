package language.pieces.logic.control;

import java.awt.Point;

import language.Connection;
import language.Piece;
import language.ProgramContext;
import language.type.Type;
import language.value.ProgramValue;
import language.value.ProgramValueBoolean;


public class Equals extends Piece {


	public Equals(final int x, final int y) {
		super(2, 1, x, y);
	}

	public static String name() {
		return "Logic.Operators.Control.Equals";
	}

	@Override
	public void updatePiece(final ProgramContext context) {
		final ProgramValue<?> v1 = getInputs()[0];
		final ProgramValue<?> v2 = getInputs()[1];
		if (v1.equals(v2)) {
			for (final Connection c : getOutputs()) {
				c.changeInput(ProgramValueBoolean.TRUE);
			}
		} else {
			for (final Connection c : getOutputs()) {
				c.changeInput(ProgramValueBoolean.FALSE);
			}
		}

	}

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
		return getInputs()[1].getType();
	}

}
