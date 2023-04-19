package core.english.mse2023.state.setCommentForTeacher;

public enum SetCommentForTeacherState {
    WAITING_FOR_COMMENT(0),
    FINISHED(1);

    private final int index;

    SetCommentForTeacherState(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
