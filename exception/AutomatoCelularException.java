package ufu.mestrado.exception;

public class AutomatoCelularException extends Exception {
	private static final long serialVersionUID = 1L;

	public AutomatoCelularException() {
	}

	public AutomatoCelularException(String message) {
		super(message);
	}

	public AutomatoCelularException(Throwable cause) {
		super(cause);
	}

	public AutomatoCelularException(String message, Throwable cause) {
		super(message, cause);
	}

}
