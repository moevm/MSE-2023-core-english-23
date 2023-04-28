package core.english.mse2023.dto;

import core.english.mse2023.state.setLessonDate.SetLessonDateEvent;
import core.english.mse2023.state.setLessonDate.SetLessonDateState;
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
public class SetLessonDateDTO {

    private Timestamp date;
    private Timestamp subscriptionStartDate;
    private Timestamp subscriptionEndDate;

    private UUID lessonId;

    private StateMachine<SetLessonDateState, SetLessonDateEvent> stateMachine;

}
