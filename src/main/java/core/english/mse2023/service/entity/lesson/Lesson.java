package core.english.mse2023.service.entity.lesson;

import core.english.mse2023.service.entity.Subscription;
import core.english.mse2023.service.entity.user.Student;
import core.english.mse2023.service.entity.user.Teacher;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
