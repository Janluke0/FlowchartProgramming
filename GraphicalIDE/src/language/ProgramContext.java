package language;

import java.io.UnsupportedEncodingException;

import ide.mainpanel.TextAreaOutputStream;

/**
 * The Class ProgramContext.
 */
public class ProgramContext {

	private long startTime = System.currentTimeMillis();
	private final TextAreaOutputStream output;

	public ProgramContext(TextAreaOutputStream output) {
		this.output = output;
	}

	public long getTime() {
		return System.currentTimeMillis() - startTime;
	}

	public void reset() {
		startTime = System.currentTimeMillis();
		output.clear();
	}

	public void print(String s) {
		try {
			output.write(s.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void println(String s) {
		try {
			output.write((s + '\n').getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@FunctionalInterface
	public static interface ResetProcedure {
		public void reset();
	}

}
