package core.english.mse2023.handler;

import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.util.builder.InlineKeyboardBuilder;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public interface Handler {

    /**
     * Use this method as the first (and the last in case of handlers without user interaction) method to use
     *
     * @param update   Data from user
     * @param userRole Role of user that triggered this handler
     * @return List of messages to be sent to the user
     */
    List<BotApiMethod<?>> handle(Update update, UserRole userRole);

    /**
     * Returns handler's command
     * @return Handler's command
     */
    BotCommand getCommandObject();


}
