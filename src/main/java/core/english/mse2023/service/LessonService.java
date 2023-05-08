package core.english.mse2023.service;

import core.english.mse2023.exception.LessonAlreadyFinishedException;
import core.english.mse2023.exception.LessonDateOutsideSubscriptionException;
import core.english.mse2023.exception.NoSuchLessonException;
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
     * Creates lesson with full data
     * @param subscription Subscription object to add lesson to
     * @param date Lesson start date
     * @param topic New lesson's topic
     * @param link Link to the classroom for the lesson
     * @return Newly created lesson
     */
    Lesson createLesson(Subscription subscription, Timestamp date, String topic, String link);

    /**
     * Finishes lesson
     * @param lessonId Lesson UUID in the database
     * @return Lesson that has been finished
     * @throws NoSuchLessonException If there is no lesson with given UUID
     * @throws LessonAlreadyFinishedException If lesson has already been finished
     */
    Lesson finishLesson(UUID lessonId);

    /**
     * Cancels lesson
     * @param lessonId Lesson UUID in the database
     * @param lessonStatus Status to set to the lesson (needed because there are multiple CANCELLED statuses)
     * @return Cancelled lesson
     * @throws NoSuchLessonException If there is no lesson with given UUID
     */
    Lesson cancelLesson(UUID lessonId, LessonStatus lessonStatus);


    /**
     * Sets attendance in lesson
     * @param lessonId UUID of the lesson to set attendance to
     * @param attendanceType Type of attendance
     * @throws NoSuchLessonException If there is no lesson with given UUID
     */
    void setAttendance(UUID lessonId, AttendanceType attendanceType);

    /**
     * Sets comment for parent to the lesson
     * @param comment Text of the comment
     * @param lessonId UUID of the lesson
     * @throws NoSuchLessonException If there is no lesson with given UUID
     */
    void setTeacherCommentForParent(String comment, UUID lessonId);

    /**
     * Sets homework to the lesson
     * @param comment Text of the homework
     * @param lessonId UUID of the lesson
     * @throws NoSuchLessonException If there is no lesson with given UUID
     */
    void setTeacherHomeworkComment(String comment, UUID lessonId);

    /**
     * Sets lesson start date
     * @param date Date of lesson start
     * @param lessonId UUID of the lesson
     * @return Changed lesson
     * @throws NoSuchLessonException If there is no lesson with given UUID
     * @throws LessonDateOutsideSubscriptionException If given date was outside subscription boundaries
     */
    Lesson setLessonDate(Timestamp date, UUID lessonId);

    /**
     * Sets if lesson's homework is complete
     * @param lessonId UUID of the lesson
     * @param isComplete Is lesson's homework complete
     * @return Changed lesson
     * @throws NoSuchLessonException If there is no lesson with given UUID
     */
    Lesson setLessonHomeworkCompletion(UUID lessonId, boolean isComplete);


    /**
     * Creates lesson history event recording
     * @param lesson Lesson object to associate recording to
     * @param historyEventType Type of event occurred
     */
    void createHistoryEvent(Lesson lesson, LessonHistoryEventType historyEventType);

    /**
     * Creates lesson info recording
     * @param lesson Lesson object to associate recording to
     * @param attendance Attendance type
     */
    void createLessonInfo(Lesson lesson, AttendanceType attendance);

}
