package core.english.mse2023.component;

import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.LessonInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class MessageTextMaker {

    @Value("${message-text-maker.lesson-info-pattern-text}")
    private String lessonInfoPatternText;

    @Value("${message-text-maker.more-lesson-info-pattern-text}")
    private String moreLessonInfoPatternText;

    @Value("${message-text-maker.data-missing-text}")
    private String dataMissingText;

    @Value("${message-text-maker.link-pattern-text}")
    private String linkPatternText;


    public String userDataPatternMessageText(String name, String lastName) {
        String messageTemplate = "%s%s";

        return String.format(messageTemplate,
                (lastName != null) ? (lastName + " ") : "",
                name
        );
    }

    public String lessonInfoPatternMessageText(Lesson lesson, LessonInfo lessonInfo) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd\\.MM\\.yyyy");

        return String.format(lessonInfoPatternText,
                lesson.getTopic() == null ? dataMissingText : lesson.getTopic(),
                lesson.getStatus().toString(),
                lesson.getDate() == null ? dataMissingText : dateFormat.format(lesson.getDate()),
                lessonInfo.getScore() == null ? dataMissingText : lessonInfo.getScore(),
                lesson.getLink() == null ? dataMissingText : String.format(linkPatternText, lesson.getLink())
        );
    }

    public String moreLessonInfoPatternMessageText(Lesson lesson) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        return String.format(moreLessonInfoPatternText,
                lesson.getTopic() == null ? dataMissingText : lesson.getTopic(),
                lesson.getStatus().toString(),
                lesson.getDate() == null ? dataMissingText : dateFormat.format(lesson.getDate())
        );
    }
}
