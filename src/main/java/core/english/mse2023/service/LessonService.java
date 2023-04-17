package core.english.mse2023.service;

import core.english.mse2023.exception.LessonAlreadyFinishedException;
import core.english.mse2023.exception.LessonHasNotStartedYetException;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.LessonInfo;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.dictionary.AttendanceType;
import core.english.mse2023.model.dictionary.LessonStatus;

import java.util.List;
import java.util.UUID;

public interface LessonService {

    Lesson getLessonById(UUID id);

    LessonInfo getLessonInfoByLessonId(UUID lessonId);

    void cancelLessonsFromSubscription(UUID subscriptionId);

    List<Lesson> getAllLessonsForSubscription(UUID subscriptionId);

    void createBaseLessonsForSubscription(Subscription subscription);

    Lesson createLesson(Subscription subscription, String topic);

    void setAttendance(UUID lessonId, AttendanceType attendanceType);

    Lesson cancelLesson(UUID lessonId);

    void setTeacherCommentForParent(String comment, UUID lessonId);

    Lesson finishLesson(UUID lessonId) throws LessonAlreadyFinishedException;

}
