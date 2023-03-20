package core.english.mse2023.service;

import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.Subscription;

import java.util.List;
import java.util.UUID;

public interface LessonService {

    Lesson getLessonById(UUID id);

    void cancelLessonsFromSubscription(UUID subscriptionId);

    List<Lesson> getAllLessonsForSubscription(UUID subscriptionId);

    void createLessonsForSubscriptions(Subscription subscription, int amount);

    Lesson createLesson(Subscription subscription, String topic);

}
