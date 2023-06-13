package core.english.mse2023.component;

import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.LessonInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class MessageTextMaker {

    @Value("${messages.text-maker.lesson-info-pattern}")
    private String lessonInfoPatternText;

    @Value("${messages.text-maker.more-lesson-info-pattern}")
    private String moreLessonInfoPatternText;

    @Value("${messages.text-maker.data-missing}")
    private String dataMissingText;

    @Value("${messages.text-maker.link-pattern}")
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
