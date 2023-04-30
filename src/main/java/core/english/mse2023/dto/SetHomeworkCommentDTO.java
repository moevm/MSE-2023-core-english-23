package core.english.mse2023.dto;

import core.english.mse2023.state.setHomeworkComment.SetHomeworkCommentEvent;
import core.english.mse2023.state.setHomeworkComment.SetHomeworkCommentState;
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
public class SetHomeworkCommentDTO {
    private String comment;

    private UUID lessonId;

    private StateMachine<SetHomeworkCommentState, SetHomeworkCommentEvent> stateMachine;
}
