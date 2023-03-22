package core.english.mse2023.util.utilities;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@UtilityClass
public class TelegramMessageUtils {

    /**
     * Creates SendMessage object
     * @param chatId Telegram chat id to put message to
     * @param messageText Text to send by this message
     * @param replyKeyboardMarkup Keyboard to send with this message
     * @return Created SendMessage object
     */
    public SendMessage createMessage(String chatId, String messageText, ReplyKeyboardMarkup replyKeyboardMarkup) {
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
    public SendMessage createMessage(String chatId, String messageText) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .build();
    }

    public EditMessageReplyMarkup editMessageReplyMarkup(String chatId, int messageId, InlineKeyboardMarkup newInlineKeyboardMarkup) {
        return EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(newInlineKeyboardMarkup)
                .build();
    }

    public EditMessageText editMessageText(String chatId, int messageId, String newMessageText) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(newMessageText)
                .build();
    }

    public EditMessageText editMessageTextWithReplyMarkup(String chatId, int messageId, String newMessageText, InlineKeyboardMarkup newInlineKeyboardMarkup) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(newMessageText)
                .replyMarkup(newInlineKeyboardMarkup)
                .build();
    }

}
