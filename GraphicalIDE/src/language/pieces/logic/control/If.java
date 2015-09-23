package language.pieces.logic.control;

import java.awt.Point;

import language.Connection;
import language.Piece;
import language.ProgramContext;
import language.type.Type;
import language.value.ProgramValue;
import language.value.ProgramValueBoolean;
import language.value.ProgramValueNothing;


public class If extends Piece {


	public If(final int x, final int y) {
		super(2, 1, x, y);
	}

	public static String name() {
		return "Logic.Operators.Control.If";
	}

	@Override
	public void updatePiece(final ProgramContext context) {
		final ProgramValue<?> v1 = getInputs()[0];
		final ProgramValue<?> v2 = getInputs()[1];
		if (v1 instanceof ProgramValueBoolean && (((ProgramValueBoolean) v1).getValue()) == true) {
			for (final Connection c : getOutputs()) {
				c.changeInput(v2);
			}
		} else {
			for (final Connection c : getOutputs()) {
				c.changeInput(ProgramValueNothing.NOTHING);
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
