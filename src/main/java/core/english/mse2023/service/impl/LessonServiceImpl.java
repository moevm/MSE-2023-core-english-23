package core.english.mse2023.service.impl;

import core.english.mse2023.exception.LessonAlreadyFinishedException;
import core.english.mse2023.exception.LessonDateOutsideSubscriptionException;
import core.english.mse2023.exception.NoSuchLessonException;
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
import core.english.mse2023.service.LessonService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static core.english.mse2023.model.dictionary.LessonStatus.ENDED;
import static core.english.mse2023.model.dictionary.LessonStatus.NOT_STARTED_YET;

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
    public List<LessonInfo> getAllLessonInfosWithUnfinishedTask() {
        return lessonInfoRepository.getAllByHomeworkCompletedFalse();
    }


    @Override
    @Transactional
    public Lesson createLesson(Subscription subscription, String topic) {
        return createLesson(subscription, null, topic, null);
    }

    @Transactional
    @Override
    public Lesson createLesson(Subscription subscription, Timestamp date, String topic, String link) {
        Lesson lesson = Lesson.builder()
                .status((date != null && date.after(new Date())) ? ENDED : NOT_STARTED_YET)
                .subscription(subscription)
                .topic(topic)
                .link(link)
                .date(date)
                .build();

        lessonRepository.save(lesson);

        createHistoryEvent(lesson, LessonHistoryEventType.CREATED);
        createLessonInfo(lesson, AttendanceType.NOT_YET_ATTENDED);

        return lesson;
    }

    @Override
    @Transactional
    public Lesson finishLesson(UUID lessonId) {
        Lesson lesson = getLessonById(lessonId);

        if (lesson == null) {
            throw new NoSuchLessonException(String.format("Lesson %s doesn't exist", lessonId));
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
    public Lesson cancelLesson(UUID lessonId, LessonStatus lessonStatus) {
        Lesson lesson = getLessonById(lessonId);

        if (lesson == null) {
            throw new NoSuchLessonException(String.format("Lesson %s doesn't exist", lessonId));
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
    public void setAttendance(UUID lessonId, AttendanceType attendanceType) {

        Lesson lesson = getLessonById(lessonId);

        if (lesson == null) {
            throw new NoSuchLessonException(String.format("Lesson %s doesn't exist", lessonId));
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
    public void setTeacherCommentForParent(String comment, UUID lessonId) {

        Lesson lesson = getLessonById(lessonId);

        if (lesson == null) {
            throw new NoSuchLessonException(String.format("Lesson %s doesn't exist", lessonId));
        }

        LessonInfo lessonInfo = lessonInfoRepository.getLessonInfoByLesson(lesson);
        lessonInfo.setTeacherCommentForParent(comment);

        createHistoryEvent(lesson, LessonHistoryEventType.UPDATED);
    }

    @Override
    @Transactional
    public void setTeacherHomeworkComment(String comment, UUID lessonId) {

        Lesson lesson = getLessonById(lessonId);

        if (lesson == null) {
            throw new NoSuchLessonException(String.format("Lesson %s doesn't exist", lessonId));
        }

        LessonInfo lessonInfo = lessonInfoRepository.getLessonInfoByLesson(lesson);
        lessonInfo.setTeacherComment(comment);

        createHistoryEvent(lesson, LessonHistoryEventType.UPDATED);
    }

    @Override
    @Transactional
    public Lesson setLessonDate(Timestamp date, UUID lessonId) {
        Lesson lesson = getLessonById(lessonId);

        if (lesson == null) {
            throw new NoSuchLessonException(String.format("Lesson %s doesn't exist", lessonId));
        }

        // Если не попадает в пределы подписки
        if (date.after(lesson.getSubscription().getEndDate()) || date.before(lesson.getSubscription().getStartDate())) {
            throw new LessonDateOutsideSubscriptionException();
        }

        lesson.setDate(date);

        if (lesson.getDate().before(new Date())) {
            lesson.setStatus(ENDED);
        }

        createHistoryEvent(lesson, LessonHistoryEventType.UPDATED);

        return lesson;
    }

    @Override
    @Transactional
    public Lesson setLessonHomeworkCompletion(UUID lessonId, boolean isComplete) {

        Lesson lesson = getLessonById(lessonId);

        if (lesson == null) {
            throw new NoSuchLessonException(String.format("Lesson %s doesn't exist", lessonId));
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
