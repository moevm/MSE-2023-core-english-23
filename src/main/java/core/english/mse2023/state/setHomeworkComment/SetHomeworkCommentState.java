package core.english.mse2023.state.setHomeworkComment;

public enum SetHomeworkCommentState {
    WAITING_FOR_HOMEWORK(0),
    FINISHED(1);

    private final int index;

    SetHomeworkCommentState(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
