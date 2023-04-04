package core.english.mse2023.handler.impl.action;
import core.english.mse2023.aop.annotation.handler.AdminRole;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.aop.annotation.handler.StudentRole;
import core.english.mse2023.aop.annotation.handler.TeacherRole;
import core.english.mse2023.component.InlineKeyboardMaker;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.dictionary.LessonStatus;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.LessonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
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
@TeacherRole
@AdminRole
@StudentRole
public class CancelLessonHandler implements Handler {
    private static final String DONE_TEXT = "Выбранный урок отменён.";
    private static final String ENDED_TEXT = "Невозможно отменить урок. Он уже завершён.";
    private static final String IN_PROGRESS_TEXT = "Невозможно отменить урок. Он уже начат.";

    private final InlineKeyboardMaker inlineKeyboardMaker;
    private final LessonService lessonService;

    @Override
    public List<BotApiMethod<?>> handle(Update update, UserRole userRole) {
        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        UUID lessonId = UUID.fromString(buttonData.getData());
        Lesson lesson = lessonService.cancelLesson(lessonId);
        List<BotApiMethod<?>> messages = new ArrayList<>();
        switch (lesson.getStatus()) {
            case ENDED -> messages.add(SendMessage.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .text(ENDED_TEXT)
                    .build());
            case IN_PROGRESS -> messages.add(SendMessage.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .text(IN_PROGRESS_TEXT)
                    .build());
            default -> {
                messages.add(EditMessageReplyMarkup.builder()
                        .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                        .messageId(update.getCallbackQuery().getMessage().getMessageId())
                        .replyMarkup(inlineKeyboardMaker.getLessonMainMenuInlineKeyboard(
                                lessonId.toString(),
                                lesson.getStatus(),
                                lesson.getDate() != null,
                                userRole)).build());
                messages.add(SendMessage.builder()
                        .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                        .text(DONE_TEXT)
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
