package core.english.mse2023.model.dictionary;

public enum LessonStatus {
    NOT_STARTED_YET("Еще не начат"),
    IN_PROGRESS("Проводится"),
    ENDED("Завершен"),
    CANCELLED_BY_TEACHER("Отменен преподавателем"),
    CANCELLED_BY_STUDENT("Отменен студентом"),
    CANCELLED("Отменен");

    private final String string;

    LessonStatus(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
