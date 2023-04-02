package core.english.mse2023.handler;

import core.english.mse2023.exception.IllegalUserInputException;
import core.english.mse2023.state.State;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface InteractiveHandler extends Handler {
    /**
     * Continues handler's execution based on user input and current state
     *
     * @param update Data from user
     * @param state  Current handler's working state
     * @return List of messages to be sent to the user
     * @throws IllegalUserInputException If user entered something wrong
     * @throws IllegalStateException     If method has been used in the wrong {@link State}
     */
    List<BotApiMethod<?>> update(Update update, State state) throws IllegalUserInputException, IllegalStateException;

    /**
     * Returns initial state for this handler
     * @return Initial state for this handler
     */
    State getInitialState();

    /**
     * Removes user's data from the handler
     * @param id User's telegram id
     */
    void removeFromCacheBy(String id);
}
