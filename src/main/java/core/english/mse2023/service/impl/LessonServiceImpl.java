package core.english.mse2023.service.impl;

import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.LessonInfo;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.dictionary.AttendanceType;
import core.english.mse2023.model.dictionary.LessonStatus;
import core.english.mse2023.repository.LessonInfoRepository;
import core.english.mse2023.repository.LessonRepository;
import core.english.mse2023.service.LessonInfoService;
import core.english.mse2023.service.LessonService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private static final String LESSON_TOPIC_TEMPLATE = "Урок №%s";


    private final LessonRepository lessonRepository;
    private final LessonInfoService lessonInfoService;

    @Override
    @Transactional
    public Lesson getLessonById(UUID id) {
        return lessonRepository.getLessonById(id);
    }

    @Override
    @Transactional
    public void cancelLessonsFromSubscription(UUID subscriptionId) {
        List<Lesson> lessons = lessonRepository.getAllBySubscriptionId(subscriptionId);

        for (Lesson lesson : lessons) {
            lesson.setStatus(LessonStatus.CANCELLED);
        }

        lessonRepository.saveAll(lessons);

    }

    @Transactional
    @Override
    public List<Lesson> getAllLessonsForSubscription(UUID subscriptionId) {
        return lessonRepository.getAllBySubscriptionId(subscriptionId);
    }

    @Override
    @Transactional
    public void createBaseLessonsForSubscription(Subscription subscription) {
        Lesson lesson = createLesson(subscription, String.format(LESSON_TOPIC_TEMPLATE, 1));
        lesson.setDate(subscription.getStartDate());

        lessonRepository.save(lesson);
        lessonInfoService.createLessonInfo(lesson);


        for (int i = 1; i < subscription.getLessonsRest(); i++) {
            lesson = createLesson(subscription, String.format(LESSON_TOPIC_TEMPLATE, (i + 1)));
            lessonRepository.save(lesson);
            lessonInfoService.createLessonInfo(lesson);
        }
    }

    @Override
    @Transactional
    public Lesson createLesson(Subscription subscription, String topic) {
        Lesson lesson = new Lesson();

        lesson.setStatus(LessonStatus.NOT_STARTED_YET);
        lesson.setSubscription(subscription);
        lesson.setTopic(topic);

        return lesson;
    }

    @Override
    @Transactional
    public void setAttendance(UUID lessonId, AttendanceType attendanceType) {

        Lesson lesson = lessonRepository.getLessonById(lessonId);

        if (attendanceType == AttendanceType.ATTENDED || attendanceType == AttendanceType.SKIPPED) {
            lesson.setStatus(LessonStatus.ENDED);
            lessonRepository.save(lesson);
        }

        lessonInfoService.setAttendance(lesson, attendanceType);
    }
}
