package language.pieces.utils;

import java.awt.Point;

import language.Connection;
import language.Piece;
import language.ProgramContext;
import language.type.Type;
import language.value.ProgramValue;
import language.value.ProgramValueBoolean;
import language.value.ProgramValueNothing;

public class VarStore extends Piece {

	private ProgramValue<?> var = ProgramValueNothing.NOTHING;

	public VarStore(final int x, final int y) {
		super(2, 1, x, y);
	}

	public static String name() {
		return "Utils.Variable Store";
	}

	@Override
	public void updatePiece(final ProgramContext context) {
		final ProgramValue<?> v1 = getInputs()[0];
		final ProgramValue<?> v2 = getInputs()[1];
		if (v1 instanceof ProgramValueBoolean && (((ProgramValueBoolean) v1).getValue()) == true) {
			var = v2;
		}
		for (final Connection c : getOutputs()) {
			c.changeInput(var);
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
		return var.getType();
	}

}
