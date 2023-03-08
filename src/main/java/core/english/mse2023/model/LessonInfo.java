package core.english.mse2023.model;

import core.english.mse2023.model.dictionary.AttendanceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "lessonInfo")
public class LessonInfo extends BaseEntity {

    @Column(name = "attendance", nullable = false)
    @Enumerated(EnumType.STRING)
    private AttendanceType attendance;

    @OneToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name="score", nullable = false)
    private int score;

    @Column(name = "family_comment")
    private String familyComment;

    @Column(name = "teacher_comment")
    private String teacherComment;

}
