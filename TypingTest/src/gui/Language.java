package gui;

public enum Language {
	ENG, ESP;

	public String toString() {
		switch (this) {
		case ENG:
			return "English";
		case ESP:
			return "Spanish";
		default:
			throw new IllegalArgumentException();
		}
	}
}
