package core.english.mse2023.service.tables;

import core.english.mse2023.service.enums.AttendanceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "lesson_info")
public class LessonInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "attendance", nullable = false)
    private AttendanceType attendance;

    @OneToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @OneToOne
    @JoinColumn(name = "family_id")
    private Family family;

    @Column(name = "family_comment", nullable = false)
    private String familyComment;

    @Column(name = "teacher_comment", nullable = false)
    private String teacherComment;

    @Column(name = "created_when", nullable = false)
    private Date createdWhen;

    @Column(name = "modified_when", nullable = false)
    private Date modifiedWhen;

}
