package core.english.mse2023.cache;

import core.english.mse2023.exception.IllegalUserInputException;
import core.english.mse2023.exception.UnexpectedUpdateType;
import core.english.mse2023.handler.InteractiveHandler;
import core.english.mse2023.model.dictionary.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
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
    private boolean hasFinished;

    @Getter
    @Setter
    private int currentStateIndex;

    public CacheData(InteractiveHandler handler) {
        this.handler = handler;
        this.hasFinished = false;
        this.currentStateIndex = 0;
    }

    public List<PartialBotApiMethod<?>> updateData(Update update, UserRole role) {
        List<PartialBotApiMethod<?>> sendMessageList;
        try {
            String userId = null;

            if (update.hasMessage()) {
                userId = update.getMessage().getFrom().getId().toString();
            } else if (update.hasCallbackQuery()) {
                userId = update.getCallbackQuery().getFrom().getId().toString();
            }

            if (userId == null) {
                throw new UnexpectedUpdateType("Cannot get user id since update doesn't have message or callback query.");
            }

            sendMessageList = handler.update(update, role);

            if (!hasFinished) {
                if (handler.hasFinished(userId)) {
                    handler.removeFromCacheBy(userId);
                    hasFinished = true;
                } else {
                    currentStateIndex = handler.getCurrentStateIndex(userId);
                }
            }

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
            message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        }
    }

}
