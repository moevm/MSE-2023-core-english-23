package core.english.mse2023.handler.impl.menu.regular;

import core.english.mse2023.aop.annotation.handler.AllRoles;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.component.ReplyKeyboardMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.dictionary.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
public class ToMainMenuHandler implements Handler {

    @Value("${handlers.to-main-menu-handler.message}")
    private String message;

    private final ReplyKeyboardMaker replyKeyboardMaker;

    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {

        return List.of(SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(message)
                .replyMarkup(replyKeyboardMaker.getMainMenuKeyboard(userRole))
                .build());
    }

    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.TO_MAIN_MENU;
    }
}
