package core.english.mse2023.dto;


import core.english.mse2023.model.dictionary.SubscriptionType;
import core.english.mse2023.state.subcription.SubscriptionCreationEvent;
import core.english.mse2023.state.subcription.SubscriptionCreationState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.statemachine.StateMachine;

import java.sql.Timestamp;

/**
 * POJO for storing temporary subscription data
 */
@ToString
@Getter
@Setter
@Builder
public class SubscriptionCreationDTO {

    private String teacherTelegramId;

    private SubscriptionType type;

    private String studentTelegramId;

    private int lessonsRest;

    private Timestamp startDate;

    private Timestamp endDate;

    private StateMachine<SubscriptionCreationState, SubscriptionCreationEvent> stateMachine;

}
