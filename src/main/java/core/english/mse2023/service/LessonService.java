package core.english.mse2023.service;

import core.english.mse2023.exception.LessonAlreadyFinishedException;
import core.english.mse2023.exception.LessonDoesNotExistsException;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.LessonInfo;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.dictionary.AttendanceType;
import core.english.mse2023.model.dictionary.LessonHistoryEventType;
import core.english.mse2023.model.dictionary.LessonStatus;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface LessonService {

    /**
     * Searches for Lesson with certain UUID
     * @param id Lesson UUID in the database
     * @return Null - if lesson doesn't exist; Lesson object - if it does exist
     */
    Lesson getLessonById(UUID id);

    /**
     * Searches for LessonInfo with certain Lesson UUID
     * @param lessonId Lesson UUID in the database
     * @return Null - if lesson with given UUID doesn't exist; LessonInfo object - if it does exist
     */
    LessonInfo getLessonInfoByLessonId(UUID lessonId);

    /**
     * Searches for all Lessons in certain subscription
     * @param subscriptionId Subscription UUID in the database
     * @return List of found Lessons. Can be empty.
     */
    List<Lesson> getAllLessonsForSubscription(UUID subscriptionId);


    /**
     * Creates Lesson with minimum data
     * @param subscription Subscription object to add lesson to
     * @param topic New lesson's topic
     * @return Newly created lesson
     */
    Lesson createLesson(Subscription subscription, String topic);

    /**
     * Finishes lesson
     * @param lessonId Lesson UUID in the database
     * @return Lesson that has been finished
     * @throws LessonDoesNotExistsException If there is no lesson with given UUID
     * @throws LessonAlreadyFinishedException If lesson has already been finished
     */
    Lesson finishLesson(UUID lessonId) throws LessonDoesNotExistsException, LessonAlreadyFinishedException;

    /**
     * Cancels lesson
     * @param lessonId Lesson UUID in the database
     * @param lessonStatus Status to set to the lesson (needed because there are multiple CANCELLED statuses)
     * @return Cancelled lesson
     * @throws LessonDoesNotExistsException If there is no lesson with given UUID
     */
    Lesson cancelLesson(UUID lessonId, LessonStatus lessonStatus) throws LessonDoesNotExistsException;



    void setAttendance(UUID lessonId, AttendanceType attendanceType) throws LessonDoesNotExistsException;

    void setTeacherCommentForParent(String comment, UUID lessonId) throws LessonDoesNotExistsException;

    void setTeacherHomeworkComment(String comment, UUID lessonId) throws LessonDoesNotExistsException;

    Lesson setLessonDate(Timestamp date, UUID lessonId) throws LessonDoesNotExistsException;

    Lesson setLessonHomeworkCompletion(UUID lessonId, boolean isComplete) throws LessonDoesNotExistsException;



    void createHistoryEvent(Lesson lesson, LessonHistoryEventType historyEventType);

    void createLessonInfo(Lesson lesson, AttendanceType attendance);
}
