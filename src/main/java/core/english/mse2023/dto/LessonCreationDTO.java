package core.english.mse2023.dto;

import core.english.mse2023.model.Subscription;
import core.english.mse2023.state.createLesson.LessonCreationEvent;
import core.english.mse2023.state.createLesson.LessonCreationState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.statemachine.StateMachine;

import java.sql.Timestamp;

@ToString
@Getter
@Setter
@Builder
public class LessonCreationDTO {

    private Subscription subscription;

    private String topic;

    private String link;

    private Timestamp date;

    private StateMachine<LessonCreationState, LessonCreationEvent> stateMachine;
}
