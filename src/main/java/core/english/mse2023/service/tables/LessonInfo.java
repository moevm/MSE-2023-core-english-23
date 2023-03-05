package core.english.mse2023.service.tables;

import core.english.mse2023.service.enums.AttendanceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "lesson_info")
public class LessonInfo extends BaseEntity {

    @Column(name = "attendance", nullable = false)
    @Enumerated(EnumType.STRING)
    private AttendanceType attendance;

    @OneToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @OneToOne
    @JoinColumn(name = "family_id")
    private Family family;

    @Column(name = "family_comment")
    private String familyComment;

    @Column(name = "teacher_comment")
    private String teacherComment;

}
