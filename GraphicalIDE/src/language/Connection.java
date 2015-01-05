package language;

import language.value.ProgramValue;

public class Connection {
	private Piece input;

	private Piece output;

	private final int inputPort;

	private final int outputPort;

	public Connection(final Piece input, final int inputPort, final Piece output, final int outputPort) {
		this.input = input;
		this.inputPort = inputPort;
		this.output = output;
		this.outputPort = outputPort;
	}

	public void changeInput(final ProgramValue value) {
		if (output != null) {
			output.changeInput(getOutputPort(), value);
		}
	}

	public Piece getInput() {
		return input;
	}

	public void setInput(final Piece input) {
		this.input = input;
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
