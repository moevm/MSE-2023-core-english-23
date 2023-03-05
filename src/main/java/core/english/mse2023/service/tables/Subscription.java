package core.english.mse2023.service.tables;

import core.english.mse2023.service.enums.SubscriptionStatus;
import core.english.mse2023.service.enums.SubscriptionType;
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
@Entity(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "type", nullable = false)
    private SubscriptionType type;

    @Column(name = "status", nullable = false)
    private SubscriptionStatus status;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @Column(name = "lessons_rest", nullable = false)
    private int lessonsRest;

    @Column(name = "start_date", nullable = false, updatable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "created_when", nullable = false)
    private Date createdWhen;

    @Column(name = "modified_when", nullable = false)
    private Date modifiedWhen;
}
