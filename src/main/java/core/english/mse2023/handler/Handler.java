package core.english.mse2023.handler;

import core.english.mse2023.constant.Command;
import core.english.mse2023.model.dictionary.UserRole;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface Handler {

    /**
     * Use this method as the first (and the last in case of handlers without user interaction) method to use
     *
     * @param update   Data from user
     * @param userRole Role of user that triggered this handler
     * @return List of messages to be sent to the user
     */
    List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole);

    /**
     * Returns handler's command
     * @return Handler's command
     */
    Command getCommandObject();


}
