package core.english.mse2023.exception;

public class NoSuchCommandException extends RuntimeException {

    private final static String DEFAULT_MESSAGE = "Unknown command from user.";

    public NoSuchCommandException() {
        super(DEFAULT_MESSAGE);
    }

    public NoSuchCommandException(String message) {
        super(message);
    }
}
