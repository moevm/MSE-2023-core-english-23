package core.english.mse2023.state.setLessonDate;


import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory(name = "setLessonDateStateMachineFactory")
public class SetLessonDateStateMachineConfiguration extends StateMachineConfigurerAdapter<SetLessonDateState, SetLessonDateEvent> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<SetLessonDateState, SetLessonDateEvent> config) throws Exception {
        super.configure(config);
    }

    @Override
    public void configure(StateMachineStateConfigurer<SetLessonDateState, SetLessonDateEvent> states) throws Exception {
        states
                .withStates()
                .initial(SetLessonDateState.WAITING_FOR_DATE)
                .end(SetLessonDateState.FINISHED)
                .states(EnumSet.allOf(SetLessonDateState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<SetLessonDateState, SetLessonDateEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(SetLessonDateState.WAITING_FOR_DATE)
                .target(SetLessonDateState.FINISHED)
                .event(SetLessonDateEvent.ENTER_DATE);
    }
}
