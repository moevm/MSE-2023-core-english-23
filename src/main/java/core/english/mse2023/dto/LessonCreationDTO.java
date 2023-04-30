package core.english.mse2023.dto;

import core.english.mse2023.model.dictionary.SubscriptionType;
import core.english.mse2023.state.lesson.LessonCreationEvent;
import core.english.mse2023.state.lesson.LessonCreationState;
import core.english.mse2023.state.subcription.SubscriptionCreationEvent;
import core.english.mse2023.state.subcription.SubscriptionCreationState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.statemachine.StateMachine;

import java.sql.Timestamp;
import java.util.UUID;

@ToString
@Getter
@Setter
@Builder
public class LessonCreationDTO {
    private String subscriptionId;
    private String topic;
    private String link;

    private Timestamp date;

    private StateMachine<LessonCreationState, LessonCreationEvent> stateMachine;
}
