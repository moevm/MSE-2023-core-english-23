package core.english.mse2023.service;

import core.english.mse2023.dto.SubscriptionCreationDTO;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.Subscription;

public interface SubscriptionService {
    void createSubscription(SubscriptionCreationDTO creationDTO);

    Lesson createLesson(Subscription subscription);
}
