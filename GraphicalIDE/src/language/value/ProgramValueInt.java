package language.value;

public class ProgramValueInt extends ProgramValue {

	private final int value;

	public ProgramValueInt(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
