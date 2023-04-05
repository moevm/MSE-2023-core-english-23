package core.english.mse2023.state.subcription;

public enum SubscriptionCreationState {

    START_DATE_CHOOSING(0),
    END_DATE_CHOOSING(1),
    LESSON_AMOUNT_ENTERING(2),
    STUDENT_CHOOSING(3),
    JUNCTION_NODE(4),
    TEACHER_CHOOSING(5),
    CREATED(6);

    private final int index;

    SubscriptionCreationState(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
