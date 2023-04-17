package core.english.mse2023.exception;

public class LessonHasNotStartedYetException extends RuntimeException {
    public LessonHasNotStartedYetException(String message) {
        super(message);
    }
}
