package core.english.mse2023.handler.impl.menu.regular;

import core.english.mse2023.aop.annotation.handler.AllRoles;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.component.ReplyKeyboardMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

@Component
@TextCommandType
@AllRoles
@RequiredArgsConstructor
public class StartHandler implements Handler {

    private static final String GREETING = "Добро пожаловать, %s. Ваша роль: %s";


    private final UserService userService;

    private final ReplyKeyboardMaker replyKeyboardMaker;


    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {
        User user = userService.getUserByTelegramId(update.getMessage().getFrom().getId().toString());

        if (user == null) {
            user = userService.createUser(
                    update.getMessage().getFrom().getId().toString(),
                    update.getMessage().getFrom().getFirstName(),
                    update.getMessage().getFrom().getLastName()
            );
        }

        return List.of(SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(String.format(GREETING, user.getName(), user.getRole()))
                .replyMarkup(replyKeyboardMaker.getMainMenuKeyboard(userRole))
                .build());
    }

    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.START;
    }
}
