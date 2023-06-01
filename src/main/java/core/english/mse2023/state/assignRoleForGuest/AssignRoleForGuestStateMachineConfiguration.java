package core.english.mse2023.state.assignRoleForGuest;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory(name = "assignRoleForGuestStateMachineFactory")
public class AssignRoleForGuestStateMachineConfiguration extends StateMachineConfigurerAdapter<AssignRoleForGuestState, AssignRoleForGuestEvent> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<AssignRoleForGuestState, AssignRoleForGuestEvent> config) throws Exception {
        super.configure(config);
    }

    @Override
    public void configure(StateMachineStateConfigurer<AssignRoleForGuestState, AssignRoleForGuestEvent> states) throws Exception {
        states
                .withStates()
                .initial(AssignRoleForGuestState.GUEST_CHOOSING)
                .end(AssignRoleForGuestState.FINISHED)
                .states(EnumSet.allOf(AssignRoleForGuestState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<AssignRoleForGuestState, AssignRoleForGuestEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(AssignRoleForGuestState.GUEST_CHOOSING)
                .target(AssignRoleForGuestState.ROLE_CHOOSING)
                .event(AssignRoleForGuestEvent.CHOOSE_GUEST)
                .and()
                .withExternal()
                .source(AssignRoleForGuestState.ROLE_CHOOSING)
                .target(AssignRoleForGuestState.FINISHED)
                .event(AssignRoleForGuestEvent.CHOOSE_ROLE);
    }
}