package core.english.mse2023.model;

import core.english.mse2023.model.dictionary.LessonStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "lesson")
public class Lesson extends BaseEntity {

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LessonStatus status;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(name = "date", nullable = false)
    private Timestamp date;

    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "link", nullable = false)
    private String link;
}
