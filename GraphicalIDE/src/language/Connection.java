package language;

import language.value.ProgramValue;

/**
 * The Class Connection.
 */
public class Connection {

	/** The piece to output to. */
	private Piece output;

	/** The port on the output to output to. */
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
	public void changeInput(final ProgramValue<?> value) {
		// if the value has changed
		if (output != null && output.getInputs()[getOutputPort()] != value) {
			output.setInput(getOutputPort(), value);
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
