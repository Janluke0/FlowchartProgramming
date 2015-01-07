package language;

import language.value.ProgramValue;

// TODO: Auto-generated Javadoc
/**
 * The Class Connection.
 */
public class Connection {

	/** The output. */
	private Piece output;

	/** The output port. */
	private final int outputPort;

	/**
	 * Instantiates a new connection.
	 *
	 * @param output
	 *            the output
	 * @param outputPort
	 *            the output port
	 */
	public Connection(final Piece output, final int outputPort) {
		this.output = output;
		this.outputPort = outputPort;
	}

	/**
	 * Change input.
	 *
	 * @param value
	 *            the value
	 */
	public void changeInput(final ProgramValue value) {
		if (output != null) {
			output.changeInput(getOutputPort(), value);
		}
	}

	/**
	 * Gets the output.
	 *
	 * @return the output
	 */
	public Piece getOutput() {
		return output;
	}

	/**
	 * Sets the output.
	 *
	 * @param output
	 *            the new output
	 */
	public void setOutput(final Piece output) {
		this.output = output;
	}

	/**
	 * Gets the output port.
	 *
	 * @return the output port
	 */
	public int getOutputPort() {
		return outputPort;
	}

}
