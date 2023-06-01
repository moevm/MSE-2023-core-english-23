package core.english.mse2023.service;

import core.english.mse2023.model.Lesson;

import java.time.Duration;
import java.util.UUID;

public interface NotificationService {

    /**
     * Sends notification to student that his lesson date has been changed
     * @param lessonId UUID of the lesson whose date was changed
     */
    void sendLessonDateChangedNotification(UUID lessonId);

    /**
     * Sends notification to student that his lesson will start soon
     * @param lesson Lesson to notify about
     * @param threshold How far in advance before the start of the lesson is required to be alerted
     */
    void sendUpcomingLessonNotification(Lesson lesson, Duration threshold);
}
