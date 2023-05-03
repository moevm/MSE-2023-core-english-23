package core.english.mse2023.exception;

public class LessonDateOutsideSubscriptionException extends RuntimeException {

    private static final String MESSAGE = "Lesson date cannot be outside subscription boundaries.";

    public LessonDateOutsideSubscriptionException() {
        super(MESSAGE);
    }
}
