package core.english.mse2023.state.subcription;

import core.english.mse2023.model.dictionary.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory(name = "subscriptionCreationStateMachineFactory")
public class SubscriptionCreationStateMachineConfiguration extends StateMachineConfigurerAdapter<SubscriptionCreationState, SubscriptionCreationEvent> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<SubscriptionCreationState, SubscriptionCreationEvent> config) throws Exception {
        super.configure(config);
    }

    @Override
    public void configure(StateMachineStateConfigurer<SubscriptionCreationState, SubscriptionCreationEvent> states) throws Exception {
        states
                .withStates()
                .initial(SubscriptionCreationState.START_DATE_CHOOSING)
                .end(SubscriptionCreationState.CREATED)
                .junction(SubscriptionCreationState.JUNCTION_NODE)
                .states(EnumSet.allOf(SubscriptionCreationState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<SubscriptionCreationState, SubscriptionCreationEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(SubscriptionCreationState.START_DATE_CHOOSING)
                .target(SubscriptionCreationState.END_DATE_CHOOSING)
                .event(SubscriptionCreationEvent.CHOOSE_START_DATE)
                .and()

                .withExternal()
                .source(SubscriptionCreationState.END_DATE_CHOOSING)
                .target(SubscriptionCreationState.LESSON_AMOUNT_ENTERING)
                .event(SubscriptionCreationEvent.CHOOSE_END_DATE)
                .and()

                .withExternal()
                .source(SubscriptionCreationState.LESSON_AMOUNT_ENTERING)
                .target(SubscriptionCreationState.STUDENT_CHOOSING)
                .event(SubscriptionCreationEvent.ENTER_LESSON_AMOUNT)
                .and()

                .withExternal()
                .source(SubscriptionCreationState.STUDENT_CHOOSING)
                .target(SubscriptionCreationState.JUNCTION_NODE)
                .event(SubscriptionCreationEvent.CHOOSE_STUDENT)
                .and()

                .withJunction()
                .source(SubscriptionCreationState.JUNCTION_NODE)
                .first(SubscriptionCreationState.CREATED, (context) -> {
                    UserRole userRole = context.getExtendedState().get("UserRole", UserRole.class);
                    return userRole == UserRole.TEACHER;
                })
                .last(SubscriptionCreationState.TEACHER_CHOOSING)
                .and()

                .withExternal()
                .source(SubscriptionCreationState.TEACHER_CHOOSING)
                .target(SubscriptionCreationState.CREATED)
                .event(SubscriptionCreationEvent.CHOOSE_TEACHER)
                .and();
    }

    @Bean
    public Guard<SubscriptionCreationState, SubscriptionCreationEvent> isTeacherGuard() {
        return new Guard<SubscriptionCreationState, SubscriptionCreationEvent>() {
            @Override
            public boolean evaluate(StateContext<SubscriptionCreationState, SubscriptionCreationEvent> context) {

                return false;
            }
        };
    }
}
