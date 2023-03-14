package core.english.mse2023.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

public interface Handler {

    /**
     * Use this method as the first (and the last in case of handlers without user interaction) method to use
     * @param update Data from user
     * @return List of messages to be sent to the user
     */
    List<SendMessage> handle(Update update);

    /**
     * Returns handler's command
     * @return Handler's command
     */
    BotCommand getCommand();

    /**
     * Creates SendMessage object
     * @param chatId Telegram chat id to put message to
     * @param messageText Text to send by this message
     * @return Created SendMessage object
     */
    default SendMessage createMessage(String chatId, String messageText, ReplyKeyboardMarkup replyKeyboardMarkup) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .replyMarkup(replyKeyboardMarkup)
                .build();
    }
}
