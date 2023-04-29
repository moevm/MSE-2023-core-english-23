package core.english.mse2023.handler.impl.action;

import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.aop.annotation.handler.StudentRole;
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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;
import java.util.UUID;

@Component
@InlineButtonType
@StudentRole
@RequiredArgsConstructor
public class SetHomeworkNotCompletedHandler implements Handler {

    private static final String DONE_TEXT = "Домашнее задание для урока \"%s\" установлено, как не выполненное.";

    private final LessonService lessonService;

    @Override
    public List<BotApiMethod<?>> handle(Update update, UserRole userRole) {
        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        UUID lessonId = UUID.fromString(buttonData.getData());

        Lesson lesson = lessonService.setLessonHomeworkCompletion(lessonId, false);

        SendMessage doneMessage = SendMessage.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                .text(String.format(DONE_TEXT, lesson.getTopic()))
                .build();

        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery(update.getCallbackQuery().getId());

        return List.of(doneMessage, answerCallbackQuery);
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.SET_HOMEWORK_NOT_COMPLETED;
    }
}