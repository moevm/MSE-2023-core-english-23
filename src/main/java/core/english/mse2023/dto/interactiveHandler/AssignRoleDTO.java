package core.english.mse2023.dto.interactiveHandler;

import core.english.mse2023.state.assignRoleForGuest.AssignRoleEvent;
import core.english.mse2023.state.assignRoleForGuest.AssignRoleState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.statemachine.StateMachine;

@ToString
@Getter
@Setter
@Builder
public class AssignRoleDTO {

    private String chosenGuest;

    private String chosenRole;

    private StateMachine<AssignRoleState, AssignRoleEvent> stateMachine;
}
