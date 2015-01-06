package language.value;

public abstract class ProgramValue<T> {
	T value;

	public ProgramValue(final T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		if (value == null) {
			return "Nothing";
		}
		return value.toString();
	}
}
