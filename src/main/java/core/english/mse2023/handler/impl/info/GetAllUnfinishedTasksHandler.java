package core.english.mse2023.handler.impl.info;

import core.english.mse2023.aop.annotation.handler.StudentRole;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.component.InlineKeyboardMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.LessonInfo;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@StudentRole
@TextCommandType
@RequiredArgsConstructor
public class GetAllUnfinishedTasksHandler implements Handler {

    @Value("${handlers.get-all-unfinished-tasks-handler.start-text}")
    private String startText;

    @Value("${handlers.get-all-unfinished-tasks-handler.task-info-pattern}")
    private String taskInfoPattern;

    @Value("${handlers.get-all-unfinished-tasks-handler.task-link-pattern}")
    private String taskLinkPattern;

    @Value("${handlers.get-all-unfinished-tasks-handler.no-tasks-text}")
    private String noTasksText;

    private static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
    private Pattern urlPattern;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");


    private final InlineKeyboardMaker inlineKeyboardMaker;

    private final LessonService lessonService;

    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {

        List<LessonInfo> lessonInfos = lessonService.getAllLessonInfosWithUnfinishedTask();

        urlPattern = Pattern.compile(URL_REGEX);

        return createTasksListMessages(lessonInfos, update.getMessage().getChatId().toString());
    }

    private List<PartialBotApiMethod<?>> createTasksListMessages(List<LessonInfo> lessonInfos, String chatId) {

        List<PartialBotApiMethod<?>> messages = new ArrayList<>();

        if (lessonInfos.isEmpty()) {
            messages.add(SendMessage.builder()
                    .chatId(chatId)
                    .text(noTasksText)
                    .build());
        } else {
            messages.add(SendMessage.builder()
                    .chatId(chatId)
                    .text(startText)
                    .build());
        }

        for (LessonInfo lessonInfo : lessonInfos) {

            String dateText = dateFormat.format(lessonInfo.getLesson().getDate());
            String linkString = lessonInfo.getTeacherComment();
            String parseMode = null;

            if (isURL(lessonInfo.getTeacherComment())) {
                dateText = prepareForMarkup(dateText);
                linkString = String.format(taskLinkPattern, lessonInfo.getTeacherComment());
                parseMode = ParseMode.MARKDOWNV2;
            }

            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(String.format(taskInfoPattern,
                            lessonInfo.getLesson().getTopic(),
                            dateText,
                            linkString
                    ))
                    .replyMarkup(inlineKeyboardMaker.getTaskMenu(lessonInfo))
                    .parseMode(parseMode)
                    .build();

            messages.add(message);
        }


        return messages;
    }

    private boolean isURL(String text) {
        Matcher matcher = urlPattern.matcher(text);
        return matcher.find();
    }

    private String prepareForMarkup(String text) {
        return text.replaceAll("\\.", "\\\\.");
    }

    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.GET_ALL_UNFINISHED_TASKS;
    }
}
