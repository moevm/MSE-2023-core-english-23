package core.english.mse2023.handler.impl;

import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.service.LessonService;
import core.english.mse2023.util.utilities.TelegramMessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Component
@InlineButtonType
@RequiredArgsConstructor
public class GetMoreLessonInfoHandler implements Handler {

    private static final String LESSON_DATA_PATTERN = """
            Данные об уроке
            Статус: %s
            Дата проведения: %s
            Тема: %s
            Ссылка: %s
            """;

    private static final String NO_DATA_TEXT = "Не установлена";

    private static final String LINK_PATTERN = "[Перейти](%s)";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd\\.MM\\.yyyy");


    private final LessonService lessonService;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {

        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        Lesson lesson = lessonService.getLessonById(UUID.fromString(buttonData.getData()));


        SendMessage message = TelegramMessageUtils.createMessage(
                update.getCallbackQuery().getMessage().getChatId().toString(),
                String.format(LESSON_DATA_PATTERN,
                        lesson.getStatus().toString(),
                        lesson.getDate() == null ? NO_DATA_TEXT : dateFormat.format(lesson.getDate()),
                        lesson.getTopic() == null ? NO_DATA_TEXT : lesson.getTopic(),
                        lesson.getLink() == null ? NO_DATA_TEXT : String.format(LINK_PATTERN, lesson.getLink())
                )
        );

        message.setParseMode(ParseMode.MARKDOWNV2);

        return List.of(message);
    }

    @Override
    public BotCommand getCommandObject() {
        return new BotCommand(InlineButtonCommand.GET_MORE_LESSON_INFO, "");
    }
}
