package core.english.mse2023.service.entity.lesson;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "lesson_info")
public class LessonInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "lesson_info_id", nullable = false)
    private Long lessonInfoId;

    @Column(name = "state", nullable = false)
    private LessonState state;

    @Column(name = "teacher_comment")
    private String teacherComment;

    @Column(name = "student_comment")
    private String studentComment;

}
