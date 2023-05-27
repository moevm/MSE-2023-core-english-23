package core.english.mse2023.state.getStudentStatistic;

public enum GetStudentStatisticState {
    STUDENT_CHOOSING(0),
    ENTERING_INTERVAL(1),
    CREATED(2);

    private final int index;

    GetStudentStatisticState(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
