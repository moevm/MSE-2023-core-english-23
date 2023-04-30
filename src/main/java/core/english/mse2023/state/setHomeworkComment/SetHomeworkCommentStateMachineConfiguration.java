package core.english.mse2023.state.setHomeworkComment;


import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory(name = "setHomeworkCommentStateMachineFactory")
public class SetHomeworkCommentStateMachineConfiguration
        extends StateMachineConfigurerAdapter<SetHomeworkCommentState, SetHomeworkCommentEvent> {
    @Override
    public void configure(StateMachineConfigurationConfigurer<SetHomeworkCommentState, SetHomeworkCommentEvent> config) throws Exception {
        super.configure(config);
    }

    @Override
    public void configure(StateMachineStateConfigurer<SetHomeworkCommentState, SetHomeworkCommentEvent> states) throws Exception {
        states
                .withStates()
                .initial(SetHomeworkCommentState.WAITING_FOR_HOMEWORK)
                .end(SetHomeworkCommentState.FINISHED)
                .states(EnumSet.allOf(SetHomeworkCommentState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<SetHomeworkCommentState, SetHomeworkCommentEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(SetHomeworkCommentState.WAITING_FOR_HOMEWORK)
                .target(SetHomeworkCommentState.FINISHED)
                .event(SetHomeworkCommentEvent.ENTER_HOMEWORK);
    }
}
