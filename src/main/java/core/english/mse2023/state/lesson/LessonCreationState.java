package core.english.mse2023.state.lesson;

public enum LessonCreationState {
    DATE_CHOOSING(0),
    TOPIC_CHOOSING(1),
    LINK_CHOOSING(2),
    CREATED(3);

    private final int index;

    LessonCreationState(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
