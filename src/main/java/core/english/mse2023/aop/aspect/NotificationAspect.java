package core.english.mse2023.aop.aspect;

import core.english.mse2023.service.NotificationService;
import core.english.mse2023.service.UserService;
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
    private final UserService userService;

    @After(value = "execution(public * core.english.mse2023.service.LessonService.setLessonDate(..)) && args(date, lessonId)",
            argNames = "date,lessonId")
    public void notifyChangeLesson(Timestamp date, UUID lessonId) {

        //todo конвертировать lessonId -> userId через сервисный метод
        UUID userId = lessonId;
        System.out.println("?? sending change lesson date notification for userId=" + userId);

        notificationService.sendNotification(userId, Notification.CHANGE_LESSON_DATE);
    }
}
