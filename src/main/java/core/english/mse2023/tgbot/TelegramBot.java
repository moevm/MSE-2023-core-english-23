package core.english.mse2023.tgbot;

import core.english.mse2023.config.BotConfig;
import core.english.mse2023.gateway.Gateway;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPhoto;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.stickers.AddStickerToSet;
import org.telegram.telegrambots.meta.api.methods.stickers.CreateNewStickerSet;
import org.telegram.telegrambots.meta.api.methods.stickers.SetStickerSetThumb;
import org.telegram.telegrambots.meta.api.methods.stickers.UploadStickerFile;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;

    private final Gateway gateway;

    public TelegramBot(BotConfig config, Gateway gateway) {
        this.config = config;
        this.gateway = gateway;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        System.out.println(update.getMessage().getChatId().toString());
        executeBotApiMethods(gateway.processUpdate(update));
    }

    /**
     * Sends all the messages from the list
     *
     * @param methods - list of bot api methods to execute
     */
    public void executeBotApiMethods(List<PartialBotApiMethod<?>> methods) {
        if (methods == null) return;
        for (PartialBotApiMethod<?> method : methods) {
            try {
                execute(method);
                log.info("Reply sent");
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }

    public void execute(PartialBotApiMethod<?> method) throws TelegramApiException {

        if (method instanceof SendDocument sendDocument) {
            execute(sendDocument);
        } else if (method instanceof SendPhoto sendPhoto) {
            execute(sendPhoto);
        } else if (method instanceof SendVideo sendVideo) {
            execute(sendVideo);
        } else if (method instanceof SendVideoNote sendVideoNote) {
            execute(sendVideoNote);
        } else if (method instanceof SendSticker sendSticker) {
            execute(sendSticker);
        } else if (method instanceof SendAudio sendAudio) {
            execute(sendAudio);
        } else if (method instanceof SendVoice sendVoice) {
            execute(sendVoice);
        } else if (method instanceof SendMediaGroup sendMediaGroup) {
            execute(sendMediaGroup);
        } else if (method instanceof SetChatPhoto setChatPhoto) {
            execute(setChatPhoto);
        } else if (method instanceof AddStickerToSet addStickerToSet) {
            execute(addStickerToSet);
        } else if (method instanceof SetStickerSetThumb setStickerSetThumb) {
            execute(setStickerSetThumb);
        } else if (method instanceof CreateNewStickerSet createNewStickerSet) {
            execute(createNewStickerSet);
        } else if (method instanceof UploadStickerFile uploadStickerFile) {
            execute(uploadStickerFile);
        } else if (method instanceof EditMessageMedia editMessageMedia) {
            execute(editMessageMedia);
        } else if (method instanceof SendAnimation sendAnimation) {
            execute(sendAnimation);
        } else if (method instanceof BotApiMethod<?> botApiMethod) {
            execute(botApiMethod);
        } else {
            throw new TelegramApiException("Unexpected PartialBotApiMethod tried to execute. Method: " + method.getClass().getName());
        }

    }
}
