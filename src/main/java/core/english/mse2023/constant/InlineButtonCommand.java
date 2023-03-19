package core.english.mse2023.constant;

/**
 * Commands for inline buttons. Be mindful to make commands small enough to fit in 64 bytes in the InlineButton.
 * @see core.english.mse2023.dto.InlineButtonDTO
 */
public interface InlineButtonCommand {
    String GET_MORE_SUBSCRIPTION_INFO = "/subscriptionGetMore";
    String GET_MORE_LESSON_INFO = "/lessonGetMore";


}
