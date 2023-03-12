package core.english.mse2023.hadler.interfaces;

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
     * Creates message object
     * @param chatId Telegram chat id to put message to
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
