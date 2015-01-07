package ide;

/**
 * This class is a wrapper class for the class <code>Class</code>. It changes the toString() method to use
 * Class.getSimpleName();
 * */
public class ClassDisplayer {
	public final Class c;

	public ClassDisplayer(final Class c) {
		this.c = c;
	}

	@Override
	public String toString() {
		return c.getSimpleName();
	}
}
