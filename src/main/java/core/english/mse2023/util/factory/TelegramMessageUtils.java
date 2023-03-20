package core.english.mse2023.util.factory;

import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class TelegramMessageUtils {

    /**
     * Creates SendMessage object
     * @param chatId Telegram chat id to put message to
     * @param messageText Text to send by this message
     * @param replyKeyboardMarkup Keyboard to send with this message
     * @return Created SendMessage object
     */
    public static SendMessage createMessage(String chatId, String messageText, ReplyKeyboardMarkup replyKeyboardMarkup) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .replyMarkup(replyKeyboardMarkup)
                .build();
    }
    /**
     * Creates SendMessage object
     * @param chatId Telegram chat id to put message to
     * @param messageText Text to send by this message
     * @return Created SendMessage object
     */
    public static SendMessage createMessage(String chatId, String messageText) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .build();
    }

    public static EditMessageReplyMarkup editMessageReplyMarkup(String chatId, int messageId, InlineKeyboardMarkup newInlineKeyboardMarkup) {
        return EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(newInlineKeyboardMarkup)
                .build();
    }

}
