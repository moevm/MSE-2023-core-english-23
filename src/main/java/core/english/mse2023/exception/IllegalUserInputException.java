package core.english.mse2023.exception;

public class IllegalUserInputException extends RuntimeException {
    public IllegalUserInputException(String errorMessage) {
        super(errorMessage);
    }
}
