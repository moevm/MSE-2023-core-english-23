package core.english.mse2023.handler.impl.todo;

import core.english.mse2023.aop.annotation.handler.AllRegisteredRoles;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.component.InlineKeyboardMaker;
import core.english.mse2023.component.MessageTextMaker;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.LessonInfo;
import core.english.mse2023.model.dictionary.AttendanceType;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;
import java.util.UUID;

@Component
@InlineButtonType
@AllRegisteredRoles
@RequiredArgsConstructor
public class SetLessonAttendedHandler implements Handler {

    private final MessageTextMaker messageTextMaker;

    private final LessonService lessonService;

    private final InlineKeyboardMaker inlineKeyboardMaker;

    @Override
    public List<BotApiMethod<?>> handle(Update update, UserRole userRole) {
        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        UUID lessonId = UUID.fromString(buttonData.getData());
        lessonService.setAttendance(lessonId, AttendanceType.ATTENDED);

        Lesson lesson = lessonService.getLessonById(lessonId);
        LessonInfo lessonInfo = lessonService.getLessonInfoByLessonId(lessonId);

        return List.of(EditMessageText.builder()
                        .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                        .messageId(update.getCallbackQuery().getMessage().getMessageId())
                        .text(messageTextMaker.lessonInfoPatternMessageText(lesson, lessonInfo))
                        .replyMarkup(inlineKeyboardMaker.getLessonMainMenuInlineKeyboard(
                                lesson,
                                lessonService.getLessonInfoByLessonId(lessonId),
                                userRole
                        ))
                        .build()
        );
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.SET_LESSON_ATTENDED;
    }
}
