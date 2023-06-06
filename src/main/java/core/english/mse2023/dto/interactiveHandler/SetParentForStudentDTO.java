package core.english.mse2023.dto.interactiveHandler;

import core.english.mse2023.state.setParentForStudent.SetParentForStudentEvent;
import core.english.mse2023.state.setParentForStudent.SetParentForStudentState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.statemachine.StateMachine;

import java.util.UUID;

@ToString
@Getter
@Setter
@Builder
public class SetParentForStudentDTO {

    private String chosenStudent;

    private String chosenParent;

    private StateMachine<SetParentForStudentState, SetParentForStudentEvent> stateMachine;
}
