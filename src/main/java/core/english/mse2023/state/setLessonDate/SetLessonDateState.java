package core.english.mse2023.state.setLessonDate;

public enum SetLessonDateState {
    WAITING_FOR_DATE(0),
    FINISHED(1);

    private final int index;

    SetLessonDateState(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
