package language.pieces;

import language.Connection;
import language.Piece;
import language.ProgramContext;
import language.value.ProgramValue;
import language.value.ProgramValueInt;

public class Add extends Piece {

	public Add(final int x, final int y) {
		super(2, 1, x, y);
	}

	public static String name() {
		return "Add";
	}

	@Override
	public void update(final ProgramContext pc) {
		final ProgramValue v1 = getInputs()[0];
		final ProgramValue v2 = getInputs()[1];
		if (v1 instanceof ProgramValueInt && v2 instanceof ProgramValueInt) {
			final ProgramValueInt v3 = new ProgramValueInt(((ProgramValueInt) v1).getValue() + ((ProgramValueInt) v2).getValue());
			for (final Connection c : getOutputs()) {
				c.changeInput(v3);
			}
		}

	}

}
