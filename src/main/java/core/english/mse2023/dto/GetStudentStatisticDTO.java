package core.english.mse2023.dto;

import core.english.mse2023.model.Subscription;
import core.english.mse2023.state.getStudentStatistic.GetStudentStatisticEvent;
import core.english.mse2023.state.getStudentStatistic.GetStudentStatisticState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.statemachine.StateMachine;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class GetStudentStatisticDTO {

    private Subscription studentSubscription;

    private Timestamp startDate;

    private Timestamp endDate;

    private StateMachine<GetStudentStatisticState, GetStudentStatisticEvent> stateMachine;

}
