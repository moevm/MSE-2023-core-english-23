package core.english.mse2023.service;

import core.english.mse2023.dto.SubscriptionCreationDTO;
import core.english.mse2023.exception.SubscriptionAlreadyCanceledException;
import core.english.mse2023.exception.SubscriptionDoesNotExistsException;
import core.english.mse2023.model.Subscription;

import java.util.List;
import java.util.UUID;

public interface SubscriptionService {

    /**
     * Gets subscription object by its UUID
     * @param subscriptionId UUID of subscription
     * @return Subscription object with given UUID
     */
    Subscription getSubscriptionById(UUID subscriptionId);

    /**
     * Gets all subscriptions it the database
     * @return All found subscriptions
     */
    List<Subscription> getAllSubscriptions();

    /**
     * Gets all subscriptions in particular family
     * @param parentTelegramId Parent id to choose families from
     * @return List of subscriptions
     */
    List<Subscription> getAllSubscriptionsInFamily(String parentTelegramId);

    /**
     * Gets all subscriptions with particular teacher
     * @param teacherTelegramId Teacher's telegram ID
     * @return List of subscriptions
     */
    List<Subscription> getAllSubscriptionsWithTeacher(String teacherTelegramId);

    /**
     * Gets all subscriptions with particular student
     * @param studentTelegramId Student's telegram ID
     * @return List of subscriptions
     */
    List<Subscription> getAllSubscriptionsWithStudent(String studentTelegramId);


    /**
     * Creates new Subscription
     * @param creationDTO DTO with data needed for subscription creation
     */
    void createSubscription(SubscriptionCreationDTO creationDTO);

    /**
     * Cancels subscription and all lessons attached to it
     * @param subscriptionId UUID of subscription
     * @throws SubscriptionDoesNotExistsException If subscription with given UUID doesn't exist
     * @throws SubscriptionAlreadyCanceledException If subscription already canceled
     */
    void cancelSubscription(UUID subscriptionId) throws SubscriptionDoesNotExistsException, SubscriptionAlreadyCanceledException;
}
