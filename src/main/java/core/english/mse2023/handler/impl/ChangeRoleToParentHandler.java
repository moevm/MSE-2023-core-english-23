package core.english.mse2023.handler.impl;

import core.english.mse2023.aop.annotation.handler.AdminHandler;
import core.english.mse2023.aop.annotation.handler.TextCommandHeader;
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
@AdminHandler
@TextCommandHeader
@RequiredArgsConstructor
public class ChangeRoleToParentHandler implements Handler {

    private static final String SUCCESS_TEXT = "Ваша роль изменена на: %s";
    private static final String FAIL_TEXT = "Невозможно сменить роль на такую же, как у вас.";

    private final UserService service;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {

        boolean result = service.changeUserRole(update, UserRole.PARENT);

        SendMessage message;

        if (!result) {
            message = createMessage(update.getMessage().getChatId().toString(), FAIL_TEXT);
        } else {
            message = createMessage(update.getMessage().getChatId().toString(), String.format(SUCCESS_TEXT, UserRole.PARENT));
        }

        return List.of(message);
    }

    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.CHANGE_ROLE_TO_PARENT;
    }
}