package core.english.mse2023.state.setParentForStudent;

public enum SetParentForStudentState {
    STUDENT_CHOOSING(0),
    PARENT_CHOOSING(1),
    FINISHED(2);

    private final int index;

    SetParentForStudentState(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
