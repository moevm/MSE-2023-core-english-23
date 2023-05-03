package core.english.mse2023.exception;

public class SubscriptionAlreadyCanceledException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Subscription %s has already been cancelled.";

    public SubscriptionAlreadyCanceledException(String subscriptionId) {
        super(String.format(MESSAGE_TEMPLATE, subscriptionId));
    }
}
