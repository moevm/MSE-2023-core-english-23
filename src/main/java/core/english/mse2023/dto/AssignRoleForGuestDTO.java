package core.english.mse2023.dto;

import core.english.mse2023.state.assignRoleForGuest.AssignRoleForGuestEvent;
import core.english.mse2023.state.assignRoleForGuest.AssignRoleForGuestState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.statemachine.StateMachine;

@ToString
@Getter
@Setter
@Builder
public class AssignRoleForGuestDTO {

    private String chosenGuest;

    private String chosenRole;

    private StateMachine<AssignRoleForGuestState, AssignRoleForGuestEvent> stateMachine;
}
