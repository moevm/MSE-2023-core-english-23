package core.english.mse2023.state.createLesson;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory(name = "lessonCreationStateMachineFactory")
public class LessonCreationStateMachineConfiguration
        extends StateMachineConfigurerAdapter<LessonCreationState, LessonCreationEvent> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<LessonCreationState, LessonCreationEvent> config) throws Exception {
        super.configure(config);
    }

    @Override
    public void configure(StateMachineStateConfigurer<LessonCreationState, LessonCreationEvent> states) throws Exception {
        states
                .withStates()
                .initial(LessonCreationState.DATE_CHOOSING)
                .end(LessonCreationState.CREATED)
                .states(EnumSet.allOf(LessonCreationState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<LessonCreationState, LessonCreationEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(LessonCreationState.DATE_CHOOSING)
                .target(LessonCreationState.TOPIC_CHOOSING)
                .event(LessonCreationEvent.CHOOSE_DATE)
                .and()

                .withExternal()
                .source(LessonCreationState.TOPIC_CHOOSING)
                .target(LessonCreationState.LINK_CHOOSING)
                .event(LessonCreationEvent.CHOOSE_TOPIC)
                .and()

                .withExternal()
                .source(LessonCreationState.LINK_CHOOSING)
                .target(LessonCreationState.CREATED)
                .event(LessonCreationEvent.CHOOSE_LINK);
    }


}
