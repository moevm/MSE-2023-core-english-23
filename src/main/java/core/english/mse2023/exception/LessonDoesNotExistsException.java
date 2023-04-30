package core.english.mse2023.exception;

public class LessonDoesNotExistsException extends RuntimeException {
    public LessonDoesNotExistsException(String message) {
        super(message);
    }
}
