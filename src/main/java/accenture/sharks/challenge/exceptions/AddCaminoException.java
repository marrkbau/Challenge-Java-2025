package accenture.sharks.challenge.exceptions;

public class AddCaminoException extends RuntimeException {

    public AddCaminoException(String message) {
        super(message);
    }

    public AddCaminoException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddCaminoException(Throwable cause) {
        super(cause);
    }
}
