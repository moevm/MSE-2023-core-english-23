package core.english.mse2023.exception;

public class LessonAlreadyFinishedException extends RuntimeException {

    public LessonAlreadyFinishedException(String message) {
        super(message);
    }
}
