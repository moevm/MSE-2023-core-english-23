package core.english.mse2023.service;

import core.english.mse2023.model.Lesson;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.Duration;
import java.util.UUID;

public interface NotificationService {

    /**
     * Creates notification message to student that his lesson date has been changed
     * @param lessonId UUID of the lesson whose date was changed
     * @return New SendMessage object with notification data
     */
    SendMessage getLessonDateChangedNotificationMessage(UUID lessonId);

    /**
     * Creates notification message to student that his lesson will start soon
     * @param lesson Lesson to notify about
     * @param threshold How far in advance before the start of the lesson is required to be alerted
     * @return New SendMessage object with notification data
     */
    SendMessage getUpcomingLessonNotificationMessage(Lesson lesson, Duration threshold);
}
