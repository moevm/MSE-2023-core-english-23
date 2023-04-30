package core.english.mse2023.service.impl;

import core.english.mse2023.dto.LessonCreationDTO;
import core.english.mse2023.dto.SubscriptionCreationDTO;
import core.english.mse2023.exception.IllegalUserInputException;
import core.english.mse2023.exception.LessonAlreadyFinishedException;
import core.english.mse2023.exception.LessonHasNotStartedYetException;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.LessonHistory;
import core.english.mse2023.model.LessonInfo;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.dictionary.*;

import core.english.mse2023.model.dictionary.LessonStatus;
import core.english.mse2023.repository.LessonHistoryRepository;
import core.english.mse2023.repository.LessonInfoRepository;
import core.english.mse2023.repository.LessonRepository;
import core.english.mse2023.repository.SubscriptionRepository;
import core.english.mse2023.service.LessonService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static core.english.mse2023.model.dictionary.AttendanceType.NOT_YET_ATTENDED;
import static core.english.mse2023.model.dictionary.LessonStatus.*;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {


    private final SubscriptionRepository subscriptionRepository;
    private static final String LESSON_TOPIC_TEMPLATE = "Урок №%s";


    private final LessonRepository lessonRepository;
    private final LessonInfoService lessonInfoService;
    private final LessonHistoryRepository lessonHistoryRepository;

    private final LessonInfoRepository lessonInfoRepository;

    @Override
    @Transactional
    public Lesson getLessonById(UUID id) {
        return lessonRepository.getLessonById(id);
    }

    @Override
    @Transactional
    public LessonInfo getLessonInfoByLessonId(UUID lessonId) {

        Lesson lesson = lessonRepository.getLessonById(lessonId);

        return lessonInfoRepository.getLessonInfoByLesson(lesson);
    }

    @Override
    @Transactional
    public void cancelLessonsFromSubscription(UUID subscriptionId) {
        List<Lesson> lessons = lessonRepository.getAllBySubscriptionId(subscriptionId);

        for (Lesson lesson : lessons) {
            lesson.setStatus(LessonStatus.CANCELLED);
            LessonHistory lessonHistory = new LessonHistory();
            lessonHistory.setLesson(lesson);
            lessonHistory.setType(LessonHistoryEventType.CANCELLED);
            lessonHistoryRepository.save(lessonHistory);
            LessonInfo lessonInfo = lessonInfoRepository.getLessonInfoByLesson(lesson);
            lessonInfo.setAttendance(AttendanceType.CANCELLED);
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
        for (int i = 0; i < subscription.getLessonsRest(); i++) {
            createLesson(subscription, String.format(LESSON_TOPIC_TEMPLATE, (i + 1)));
        }
    }

    @Override
    @Transactional
    public Lesson createLesson(Subscription subscription, String topic) {
        Lesson lesson = new Lesson();

        lesson.setStatus(NOT_STARTED_YET);
        lesson.setSubscription(subscription);
        lesson.setTopic(topic);
        lessonRepository.save(lesson);

        LessonHistory lessonHistory = new LessonHistory();
        lessonHistory.setLesson(lesson);
        lessonHistory.setType(LessonHistoryEventType.CREATED);
        lessonHistoryRepository.save(lessonHistory);

        LessonInfo lessonInfo = new LessonInfo();
        lessonInfo.setLesson(lesson);
        lessonInfo.setAttendance(NOT_YET_ATTENDED);
        lessonInfoRepository.save(lessonInfo);

        return lesson;
    }
    @Transactional
    @Override
    public Lesson createLesson(LessonCreationDTO creationDTO, UserRole userRole) throws IllegalUserInputException {
        Subscription subscription = subscriptionRepository.getSubscriptionsById(UUID.fromString(creationDTO.getSubscriptionId()));

        Lesson lesson = createLesson(subscription,creationDTO.getTopic());
        if (creationDTO.getDate() != null){
            setLessonDate(creationDTO.getDate(), lesson.getId());
            if (lesson.getDate().after(new Date())) {
                lesson.setStatus(ENDED);
            }
        }
        lesson.setLink(creationDTO.getLink());

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

        LessonInfo lessonInfo = lessonInfoRepository.getLessonInfoByLesson(lesson);
        lessonInfo.setAttendance(attendanceType);
    }

    @Override
    @Transactional
    public Lesson cancelLesson(UUID lessonId){
        Lesson lesson = this.getLessonById(lessonId);
        LessonStatus status = lesson.getStatus();
        if (status == NOT_STARTED_YET) {

            lesson.setStatus(CANCELLED_BY_TEACHER);

            LessonHistory lessonHistory = new LessonHistory();
            lessonHistory.setLesson(lesson);
            lessonHistory.setType(LessonHistoryEventType.CANCELLED);
            lessonHistoryRepository.save(lessonHistory);

            LessonInfo lessonInfo = lessonInfoRepository.getLessonInfoByLesson(lesson);
            lessonInfo.setAttendance(AttendanceType.CANCELLED);
        }
        return lesson;
    }

    @Override
    @Transactional
    public Lesson finishLesson(UUID lessonId) throws LessonAlreadyFinishedException {
        Lesson lesson = this.getLessonById(lessonId);
        LessonStatus status = lesson.getStatus();

        if (status == ENDED)
            throw new LessonAlreadyFinishedException(String.format("Lesson with id '%s' cannot be finished more than once.", lessonId.toString()));

        lesson.setStatus(ENDED);

        LessonHistory lessonHistory = new LessonHistory();
        lessonHistory.setLesson(lesson);
        lessonHistory.setType(LessonHistoryEventType.UPDATED);

        return lesson;
    }

    @Override
    @Transactional
    public void setTeacherCommentForParent(String comment, UUID lessonId) {

        Lesson lesson = this.getLessonById(lessonId);

        LessonHistory lessonHistory = new LessonHistory();
        lessonHistory.setLesson(lesson);
        lessonHistory.setType(LessonHistoryEventType.UPDATED);
        lessonHistoryRepository.save(lessonHistory);

        LessonInfo lessonInfo = lessonInfoRepository.getLessonInfoByLesson(lesson);
        lessonInfo.setTeacherCommentForParent(comment);

    }
    @Override
    @Transactional
    public void setTeacherHomeworkComment(String comment, UUID lessonId) {

        Lesson lesson = this.getLessonById(lessonId);

        LessonHistory lessonHistory = new LessonHistory();
        lessonHistory.setLesson(lesson);
        lessonHistory.setType(LessonHistoryEventType.UPDATED);
        lessonHistoryRepository.save(lessonHistory);

        LessonInfo lessonInfo = lessonInfoRepository.getLessonInfoByLesson(lesson);
        lessonInfo.setTeacherComment(comment);

    }

    @Override
    @Transactional
    public Lesson setLessonDate(Timestamp date, UUID lessonId) {
        Lesson lesson = this.getLessonById(lessonId);

        if ( date.after(lesson.getSubscription().getEndDate()) ||
                date.before(lesson.getSubscription().getStartDate())) { //если не попадает в подписку
            throw new IllegalUserInputException("User entered lesson date outside subscription boundaries");
        }
        LessonHistory lessonHistory = new LessonHistory();
        lessonHistory.setLesson(lesson);
        lessonHistory.setType(LessonHistoryEventType.UPDATED);
        lessonHistoryRepository.save(lessonHistory);

        lesson.setDate(date);

        if (lesson.getDate().before(new Date())) {
            lesson.setStatus(ENDED);
        }

        return lesson;
    }

    @Override
    @Transactional
    public Lesson setLessonHomeworkCompletion(UUID lessonId, boolean isComplete) {

        Lesson lesson = lessonRepository.getLessonById(lessonId);

        LessonHistory lessonHistory = new LessonHistory();
        lessonHistory.setLesson(lesson);
        lessonHistory.setType(LessonHistoryEventType.UPDATED);
        lessonHistoryRepository.save(lessonHistory);

        LessonInfo lessonInfo = lessonInfoRepository.getLessonInfoByLesson(lesson);
        lessonInfo.setHomeworkCompleted(isComplete);

        return lesson;
    }
}
