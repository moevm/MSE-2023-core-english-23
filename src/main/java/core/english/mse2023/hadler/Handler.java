package core.english.mse2023.hadler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface Handler {
    List<SendMessage> handle(Update update);

    String getCommand();

    default SendMessage createMessage(Update update, String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        message.setText(msg);

        return message;
    }
}
