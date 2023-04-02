package core.english.mse2023.handler.impl;

import core.english.mse2023.aop.annotation.handler.AllRoles;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.component.ReplyKeyboardMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.handler.Handler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

@Component
@TextCommandType
@AllRoles
@AllArgsConstructor
public class ToStatisticsMenuHandler  implements Handler {

    private static final String MESSAGE = "Вы вернулись в меню СТАТИСТИКА";
    private final ReplyKeyboardMaker replyKeyboardMaker;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {

        return List.of(SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(MESSAGE)
                .replyMarkup(replyKeyboardMaker.getStatisticsKeyboard())
                .build());
    }

    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.TO_STATISTICS_MENU;
    }
}
