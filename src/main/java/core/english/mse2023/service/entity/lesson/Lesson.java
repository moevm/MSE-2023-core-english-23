package core.english.mse2023.service.entity.lesson;

import core.english.mse2023.service.entity.Subscription;
import core.english.mse2023.service.entity.user.Student;
import core.english.mse2023.service.entity.user.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "lesson_id", nullable = false)
    private Long lessonId;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToOne
    @JoinColumn(name = "lesson_info_id")
    private LessonInfo lessonInfo;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(name = "topic")
    private String topic;

    @Column(name = "link")
    private String link;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;

}
