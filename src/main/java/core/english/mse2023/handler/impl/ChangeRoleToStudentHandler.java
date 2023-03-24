package core.english.mse2023.handler.impl;

import core.english.mse2023.aop.annotation.handler.AdminRole;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.component.MessageTextMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

@Component
@AdminRole
@TextCommandType
@RequiredArgsConstructor
public class ChangeRoleToStudentHandler implements Handler {

    private final MessageTextMaker messageTextMaker;

    private final UserService service;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {

        boolean result = service.changeUserRole(update, UserRole.STUDENT);

        SendMessage message;

        if (!result) {
            message = SendMessage.builder()
                    .chatId(update.getMessage().getChatId().toString())
                    .text(messageTextMaker.failedToChangeUserRoleMessageText())
                    .build();
        } else {
            message = SendMessage.builder()
                    .chatId(update.getMessage().getChatId().toString())
                    .text(messageTextMaker.userRoleSuccessfullyChangedMessageText(UserRole.STUDENT))
                    .build();
        }

        return List.of(message);
    }

    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.CHANGE_ROLE_TO_STUDENT;
    }
}