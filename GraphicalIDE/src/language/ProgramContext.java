package language;

/**
 * The Class ProgramContext.
 */
public class ProgramContext {

	private long startTime = System.currentTimeMillis();

	public ProgramContext() {
	}

	public long getTime() {
		return System.currentTimeMillis() - startTime;
	}

	public void reset() {
		startTime = System.currentTimeMillis();
	}
}
