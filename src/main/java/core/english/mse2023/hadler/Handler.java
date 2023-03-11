package core.english.mse2023.hadler;

import core.english.mse2023.state.State;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface Handler {

    /**
     * Use this method as the first (and the last in case of handlers without user interaction) method to use
     * @param update Data from user
     * @return List of messages to be sent to the user
     */
    List<SendMessage> handle(Update update);

    /**
     * Returns command name to handle
     * @return Command name to handle
     */
    String getCommand();

    /**
     * Continues handler's execution based on user input and current state
     * @param update Data from user
     * @param state Current handler's working state
     * @return List of messages to be sent to the user
     *
     * @exception RuntimeException If a handler doesn't need user interaction
     */
    List<SendMessage> update(Update update, State state);

    /**
     * Returns if handler needs to interact with user
     * @return Does handler need to interact with user
     */
    boolean needsUserInteraction();

    /**
     * Returns initial state for this handler
     * @return Initial state for this handler
     *
     * @exception RuntimeException If a handler doesn't need user interaction
     */
    State getInitialState();

    /**
     * Removes user's data from the hadler
     * @param id User's telegram id
     *
     * @exception RuntimeException If a handler doesn't need user interaction
     */
    void cleanUp(String id);

    /**
     * Creates message object
     * @param update Data from user
     * @param msg Text to send by this message
     * @return Created message object
     */
    default SendMessage createMessage(String chatId, String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(msg);

        return message;
    }
}
