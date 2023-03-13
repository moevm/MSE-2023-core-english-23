package core.english.mse2023.cache;

import core.english.mse2023.exception.IllegalUserInputException;
import core.english.mse2023.handler.InteractiveHandler;
import core.english.mse2023.state.State;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * POJO for storing data in cache
 */
public class CacheData {

    @Getter
    private final InteractiveHandler handler;

    @Getter
    @Setter
    private State state;

    public CacheData(InteractiveHandler handler) {
        this.handler = handler;
        state = handler.getInitialState();
    }

    public List<SendMessage> updateData(Update update) {
        List<SendMessage> sendMessageList;
        try {
            sendMessageList = handler.update(update, state);

            state.next(this);

        } catch (IllegalUserInputException exception) {
            SendMessage message = new SendMessage();
            setChatId(message, update);

            message.setText("Вы ввели что-то не то. Попробуйте снова.");

            sendMessageList = List.of(message);
        } catch (IllegalStateException exception) {
            SendMessage message = new SendMessage();
            setChatId(message, update);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS");

            message.setText("Произошла внутренняя ошибка. " +
                    "Свяжитесь со службой технической поддержки и сообщите им время ошибки: " +
                    dateFormat.format(new Date()));

            sendMessageList = List.of(message);
        }

        return sendMessageList;
    }

    private void setChatId(SendMessage message, Update update) {
        if (update.hasMessage()) {
            message.setChatId(String.valueOf(update.getMessage().getChatId()));
        } else if (update.hasCallbackQuery()) {
            message.setChatId(String.valueOf(update.getMessage().getChatId()));
        }
    }

}
