package core.english.mse2023.handler.impl;

import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.component.InlineKeyboardMaker;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.util.builder.InlineKeyboardBuilder;
import core.english.mse2023.util.utilities.TelegramInlineButtonsUtils;
import core.english.mse2023.util.utilities.TelegramMessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Component
@InlineButtonType
@RequiredArgsConstructor
public class GetAttendMenuHandler implements Handler {

    private static final String ATTENDED_TEXT = "Посетил";

    private static final String DID_NOT_ATTEND_TEXT = "Пропустил";

    private final InlineKeyboardMaker inlineKeyboardMaker;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());
        return List.of(
                TelegramMessageUtils.editMessageReplyMarkup(
                        update.getCallbackQuery().getMessage().getChatId().toString(),
                        update.getCallbackQuery().getMessage().getMessageId(),
                        inlineKeyboardMaker.getLessonAttendanceMenu(buttonData.getData())
                )
        );
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.GET_ATTEND_MENU;
    }
}
