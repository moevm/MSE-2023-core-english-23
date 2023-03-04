package core.english.mse2023.service.entity;

import core.english.mse2023.service.entity.user.Student;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subscription_id", nullable = false)
    private Long subscriptionId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "lessons_rest", nullable = false)
    private Integer lessonsRest;

    @Column(name = "start_date", nullable = false, updatable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;
}
