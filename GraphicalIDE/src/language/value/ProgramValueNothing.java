package language.value;

import language.type.Type;

// TODO: Auto-generated Javadoc
/**
 * The Class ProgramValueNothing.
 */
public final class ProgramValueNothing extends ProgramValue<Object> {

	public static final ProgramValueNothing NOTHING = new ProgramValueNothing();

	/**
	 * Instantiates a new program value nothing.
	 */
	private ProgramValueNothing() {
		super(null);
	}

	@Override
	public Type getType() {
		return Type.NONE;
	}
}
