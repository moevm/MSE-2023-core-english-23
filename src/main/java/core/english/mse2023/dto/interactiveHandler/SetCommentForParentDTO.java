package core.english.mse2023.dto.interactiveHandler;

import core.english.mse2023.state.setCommentForTeacher.SetCommentForTeacherEvent;
import core.english.mse2023.state.setCommentForTeacher.SetCommentForTeacherState;
import core.english.mse2023.state.subcription.SubscriptionCreationEvent;
import core.english.mse2023.state.subcription.SubscriptionCreationState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.statemachine.StateMachine;

import java.util.UUID;

@ToString
@Getter
@Setter
@Builder
public class SetCommentForParentDTO {

    private String comment;

    private UUID lessonId;

    private StateMachine<SetCommentForTeacherState, SetCommentForTeacherEvent> stateMachine;

}
