package core.english.mse2023.handler.impl;

import core.english.mse2023.component.ReplyKeyboardMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.handler.Handler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

@Component
@AllArgsConstructor
public class AssignRoleHandler implements Handler {

    private static final String MESSAGE = "Вы перешли в раздел НАЗНАЧИТЬ РОЛЬ";
    private final ReplyKeyboardMaker replyKeyboardMaker;

    @Override
    public List<SendMessage> handle(Update update) {

        return List.of(createMessage(update.getMessage().getChatId().toString(),
                MESSAGE, replyKeyboardMaker.getAssignRoleKeyboard()));
    }

    @Override
    public BotCommand getCommand() {
        return ButtonCommand.ASSIGN_ROLE;
    }
}

