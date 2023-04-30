package core.english.mse2023.service.impl;

import core.english.mse2023.dto.SubscriptionCreationDTO;
import core.english.mse2023.exception.SubscriptionAlreadyCanceledException;
import core.english.mse2023.exception.SubscriptionDoesNotExistsException;
import core.english.mse2023.model.Family;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.dictionary.AttendanceType;
import core.english.mse2023.model.dictionary.LessonStatus;
import core.english.mse2023.model.dictionary.SubscriptionStatus;
import core.english.mse2023.repository.FamilyRepository;
import core.english.mse2023.repository.LessonRepository;
import core.english.mse2023.repository.SubscriptionRepository;
import core.english.mse2023.repository.UserRepository;
import core.english.mse2023.service.LessonService;
import core.english.mse2023.service.SubscriptionService;
import core.english.mse2023.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static core.english.mse2023.model.dictionary.LessonStatus.CANCELLED;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private static final String LESSON_TOPIC_TEMPLATE = "Урок №%s";

    private final SubscriptionRepository subscriptionRepository;

    private final LessonService lessonService;
    private final UserService userService;

    @Override
    @Transactional
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @Override
    @Transactional
    public List<Subscription> getAllSubscriptionsInFamily(String parentTelegramId) {

        List<Subscription> subscriptions = new ArrayList<>();

        for (Family family : userService.getAllFamiliesWithParent(parentTelegramId)) {
            subscriptions.addAll(subscriptionRepository.getAllByStudent(family.getStudent()));
        }

        return subscriptions;
    }

    @Override
    @Transactional
    public List<Subscription> getAllSubscriptionsWithTeacher(String teacherTelegramId) {

        return subscriptionRepository.getAllByTeacher(userService.getUserByTelegramId(teacherTelegramId));

    }

    @Override
    @Transactional
    public List<Subscription> getAllSubscriptionsWithStudent(String studentTelegramId) {
        return subscriptionRepository.getAllByStudent(userService.getUserByTelegramId(studentTelegramId));
    }



    @Override
    @Transactional
    public void createSubscription(SubscriptionCreationDTO creationDTO) {

        Subscription subscription = Subscription.builder()
                .startDate(creationDTO.getStartDate())
                .endDate(creationDTO.getEndDate())
                .type(creationDTO.getType())
                .status(creationDTO.getStartDate().after(new Date()) ? SubscriptionStatus.NOT_YET_STARTED : SubscriptionStatus.ACTIVE)
                .student(userService.getUserByTelegramId(creationDTO.getStudentTelegramId()))
                .teacher(userService.getUserByTelegramId(creationDTO.getTeacherTelegramId()))
                .lessonsRest(creationDTO.getLessonsRest())
                .build();

        subscriptionRepository.save(subscription);

        for (int i = 0; i < subscription.getLessonsRest(); i++) {
            Lesson lesson = lessonService.createLesson(subscription, String.format(LESSON_TOPIC_TEMPLATE, (i + 1)));

            if (i == 0) {
                lesson.setDate(subscription.getStartDate());
            }
        }
    }

    @Override
    @Transactional
    public void cancelSubscription(UUID subscriptionId) throws SubscriptionDoesNotExistsException, SubscriptionAlreadyCanceledException {
        Subscription subscription = subscriptionRepository.getSubscriptionsById(subscriptionId);

        if (subscription == null) {
            throw new SubscriptionDoesNotExistsException(String.format("Subscription %s does not exists.", subscriptionId));
        }

        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            throw new SubscriptionAlreadyCanceledException(String.format("Subscription %s has already been cancelled.", subscriptionId));
        }

        subscription.setStatus(SubscriptionStatus.CANCELLED);

        List<Lesson> lessons = lessonService.getAllLessonsForSubscription(subscriptionId);
        lessons.forEach(lesson -> lessonService.cancelLesson(lesson.getId(), CANCELLED));
    }

}
