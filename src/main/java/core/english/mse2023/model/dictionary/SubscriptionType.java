package core.english.mse2023.model.dictionary;

public enum SubscriptionType {
    QUANTITY_BASED("По количеству уроков"),
    TIME_BASED("По продолжительности");

    private final String string;

    SubscriptionType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
