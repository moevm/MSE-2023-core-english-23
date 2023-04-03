package core.english.mse2023.repository;

import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.LessonHistory;
import core.english.mse2023.model.LessonInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface LessonHistoryRepository extends JpaRepository<LessonHistory, UUID> {
}