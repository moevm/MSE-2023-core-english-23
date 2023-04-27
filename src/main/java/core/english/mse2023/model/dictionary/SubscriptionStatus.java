package core.english.mse2023.model.dictionary;

public enum SubscriptionStatus {
    NOT_YET_STARTED("Еще не начата"),
    ACTIVE("Активна"),
    FINISHED("Завершена"),
    CANCELLED("Отменена");

    private final String string;

    SubscriptionStatus(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
