package core.english.mse2023.exception;

public class ButtonCallbackDataLimitExceedException extends RuntimeException {

    private static final String MESSAGE = "Data package is too big. " +
            "Consider using smaller command name or/and data strings. " +
            "Current package size: %s";

    public ButtonCallbackDataLimitExceedException(int size) {
        super(String.format(MESSAGE, size));
    }

}
