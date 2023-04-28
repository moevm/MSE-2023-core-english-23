package core.english.mse2023.service;

import core.english.mse2023.dto.SubscriptionCreationDTO;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.User;

import java.util.List;
import java.util.UUID;

public interface SubscriptionService {
    Subscription createSubscription(SubscriptionCreationDTO creationDTO);

    List<Subscription> getAllSubscriptions();

    List<Subscription> getAllSubscriptionsInFamily(String parentTelegramId);

    List<Subscription> getAllSubscriptionsWithTeacher(String teacherTelegramId);

    List<Subscription> getAllSubscriptionsWithStudent(String studentTelegramId);

    boolean cancelSubscription(UUID subscriptionId);
}
