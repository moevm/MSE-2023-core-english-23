package core.english.mse2023.component;

import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.LessonInfo;
import core.english.mse2023.model.dictionary.UserRole;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class MessageTextMaker {

    public String userDataPatternMessageText(String lastName, String name) {
        String messageTemplate = "%s%s";

        return String.format(messageTemplate, lastName, name);
    }

    public String lessonInfoPatternMessageText(Lesson lesson, LessonInfo lessonInfo) {

        String messageTemplate = """
                Данные об уроке
                Тема: %s
                Статус: %s
                Дата проведения: %s
                Оценка: %s
                Ссылка: %s
                """;

        String NO_DATA_TEXT = "Не установлена";

        String LINK_PATTERN = "[Перейти](%s)";

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd\\.MM\\.yyyy");

        return String.format(messageTemplate,
                lesson.getTopic() == null ? NO_DATA_TEXT : lesson.getTopic(),
                lesson.getStatus().toString(),
                lesson.getDate() == null ? NO_DATA_TEXT : dateFormat.format(lesson.getDate()),
                lessonInfo.getScore() == null ? NO_DATA_TEXT : lessonInfo.getScore(),
                lesson.getLink() == null ? NO_DATA_TEXT : String.format(LINK_PATTERN, lesson.getLink())
        );
    }

    public String moreLessonInfoPatternMessageText(Lesson lesson) {

        String messageTemplate = """
                
                
                Урок: %s
                Статус: %s
                Дата проведения: %s
                """;

        String NO_DATA_TEXT = "Не установлена";

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        return String.format(messageTemplate,
                lesson.getTopic() == null ? NO_DATA_TEXT : lesson.getTopic(),
                lesson.getStatus().toString(),
                lesson.getDate() == null ? NO_DATA_TEXT : dateFormat.format(lesson.getDate())
        );
    }
}
