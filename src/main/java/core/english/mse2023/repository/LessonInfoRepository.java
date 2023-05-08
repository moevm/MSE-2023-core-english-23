package core.english.mse2023.repository;

import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.LessonInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LessonInfoRepository extends JpaRepository<LessonInfo, UUID> {
    LessonInfo getLessonInfoByLesson(Lesson lesson);

    LessonInfo getLessonInfoByLessonId(UUID lessonId);
}
