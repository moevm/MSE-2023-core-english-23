package core.english.mse2023.handler.impl.action;


import core.english.mse2023.aop.annotation.handler.AdminRole;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.aop.annotation.handler.TeacherRole;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.util.builder.InlineKeyboardBuilder;
import core.english.mse2023.util.utilities.TelegramInlineButtonsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@InlineButtonType
@AdminRole
@TeacherRole
@RequiredArgsConstructor
public class SetMarkMenuHandler implements Handler {

    private static final String MESSAGE = "Выберите оценку:";

    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {
        InlineButtonDTO inlineButtonDTO = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        return List.of(SendMessage.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                .text(MESSAGE)
                .replyMarkup(getMarkButtons(inlineButtonDTO.getData()))
                .build());
    }

    private InlineKeyboardMarkup getMarkButtons(String lessonId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.instance()
                .button(TelegramInlineButtonsUtils.createInlineButtonWithDescriptionOverride(
                        InlineButtonCommand.SET_MARK,
                        "5",
                        lessonId,
                        5
                ))
                .row()
                .button(TelegramInlineButtonsUtils.createInlineButtonWithDescriptionOverride(
                        InlineButtonCommand.SET_MARK,
                        "4",
                        lessonId,
                        4
                ))
                .row()
                .button(TelegramInlineButtonsUtils.createInlineButtonWithDescriptionOverride(
                        InlineButtonCommand.SET_MARK,
                        "3",
                        lessonId,
                        3
                ))
                .row()
                .button(TelegramInlineButtonsUtils.createInlineButtonWithDescriptionOverride(
                        InlineButtonCommand.SET_MARK,
                        "2",
                        lessonId,
                        2
                ))
                .row()
                .button(TelegramInlineButtonsUtils.createInlineButtonWithDescriptionOverride(
                        InlineButtonCommand.SET_MARK,
                        "1",
                        lessonId,
                        1
                ))
                .row();

        inlineKeyboardMarkup.setKeyboard(builder.build().getKeyboard());
        return inlineKeyboardMarkup;
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.SET_MARK_MENU;
    }


}

