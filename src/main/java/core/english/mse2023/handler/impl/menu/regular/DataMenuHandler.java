package core.english.mse2023.handler.impl.menu.regular;

import core.english.mse2023.aop.annotation.handler.AdminRole;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.component.ReplyKeyboardMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.constant.Command;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.dictionary.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

@Component
@TextCommandType
@AdminRole
@AllArgsConstructor
public class DataMenuHandler implements Handler {

    private static final String MESSAGE = "Вы перешли в раздел РАБОТА С ДАННЫМИ";
    private final ReplyKeyboardMaker replyKeyboardMaker;

    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {

        return List.of(SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(MESSAGE)
                .replyMarkup(replyKeyboardMaker.getDataMenu())
                .build());
    }

    @Override
    public Command getCommandObject() {
        return ButtonCommand.DATA;
    }
}