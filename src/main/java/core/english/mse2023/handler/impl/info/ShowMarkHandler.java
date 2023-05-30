package core.english.mse2023.handler.impl.info;
import core.english.mse2023.aop.annotation.handler.*;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
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
@TeacherRole
@AdminRole
@ParentRole
@StudentRole
@InlineButtonType
@RequiredArgsConstructor
public class ShowMarkHandler implements Handler {
    private static final String DATA_PATTERN = "Оценка за занятие: %s";

    private final LessonService lessonService;

    @Override
    public List<BotApiMethod<?>> handle(Update update, UserRole userRole) {

        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        UUID lessonId = UUID.fromString(buttonData.getData());

        int mark = lessonService.getLessonInfoByLessonId(lessonId).getScore();

        return List.of(
                SendMessage.builder()
                        .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                        .text(String.format(DATA_PATTERN, mark))
                        .build(),
                new AnswerCallbackQuery(update.getCallbackQuery().getId())
        );
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.SHOW_MARK;
    }
}