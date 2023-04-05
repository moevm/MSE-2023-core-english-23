package core.english.mse2023.handler;

import core.english.mse2023.exception.IllegalUserInputException;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.state.subcription.SubscriptionCreationState;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface InteractiveHandler extends Handler {
    /**
     * Continues handler's execution based on user input and current state
     *
     * @param update Data from user
     * @param role Role of user that triggered this handler
     * @return List of messages to be sent to the user
     * @throws IllegalUserInputException If user entered something wrong
     * @throws IllegalStateException     If method has been used in the wrong {@link SubscriptionCreationState}
     */
    List<BotApiMethod<?>> update(Update update, UserRole role) throws IllegalUserInputException, IllegalStateException;

    /**
     * Removes user's data from the handler
     * @param id User's telegram id
     */
    void removeFromCacheBy(String id);

    boolean hasFinished(String id);

    int getCurrentStateIndex(String id);
}
