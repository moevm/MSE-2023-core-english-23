package core.english.mse2023.service;

import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.dictionary.AttendanceType;

import java.util.UUID;

public interface LessonInfoService {

    void setAttendance(Lesson lesson, AttendanceType attendanceType);

    void createLessonInfo(Lesson lesson);

}
