package core.english.mse2023.service.tables;

import core.english.mse2023.service.enums.LessonStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LessonStatus status;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "link", nullable = false)
    private String link;

    @Column(name = "created_when", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Timestamp createdWhen;

    @Column(name = "modified_when", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Timestamp modifiedWhen;
}
