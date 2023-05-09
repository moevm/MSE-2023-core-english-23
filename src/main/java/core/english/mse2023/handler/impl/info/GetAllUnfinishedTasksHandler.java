package core.english.mse2023.handler.impl.info;

import core.english.mse2023.aop.annotation.handler.StudentRole;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.component.InlineKeyboardMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.LessonInfo;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.LessonService;
import core.english.mse2023.util.builder.InlineKeyboardBuilder;
import core.english.mse2023.util.utilities.TelegramInlineButtonsUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@StudentRole
@TextCommandType
@RequiredArgsConstructor
public class GetAllUnfinishedTasksHandler implements Handler {

    private static final String START_TEXT = "Список предстоящих (невыполненных) ИДЗ:";

    private static final String TASK_INFO_PATTERN = """
            Тема занятия: %s
            Дата проведения: %s
            Задание: %s
            """;

    private static final String TASK_LINK_PATTERN = "[Ссылка к ИДЗ](%s)";
    private static final String NO_TASKS_TEXT = "Предстоящие (невыполненные) ИДЗ отсутствуют.";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");


    private final InlineKeyboardMaker inlineKeyboardMaker;

    private final LessonService lessonService;

    @Override
    public List<BotApiMethod<?>> handle(Update update, UserRole userRole) {

        List<LessonInfo> lessonInfos = lessonService.getAllLessonInfosWithUnfinishedTask();


        return createTasksListMessages(lessonInfos, update.getMessage().getChatId().toString());
    }

    private List<BotApiMethod<?>> createTasksListMessages(List<LessonInfo> lessonInfos, String chatId) {

        List<BotApiMethod<?>> messages = new ArrayList<>();

        if (lessonInfos.size() == 0) {
            messages.add(SendMessage.builder()
                    .chatId(chatId)
                    .text(NO_TASKS_TEXT)
                    .build());
        } else {
            messages.add(SendMessage.builder()
                    .chatId(chatId)
                    .text(START_TEXT)
                    .build());
        }

        for (LessonInfo lessonInfo : lessonInfos) {
            boolean hasLink = lessonInfo.getTeacherComment().indexOf("http") == 0;

            String dateText = dateFormat.format(lessonInfo.getLesson().getDate());

            if (hasLink) {
                dateText = dateText.replaceAll("\\.", "\\\\.");
            }

            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(String.format(TASK_INFO_PATTERN,
                            lessonInfo.getLesson().getTopic(),
                            dateText,
                            (hasLink) ? // Проверка на ссылку
                                    String.format(TASK_LINK_PATTERN, lessonInfo.getTeacherComment()) :
                                    lessonInfo.getTeacherComment()
                    ))
                    .replyMarkup(inlineKeyboardMaker.getTaskMenu(lessonInfo))
                    .build();

            if (hasLink) {
                message.setParseMode(ParseMode.MARKDOWNV2);
            }

            messages.add(message);
        }


        return messages;
    }

    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.GET_ALL_UNFINISHED_TASKS;
    }
}
