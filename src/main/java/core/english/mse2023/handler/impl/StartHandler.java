package core.english.mse2023.handler.impl;

import core.english.mse2023.aop.annotation.handler.TextCommandHandler;
import core.english.mse2023.component.ReplyKeyboardMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.User;
import core.english.mse2023.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

@Component
@TextCommandHandler
@RequiredArgsConstructor
public class StartHandler implements Handler {

    private static final String GREETING = "Добро пожаловать, %s. Ваша роль: %s";


    private final UserService service;

    private final ReplyKeyboardMaker replyKeyboardMaker;


    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        User user = service.create(update);

        return List.of(createMessage(update.getMessage().getChatId().toString(),
                String.format(GREETING, user.getName(), user.getRole()), replyKeyboardMaker.getMainMenuKeyboard()));
    }

    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.START;
    }
}
