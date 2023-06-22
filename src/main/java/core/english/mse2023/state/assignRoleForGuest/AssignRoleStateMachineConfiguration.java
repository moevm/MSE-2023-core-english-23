package core.english.mse2023.state.assignRoleForGuest;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory(name = "assignRoleStateMachineFactory")
public class AssignRoleStateMachineConfiguration extends StateMachineConfigurerAdapter<AssignRoleState, AssignRoleEvent> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<AssignRoleState, AssignRoleEvent> config) throws Exception {
        super.configure(config);
    }

    @Override
    public void configure(StateMachineStateConfigurer<AssignRoleState, AssignRoleEvent> states) throws Exception {
        states
                .withStates()
                .initial(AssignRoleState.USER_CHOOSING)
                .end(AssignRoleState.FINISHED)
                .states(EnumSet.allOf(AssignRoleState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<AssignRoleState, AssignRoleEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(AssignRoleState.USER_CHOOSING)
                .target(AssignRoleState.ROLE_CHOOSING)
                .event(AssignRoleEvent.CHOOSE_USER)
                .and()

                .withExternal()
                .source(AssignRoleState.ROLE_CHOOSING)
                .target(AssignRoleState.FINISHED)
                .event(AssignRoleEvent.CHOOSE_ROLE);
    }
}