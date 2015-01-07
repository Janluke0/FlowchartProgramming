package ide;

import language.Program;

// TODO: Auto-generated Javadoc
/**
 * The Class Display.
 */
public class Display {

	/** The frame. */
	private final WindowFrame frame;
	
	/** The program. */
	private Program program;

	/**
	 * Instantiates a new display.
	 *
	 * @param frame the frame
	 */
	public Display(final WindowFrame frame) {
		this.frame = frame;
		setProgram(new Program());
	}

	/**
	 * Sets the program.
	 *
	 * @param program the new program
	 */
	public void setProgram(final Program program) {
		this.program = program;
	}

	/**
	 * Gets the program.
	 *
	 * @return the program
	 */
	public Program getProgram() {
		return program;
	}

	/**
	 * Gets the frame.
	 *
	 * @return the frame
	 */
	public WindowFrame getFrame() {
		return frame;
	}

}
