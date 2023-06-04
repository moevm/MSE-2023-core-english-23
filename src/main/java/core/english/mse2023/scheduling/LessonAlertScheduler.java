package core.english.mse2023.scheduling;

import core.english.mse2023.model.Lesson;
import core.english.mse2023.service.LessonService;
import core.english.mse2023.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LessonAlertScheduler {

    @Value("${notification.upcoming-lesson-threshold}")
    private Duration UPCOMING_LESSON_NOTIFICATION_THRESHOLD;

    private final LessonService lessonService;
    private final NotificationService notificationService;

    @Scheduled(cron = "${notification.upcoming-lesson-cron}")
    public void alertAboutUpcomingLesson() {

        List<Lesson> upcomingLessons = lessonService.setAllLessonsWithinThresholdAlerted(UPCOMING_LESSON_NOTIFICATION_THRESHOLD);

        for (Lesson lesson : upcomingLessons) {
            notificationService.sendUpcomingLessonNotification(lesson, UPCOMING_LESSON_NOTIFICATION_THRESHOLD);
        }
    }

}
