package core.english.mse2023.service;

import core.english.mse2023.dto.SubscriptionCreationDTO;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.dictionary.LessonStatus;
import core.english.mse2023.model.dictionary.SubscriptionStatus;
import core.english.mse2023.repository.LessonRepository;
import core.english.mse2023.repository.SubscriptionRepository;
import core.english.mse2023.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");


    public void create(SubscriptionCreationDTO creationDTO) {

        Subscription subscription = new Subscription();



        try {
            Date parsedDate = dateFormat.parse(creationDTO.getStartDate());
            subscription.setStartDate(new Timestamp(parsedDate.getTime()));
        } catch (ParseException e) {
            System.out.println("Failed to parse date format on StartDate parameter");
        }

        try {
            Date parsedDate = dateFormat.parse(creationDTO.getEndDate());
            subscription.setEndDate(new Timestamp(parsedDate.getTime()));
        } catch (ParseException e) {
            System.out.println("Failed to parse date format on StartDate parameter");
        }

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
