package core.english.mse2023.handler;

import core.english.mse2023.constant.InlineButtonCommand;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

public interface InlineButtonHandler {

    /**
     *
     * @param update Data from user
     * @return List of messages to be sent to the user
     */
    List<BotApiMethod<?>> handle(Update update);

    /**
     * Returns handler's command
     * @return Handler's command
     */
    String getCommand();


    /**
     * Creates SendMessage object
     * @param chatId Telegram chat id to put message to
     * @param messageText Text to send by this message
     * @return Created SendMessage object
     */
    default SendMessage createMessage(String chatId, String messageText) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .build();
    }


    default EditMessageReplyMarkup editMessageReplyMarkup(String chatId, int messageId, InlineKeyboardMarkup newInlineKeyboardMarkup) {
        return EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(newInlineKeyboardMarkup)
                .build();
    }
}
