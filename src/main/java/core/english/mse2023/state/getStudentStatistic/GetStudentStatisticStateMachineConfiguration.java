package core.english.mse2023.state.getStudentStatistic;

import core.english.mse2023.state.createLesson.LessonCreationEvent;
import core.english.mse2023.state.createLesson.LessonCreationState;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory(name = "getStudentStatisticStateMachineFactory")
public class GetStudentStatisticStateMachineConfiguration
        extends StateMachineConfigurerAdapter<GetStudentStatisticState, GetStudentStatisticEvent> {
    @Override
    public void configure(StateMachineConfigurationConfigurer<GetStudentStatisticState, GetStudentStatisticEvent> config) throws Exception {
        super.configure(config);
    }

    @Override
    public void configure(StateMachineStateConfigurer<GetStudentStatisticState, GetStudentStatisticEvent> states) throws Exception {
        states
                .withStates()
                .initial(GetStudentStatisticState.STUDENT_CHOOSING)
                .end(GetStudentStatisticState.CREATED)
                .states(EnumSet.allOf(GetStudentStatisticState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<GetStudentStatisticState, GetStudentStatisticEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(GetStudentStatisticState.STUDENT_CHOOSING)
                .target(GetStudentStatisticState.ENTERING_INTERVAL)
                .event(GetStudentStatisticEvent.CHOOSE_STUDENT)
                .and()

                .withExternal()
                .source(GetStudentStatisticState.ENTERING_INTERVAL)
                .target(GetStudentStatisticState.CREATED)
                .event(GetStudentStatisticEvent.ENTER_INTERVAL);
    }
}
