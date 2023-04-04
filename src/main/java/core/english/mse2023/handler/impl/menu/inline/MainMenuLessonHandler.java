package core.english.mse2023.handler.impl.menu.inline;

import core.english.mse2023.aop.annotation.handler.AllRoles;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.component.InlineKeyboardMaker;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;
import java.util.UUID;

@Component
@InlineButtonType
@AllRoles
@RequiredArgsConstructor
public class MainMenuLessonHandler implements Handler {

    private final InlineKeyboardMaker inlineKeyboardMaker;

    private final LessonService lessonService;

    @Override
    public List<BotApiMethod<?>> handle(Update update, UserRole userRole) {
        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        Lesson lesson = lessonService.getLessonById(UUID.fromString(buttonData.getData()));

        return List.of(
                EditMessageReplyMarkup.builder()
                        .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                        .messageId(update.getCallbackQuery().getMessage().getMessageId())
                        .replyMarkup(inlineKeyboardMaker.getLessonMainMenuInlineKeyboard(buttonData.getData(), lesson.getStatus(), lesson.getDate() != null, userRole))
                        .build(),
                new AnswerCallbackQuery(update.getCallbackQuery().getId())
        );
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.MAIN_MENU_LESSON;
    }
}
