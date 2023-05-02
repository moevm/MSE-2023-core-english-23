package core.english.mse2023.state.setParentForStudent;


import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory(name = "setParentForStudentStateMachineFactory")
public class SetParentForStudentStateMachineConfiguration extends StateMachineConfigurerAdapter<SetParentForStudentState, SetParentForStudentEvent> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<SetParentForStudentState, SetParentForStudentEvent> config) throws Exception {
        super.configure(config);
    }

    @Override
    public void configure(StateMachineStateConfigurer<SetParentForStudentState, SetParentForStudentEvent> states) throws Exception {
        states
                .withStates()
                .initial(SetParentForStudentState.STUDENT_CHOOSING)
                .end(SetParentForStudentState.FINISHED)
                .states(EnumSet.allOf(SetParentForStudentState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<SetParentForStudentState, SetParentForStudentEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(SetParentForStudentState.STUDENT_CHOOSING)
                .target(SetParentForStudentState.PARENT_CHOOSING)
                .event(SetParentForStudentEvent.CHOOSE_STUDENT)
                .and()
                .withExternal()
                .source(SetParentForStudentState.PARENT_CHOOSING)
                .target(SetParentForStudentState.FINISHED)
                .event(SetParentForStudentEvent.CHOOSE_PARENT);
    }
}

