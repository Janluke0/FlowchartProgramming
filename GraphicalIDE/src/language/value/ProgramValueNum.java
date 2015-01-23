package language.value;

import java.math.BigDecimal;

import language.type.Type;

// TODO: Auto-generated Javadoc
/**
 * The Class ProgramValueNum.
 */
public class ProgramValueNum extends ProgramValue<BigDecimal> {

	/**
	 * Instantiates a new program value number.
	 *
	 * @param value
	 *            the value
	 */
	public ProgramValueNum(final BigDecimal value) {
		super(value);
	}

	@Override
	public Type getType() {
		return Type.NUMBER;
	}
}
