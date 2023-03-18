package core.english.mse2023.constant;

/**
 * Commands for inline buttons. Be mindful to make commands small enough to fit in 64 bytes in the InlineButton.
 * @see core.english.mse2023.dto.InlineButtonDTO
 */
public interface InlineButtonCommand {
    String SUBSCRIPTION_GET_MORE_DATA = "/subscriptionGetMore";
    String LESSON_GET_MORE_DATA = "/lessonGetMore";


}
