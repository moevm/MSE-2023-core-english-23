package core.english.mse2023.handler.impl.action;

import core.english.mse2023.aop.annotation.handler.AdminRole;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.aop.annotation.handler.TeacherRole;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.component.InlineKeyboardMaker;
import core.english.mse2023.component.MessageTextMaker;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.exception.LessonAlreadyFinishedException;
import core.english.mse2023.exception.LessonHasNotStartedYetException;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@InlineButtonType
@AdminRole
@TeacherRole
@RequiredArgsConstructor
public class FinishLessonHandler implements Handler {

    private static final String ALREADY_FINISHED_TEXT = "Урок не может быть повторно завершен!";
    private static final String NOT_STARTED_YET_TEXT = "Урок еще не начался. Невозможно завершить!";

    private final InlineKeyboardMaker inlineKeyboardMaker;
    private final MessageTextMaker messageTextMaker;

    private final LessonService lessonService;

    @Override
    public List<BotApiMethod<?>> handle(Update update, UserRole userRole) {

        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        UUID lessonId = UUID.fromString(buttonData.getData());

        List<BotApiMethod<?>> messages = new ArrayList<>();

        try {
            Lesson lesson = lessonService.finishLesson(lessonId);

            messages.add(EditMessageText.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .text(messageTextMaker.lessonInfoPatternMessageText(lesson))
                    .replyMarkup(inlineKeyboardMaker.getLessonMainMenuInlineKeyboard(
                            lesson,
                            lessonService.getLessonInfoByLessonId(lessonId),
                            userRole
                    ))
                    .parseMode(ParseMode.MARKDOWNV2)
                    .build());

        } catch (LessonAlreadyFinishedException exception) {
            messages.add(SendMessage.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .text(ALREADY_FINISHED_TEXT)
                    .build());
        }

        messages.add(new AnswerCallbackQuery(update.getCallbackQuery().getId()));

        return messages;
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.FINISH_LESSON;
    }
}
