package core.english.mse2023.cache;

public class IllegalUserInputException extends RuntimeException {
    public IllegalUserInputException(String errorMessage) {
        super(errorMessage);
    }
}
