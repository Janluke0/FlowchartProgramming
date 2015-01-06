package language;

import language.value.ProgramValue;

public class Connection {
	private Piece output;

	private final int outputPort;

	public Connection(final Piece output, final int outputPort) {
		this.output = output;
		this.outputPort = outputPort;
	}

	public void changeInput(final ProgramValue value) {
		if (output != null) {
			output.changeInput(getOutputPort(), value);
		}
	}

	public Piece getOutput() {
		return output;
	}

	public void setOutput(final Piece output) {
		this.output = output;
	}

	public int getOutputPort() {
		return outputPort;
	}

}
