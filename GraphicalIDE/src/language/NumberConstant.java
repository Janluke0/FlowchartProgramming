package language;

import language.value.ProgramValueInt;

public class NumberConstant extends Piece {

	private ProgramValueInt value;

	public NumberConstant(final int value) {
		super(0, 1);
		this.value = new ProgramValueInt(value);
	}

	public static String name() {
		return "Number Constant";
	}

	@Override
	public void update(final ProgramContext pc) {
		for (final Connection c : getOutputs()) {
			c.changeInput(value);
		}
	}

	public void setValue(final int value) {
		this.value = new ProgramValueInt(value);
	}

}
