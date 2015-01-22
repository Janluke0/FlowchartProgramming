package language;

/**
 * The Class ProgramContext.
 */
public class ProgramContext {

	public static final long START_TIME = System.currentTimeMillis();

	private ProgramContext() {
	}

	public static long getTime() {
		return System.currentTimeMillis() - START_TIME;
	}
}
