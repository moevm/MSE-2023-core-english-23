package core.english.mse2023.state.assignRoleForGuest;

public enum AssignRoleForGuestState {
    GUEST_CHOOSING(0),
    ROLE_CHOOSING(1),
    FINISHED(2);

    private final int index;

    AssignRoleForGuestState(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
