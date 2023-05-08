package core.english.mse2023.model;

import core.english.mse2023.model.dictionary.LessonStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "lesson")
public class Lesson extends BaseEntity {

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LessonStatus status;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "topic")
    private String topic;

    @Column(name = "link")
    private String link;
}
