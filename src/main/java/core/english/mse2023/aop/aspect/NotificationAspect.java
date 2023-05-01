package core.english.mse2023.aop.aspect;

import core.english.mse2023.model.Lesson;
import core.english.mse2023.service.LessonService;
import core.english.mse2023.service.NotificationService;
import core.english.mse2023.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationAspect {

    private final NotificationService notificationService;

    @After(
            value = "execution(public * core.english.mse2023.service.LessonService.setLessonDate(..)) && args(date, lessonId)",
            argNames = "date,lessonId"
    )
    public void notifyLessonDateChanged(Timestamp date, UUID lessonId) {
        notificationService.sendLessonDateChangedNotification(lessonId);
    }
}
