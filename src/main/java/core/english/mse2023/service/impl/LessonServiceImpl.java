package core.english.mse2023.service.impl;

import core.english.mse2023.exception.LessonAlreadyFinishedException;
import core.english.mse2023.exception.LessonDoesNotExistsException;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.LessonHistory;
import core.english.mse2023.model.LessonInfo;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.dictionary.AttendanceType;
import core.english.mse2023.model.dictionary.LessonHistoryEventType;

import core.english.mse2023.model.dictionary.LessonStatus;
import core.english.mse2023.repository.LessonHistoryRepository;
import core.english.mse2023.repository.LessonInfoRepository;
import core.english.mse2023.repository.LessonRepository;
import core.english.mse2023.repository.SubscriptionRepository;
import core.english.mse2023.service.LessonService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static core.english.mse2023.model.dictionary.LessonStatus.*;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
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
        return lessonInfoRepository.getLessonInfoByLessonId(lessonId);
    }

    @Override
    @Transactional
    public List<Lesson> getAllLessonsForSubscription(UUID subscriptionId) {
        return lessonRepository.getAllBySubscriptionId(subscriptionId);
    }


    @Override
    @Transactional
    public Lesson createLesson(Subscription subscription, String topic) {

        Lesson lesson = Lesson.builder()
                .status(NOT_STARTED_YET)
                .subscription(subscription)
                .topic(topic)
                .build();

        lessonRepository.save(lesson);

        createHistoryEvent(lesson, LessonHistoryEventType.CREATED);
        createLessonInfo(lesson, AttendanceType.NOT_YET_ATTENDED);

        return lesson;
    }

    @Override
    @Transactional
    public Lesson finishLesson(UUID lessonId) throws LessonDoesNotExistsException, LessonAlreadyFinishedException {
        Lesson lesson = getLessonById(lessonId);

        if (lesson == null) {
            throw new LessonDoesNotExistsException(String.format("Lesson %s doesn't exist", lessonId));
        }

        LessonStatus status = lesson.getStatus();

        if (status == ENDED)
            throw new LessonAlreadyFinishedException(String.format("Lesson with id '%s' cannot be finished more than once.", lessonId.toString()));

        lesson.setStatus(ENDED);

        createHistoryEvent(lesson, LessonHistoryEventType.UPDATED);

        return lesson;
    }

    @Override
    @Transactional
    public Lesson cancelLesson(UUID lessonId, LessonStatus lessonStatus) throws LessonDoesNotExistsException {
        Lesson lesson = getLessonById(lessonId);

        if (lesson == null) {
            throw new LessonDoesNotExistsException(String.format("Lesson %s doesn't exist", lessonId));
        }

        LessonStatus status = lesson.getStatus();

        if (status == NOT_STARTED_YET) {
            lesson.setStatus(lessonStatus);

            createHistoryEvent(lesson, LessonHistoryEventType.CANCELLED);

            LessonInfo lessonInfo = lessonInfoRepository.getLessonInfoByLesson(lesson);
            lessonInfo.setAttendance(AttendanceType.CANCELLED);
        }

        return lesson;
    }



    @Override
    @Transactional
    public void setAttendance(UUID lessonId, AttendanceType attendanceType) throws LessonDoesNotExistsException {

        Lesson lesson = getLessonById(lessonId);

        if (lesson == null) {
            throw new LessonDoesNotExistsException(String.format("Lesson %s doesn't exist", lessonId));
        }

        if (attendanceType == AttendanceType.ATTENDED || attendanceType == AttendanceType.SKIPPED) {
            lesson.setStatus(LessonStatus.ENDED);
        }

        LessonInfo lessonInfo = lessonInfoRepository.getLessonInfoByLesson(lesson);
        lessonInfo.setAttendance(attendanceType);

        createHistoryEvent(lesson, LessonHistoryEventType.UPDATED);
    }

    @Override
    @Transactional
    public void setTeacherCommentForParent(String comment, UUID lessonId) throws LessonDoesNotExistsException {

        Lesson lesson = getLessonById(lessonId);

        if (lesson == null) {
            throw new LessonDoesNotExistsException(String.format("Lesson %s doesn't exist", lessonId));
        }

        LessonInfo lessonInfo = lessonInfoRepository.getLessonInfoByLesson(lesson);
        lessonInfo.setTeacherCommentForParent(comment);

        createHistoryEvent(lesson, LessonHistoryEventType.UPDATED);
    }

    @Override
    @Transactional
    public void setTeacherHomeworkComment(String comment, UUID lessonId) throws LessonDoesNotExistsException {

        Lesson lesson = getLessonById(lessonId);

        if (lesson == null) {
            throw new LessonDoesNotExistsException(String.format("Lesson %s doesn't exist", lessonId));
        }

        LessonInfo lessonInfo = lessonInfoRepository.getLessonInfoByLesson(lesson);
        lessonInfo.setTeacherComment(comment);

        createHistoryEvent(lesson, LessonHistoryEventType.UPDATED);
    }

    @Override
    @Transactional
    public Lesson setLessonDate(Timestamp date, UUID lessonId) throws LessonDoesNotExistsException {
        Lesson lesson = getLessonById(lessonId);

        if (lesson == null) {
            throw new LessonDoesNotExistsException(String.format("Lesson %s doesn't exist", lessonId));
        }

        lesson.setDate(date);

        createHistoryEvent(lesson, LessonHistoryEventType.UPDATED);

        return lesson;
    }

    @Override
    @Transactional
    public Lesson setLessonHomeworkCompletion(UUID lessonId, boolean isComplete) throws LessonDoesNotExistsException {

        Lesson lesson = getLessonById(lessonId);

        if (lesson == null) {
            throw new LessonDoesNotExistsException(String.format("Lesson %s doesn't exist", lessonId));
        }

        LessonInfo lessonInfo = lessonInfoRepository.getLessonInfoByLesson(lesson);
        lessonInfo.setHomeworkCompleted(isComplete);

        createHistoryEvent(lesson, LessonHistoryEventType.UPDATED);

        return lesson;
    }



    @Override
    @Transactional
    public void createHistoryEvent(Lesson lesson, LessonHistoryEventType historyEventType) {
        LessonHistory lessonHistory = new LessonHistory();
        lessonHistory.setLesson(lesson);
        lessonHistory.setType(historyEventType);
        lessonHistoryRepository.save(lessonHistory);
    }

    @Override
    @Transactional
    public void createLessonInfo(Lesson lesson, AttendanceType attendance) {
        LessonInfo lessonInfo = new LessonInfo();
        lessonInfo.setLesson(lesson);
        lessonInfo.setAttendance(attendance);
        lessonInfoRepository.save(lessonInfo);
    }
}
