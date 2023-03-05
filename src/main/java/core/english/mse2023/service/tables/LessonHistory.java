package core.english.mse2023.service.tables;

import core.english.mse2023.service.enums.LessonHistoryEventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "lesson_history")
public class LessonHistory extends BaseEntity {
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LessonHistoryEventType type;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(name = "timestamp", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp timestamp;
}

