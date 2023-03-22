package core.english.mse2023.service.impl;

import core.english.mse2023.dto.SubscriptionCreationDTO;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.dictionary.LessonStatus;
import core.english.mse2023.model.dictionary.SubscriptionStatus;
import core.english.mse2023.repository.LessonRepository;
import core.english.mse2023.repository.SubscriptionRepository;
import core.english.mse2023.repository.UserRepository;
import core.english.mse2023.service.LessonService;
import core.english.mse2023.service.SubscriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final LessonService lessonService;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Subscription createSubscription(SubscriptionCreationDTO creationDTO) {

        Subscription subscription = new Subscription();

        subscription.setStartDate(creationDTO.getStartDate());
        subscription.setEndDate(creationDTO.getEndDate());

        subscription.setType(creationDTO.getType());

        if (subscription.getStartDate().after(new Date())) {
            subscription.setStatus(SubscriptionStatus.NOT_YET_STARTED);
        } else {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }

        subscription.setStudent(userRepository.findByTelegramId(creationDTO.getStudentTelegramId()));
        subscription.setTeacher(userRepository.findByTelegramId(creationDTO.getTeacherTelegramId()));
        subscription.setLessonsRest(creationDTO.getLessonsRest());

        subscriptionRepository.save(subscription);

        lessonService.createBaseLessonsForSubscription(subscription);

        return subscription;
    }

    @Override
    @Transactional
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }


    @Override
    @Transactional
    public boolean cancelSubscription(UUID subscriptionId) {
        Subscription subscription = subscriptionRepository.getSubscriptionsById(subscriptionId);

        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            return false;
        }
        subscription.setStatus(SubscriptionStatus.CANCELLED);

        lessonService.cancelLessonsFromSubscription(subscriptionId);

        subscriptionRepository.save(subscription);
        return true;
    }

}
