package core.english.mse2023.exception;

public class UserDoesNotExistsException extends RuntimeException {

    public UserDoesNotExistsException(String message) {
        super(message);
    }

}