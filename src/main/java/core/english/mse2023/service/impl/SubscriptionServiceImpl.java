package core.english.mse2023.service.impl;

import core.english.mse2023.dto.SubscriptionCreationDTO;
import core.english.mse2023.model.Family;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.dictionary.LessonStatus;
import core.english.mse2023.model.dictionary.SubscriptionStatus;
import core.english.mse2023.repository.FamilyRepository;
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
    private final FamilyRepository familyRepository;

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
    public List<Subscription> getAllSubscriptionsInFamily(String parentTelegramId) {

        List<Subscription> subscriptions = new ArrayList<>();

        for (Family family : familyRepository.getAllByParent(userRepository.findByTelegramId(parentTelegramId))) {
            subscriptions.addAll(subscriptionRepository.getAllByStudent(family.getStudent()));
        }

        return subscriptions;
    }

    @Override
    @Transactional
    public List<Subscription> getAllSubscriptionsWithTeacher(String teacherTelegramId) {

        return new ArrayList<>(subscriptionRepository.getAllByTeacher(userRepository.findByTelegramId(teacherTelegramId)));

    }

    @Override
    public List<Subscription> getAllSubscriptionsWithStudent(String studentTelegramId) {

        return new ArrayList<>(subscriptionRepository.getAllByStudent(userRepository.findByTelegramId(studentTelegramId)));
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
