package core.english.mse2023.handler.impl.menu.inline;

import core.english.mse2023.aop.annotation.handler.AllRegisteredRoles;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.component.InlineKeyboardMaker;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.dictionary.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

@Component
@InlineButtonType
@AllRegisteredRoles
@RequiredArgsConstructor
public class MainMenuSubscriptionHandler implements Handler {

    private final InlineKeyboardMaker inlineKeyboardMaker;


    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {
        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        return List.of(
                EditMessageReplyMarkup.builder()
                        .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                        .messageId(update.getCallbackQuery().getMessage().getMessageId())
                        .replyMarkup(inlineKeyboardMaker.getSubscriptionMainMenu(buttonData.getData(), userRole))
                        .build(),
                new AnswerCallbackQuery(update.getCallbackQuery().getId())
        );
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.MAIN_MENU_SUBSCRIPTION;
    }
}
