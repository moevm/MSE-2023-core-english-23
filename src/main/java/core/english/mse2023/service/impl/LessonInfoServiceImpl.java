package core.english.mse2023.service.impl;

import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.LessonInfo;
import core.english.mse2023.model.dictionary.AttendanceType;
import core.english.mse2023.model.dictionary.LessonStatus;
import core.english.mse2023.repository.LessonInfoRepository;
import core.english.mse2023.repository.LessonRepository;
import core.english.mse2023.service.LessonInfoService;
import core.english.mse2023.service.LessonService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LessonInfoServiceImpl implements LessonInfoService {

    private final LessonInfoRepository lessonInfoRepository;

    @Override
    @Transactional
    public void setAttendance(Lesson lesson, AttendanceType attendanceType) {
        LessonInfo lessonInfo = lessonInfoRepository.getLessonInfoByLesson(lesson);

        lessonInfo.setAttendance(attendanceType);

        lessonInfoRepository.save(lessonInfo);
    }

    @Override
    @Transactional
    public void createLessonInfo(Lesson lesson) {
        LessonInfo lessonInfo = new LessonInfo();
        lessonInfo.setAttendance(AttendanceType.NOT_YET_ATTENDED);
        lessonInfo.setLesson(lesson);

        lessonInfoRepository.save(lessonInfo);
    }
}
