package core.english.mse2023.cache;

import core.english.mse2023.constant.Command;
import core.english.mse2023.hadler.Handler;
import core.english.mse2023.state.State;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * POJO for storing data in cache
 */
public class CacheData {

    @Getter
    private final Handler handler;

    @Getter
    @Setter
    private State state;

    public CacheData(Handler handler) {
        this.handler = handler;
        state = handler.getInitialState();
    }

    public List<SendMessage> updateData(Update update) {
        List<SendMessage> sendMessageList;
        try {
            sendMessageList = handler.update(update, state);

            state.next(this);

        } catch (IllegalArgumentException exception) {
            SendMessage message = new SendMessage();
            if (update.hasMessage()) {
                message.setChatId(String.valueOf(update.getMessage().getChatId()));
            } else if (update.hasCallbackQuery()) {
                message.setChatId(String.valueOf(update.getMessage().getChatId()));
            }
            message.setText("Вы ввели что-то не то. Попробуйте снова.");

            sendMessageList = List.of(message);
        }

        return sendMessageList;
    }

}
