package language;

/**
 * The Class ProgramContext.
 */
public class ProgramContext {

	public static final long START_TIME = System.currentTimeMillis();
	public final long TIME;

	public ProgramContext() {
		TIME = System.currentTimeMillis() - START_TIME;
	}
}
