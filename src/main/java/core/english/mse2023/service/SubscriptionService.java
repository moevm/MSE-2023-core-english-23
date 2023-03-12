package core.english.mse2023.service;

import core.english.mse2023.dto.SubscriptionCreationDTO;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.dictionary.LessonStatus;
import core.english.mse2023.model.dictionary.SubscriptionStatus;
import core.english.mse2023.repository.LessonRepository;
import core.english.mse2023.repository.SubscriptionRepository;
import core.english.mse2023.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService implements SubscriptionServiceInterface {

    private final SubscriptionRepository subscriptionRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void create(SubscriptionCreationDTO creationDTO) {

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

        List<Lesson> lessons = new ArrayList<>();

        for (int i = 0; i < creationDTO.getLessonsRest(); i++) {
            Lesson lesson = new Lesson();

            lesson.setStatus(LessonStatus.NOT_STARTED_YET);
            lesson.setSubscription(subscription);

            lessons.add(lesson);
        }

        lessons.get(0).setDate(subscription.getStartDate());

        subscriptionRepository.save(subscription);

        lessonRepository.saveAll(lessons);

    }

}
