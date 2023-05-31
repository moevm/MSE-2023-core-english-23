package core.english.mse2023.handler.impl.action;
import core.english.mse2023.aop.annotation.handler.AdminRole;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.component.InlineKeyboardMaker;
import core.english.mse2023.component.MessageTextMaker;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.LessonInfo;
import core.english.mse2023.model.dictionary.LessonStatus;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.LessonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
@InlineButtonType
@AdminRole
public class CancelLessonHandler implements Handler {
    private static final String DONE_TEXT = "Урок отменён.";
    private static final String ENDED_TEXT = "Невозможно отменить урок. Он уже завершён.";
    private static final String IN_PROGRESS_TEXT = "Невозможно отменить урок. Он уже начат.";

    private final InlineKeyboardMaker inlineKeyboardMaker;
    private final LessonService lessonService;
    private final MessageTextMaker messageTextMaker;

    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {
        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        UUID lessonId = UUID.fromString(buttonData.getData());
        Lesson lesson = lessonService.cancelLesson(lessonId, LessonStatus.CANCELLED_BY_TEACHER);
        List<PartialBotApiMethod<?>> messages = new ArrayList<>();

        switch (lesson.getStatus()) {
            case ENDED -> messages.add(SendMessage.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .text(ENDED_TEXT + messageTextMaker.moreLessonInfoPatternMessageText(lesson))
                    .build());
            case IN_PROGRESS -> messages.add(SendMessage.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .text(IN_PROGRESS_TEXT + messageTextMaker.moreLessonInfoPatternMessageText(lesson))
                    .build());
            default -> {
                messages.add(EditMessageReplyMarkup.builder()
                        .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                        .messageId(update.getCallbackQuery().getMessage().getMessageId())
                        .replyMarkup(inlineKeyboardMaker.getLessonMainMenuInlineKeyboard(
                                lesson,
                                lessonService.getLessonInfoByLessonId(lessonId),
                                userRole)).build());
                messages.add(SendMessage.builder()
                        .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                        .text(DONE_TEXT + messageTextMaker.moreLessonInfoPatternMessageText(lesson))
                        .build());
            }
        }

        messages.add(new AnswerCallbackQuery(update.getCallbackQuery().getId()));

        return messages;
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.CANCEL_LESSON;
    }


}
