package ide;

import language.Program;

public class Display {

	private final WindowFrame frame;
	private Program program;

	public Display(final WindowFrame frame) {
		this.frame = frame;
		setProgram(new Program());
	}

	public void setProgram(final Program program) {
		this.program = program;
	}

	public Program getProgram() {
		return program;
	}

	public WindowFrame getFrame() {
		return frame;
	}

}
