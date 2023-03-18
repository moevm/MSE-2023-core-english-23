package core.english.mse2023.handler.impl;

import core.english.mse2023.aop.annotation.handler.TextCommandHeader;
import core.english.mse2023.component.ReplyKeyboardMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.handler.Handler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

@Component
@TextCommandHeader
@RequiredArgsConstructor
public class StudyMenuHandler implements Handler {

    private static final String MESSAGE = "Вы перешли в раздел УЧЕБА";
    private final ReplyKeyboardMaker replyKeyboardMaker;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {

        return List.of(createMessage(update.getMessage().getChatId().toString(),
                MESSAGE, replyKeyboardMaker.getStudyKeyboard()));
    }

    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.STUDY;
    }
}
