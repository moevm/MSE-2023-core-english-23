package core.english.mse2023.service;

import core.english.mse2023.dto.SubscriptionCreationDTO;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.User;

import java.util.List;

public interface SubscriptionService {
    void createSubscription(SubscriptionCreationDTO creationDTO);

    List<Subscription> getAllSubscriptions();

    Lesson createLesson(Subscription subscription);
}
