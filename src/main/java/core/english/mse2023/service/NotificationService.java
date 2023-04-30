package core.english.mse2023.service;

import core.english.mse2023.aop.aspect.Notification;

import java.util.UUID;

public interface NotificationService {

    void sendLessonDateChangedNotification(UUID lessonId);
}
