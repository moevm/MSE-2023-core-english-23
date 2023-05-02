package core.english.mse2023.exception;

public class LessonDateOutsideSubscriptionException extends RuntimeException {
    public LessonDateOutsideSubscriptionException(String message) {
        super(message);
    }
}
