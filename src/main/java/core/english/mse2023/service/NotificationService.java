package core.english.mse2023.service;

import java.util.UUID;

public interface NotificationService {

    /**
     * Sends notification to student that his lesson date has been changed
     * @param lessonId UUID of the lesson whose date was changed
     */
    void sendLessonDateChangedNotification(UUID lessonId);
}
