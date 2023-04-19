package core.english.mse2023.state.setCommentForTeacher;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory(name = "setCommentForParentStateMachineFactory")
public class SetCommentForTeacherStateMachineConfiguration extends StateMachineConfigurerAdapter<SetCommentForTeacherState, SetCommentForTeacherEvent> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<SetCommentForTeacherState, SetCommentForTeacherEvent> config) throws Exception {
        super.configure(config);
    }

    @Override
    public void configure(StateMachineStateConfigurer<SetCommentForTeacherState, SetCommentForTeacherEvent> states) throws Exception {
        states
                .withStates()
                .initial(SetCommentForTeacherState.WAITING_FOR_COMMENT)
                .end(SetCommentForTeacherState.FINISHED)
                .states(EnumSet.allOf(SetCommentForTeacherState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<SetCommentForTeacherState, SetCommentForTeacherEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(SetCommentForTeacherState.WAITING_FOR_COMMENT)
                .target(SetCommentForTeacherState.FINISHED)
                .event(SetCommentForTeacherEvent.ENTER_COMMENT);
    }
}
