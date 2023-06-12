package core.english.mse2023.handler.impl.action;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import core.english.mse2023.aop.annotation.handler.AdminRole;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.aop.annotation.handler.TeacherRole;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.dto.interactiveHandler.SetHomeworkCommentDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.exception.IllegalUserInputException;
import core.english.mse2023.handler.InteractiveHandler;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.LessonService;
import core.english.mse2023.state.setHomeworkComment.SetHomeworkCommentEvent;
import core.english.mse2023.state.setHomeworkComment.SetHomeworkCommentState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@InlineButtonType
@AdminRole
@TeacherRole
@RequiredArgsConstructor
public class SetHomeworkCommentHandler implements InteractiveHandler {

    @Value("${messages.handlers.set-homework-comment.start}")
    private String startText;

    @Value("${messages.handlers.set-homework-comment.success}")
    private String successText;


    private final LessonService lessonService;

    // This cache works in manual mode. It means - no evictions was configured
    private final Cache<String, SetHomeworkCommentDTO> setHomeworkCommentCache = Caffeine.newBuilder()
            .build();

    @Qualifier("setHomeworkCommentStateMachineFactory")
    private final StateMachineFactory<SetHomeworkCommentState, SetHomeworkCommentEvent> stateMachineFactory;

    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {

        InlineButtonDTO inlineButtonDTO = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        StateMachine<SetHomeworkCommentState, SetHomeworkCommentEvent> stateMachine =
                stateMachineFactory.getStateMachine();
        stateMachine.start();

        SetHomeworkCommentDTO dto = SetHomeworkCommentDTO.builder()
                .lessonId(UUID.fromString(inlineButtonDTO.getData()))
                .stateMachine(stateMachine)
                .build();

        setHomeworkCommentCache.put(update.getCallbackQuery().getFrom().getId().toString(), dto);

        // Sending start message
        SendMessage message;

        message = SendMessage.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                .text(startText)
                .build();

        return List.of(message, new AnswerCallbackQuery(update.getCallbackQuery().getId()));
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.SET_HOMEWORK_COMMENT;
    }

    @Override
    public List<PartialBotApiMethod<?>> update(Update update, UserRole role) throws IllegalUserInputException, IllegalStateException {

        SetHomeworkCommentDTO dto = setHomeworkCommentCache.getIfPresent(update.getMessage().getFrom().getId().toString());

        if (dto == null) {
            log.error("DTO instance wasn't created yet! Cannot continue. User id: {}", update.getMessage().getFrom().getId());
            throw new IllegalStateException("There's no DTO created to receive message for parent.");
        }

        var stateMachine = dto.getStateMachine();

        if (stateMachine.getState().getId() != SetHomeworkCommentState.WAITING_FOR_HOMEWORK) {
            log.error("Update method has been called, but interactive handler has the wrong state. User id: {}", update.getMessage().getFrom().getId());
            throw new IllegalStateException(String.format("WAITING_FOR_COMMENT state expected. Current state: %s", stateMachine.getState().toString()));
        }

        dto.setComment(update.getMessage().getText());

        lessonService.setTeacherHomeworkComment(dto.getComment(), dto.getLessonId());

        stateMachine.sendEvent(SetHomeworkCommentEvent.ENTER_HOMEWORK);

        // Sending buttons with students. Data from them will be used in the next state
        SendMessage sendMessage = SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(successText)
                .build();

        return List.of(sendMessage);
    }

    @Override
    public void removeFromCacheBy(String id) {
        if (setHomeworkCommentCache.getIfPresent(id) != null)
            setHomeworkCommentCache.invalidate(id);
    }

    @Override
    public boolean hasFinished(String id) {
        var dto = setHomeworkCommentCache.getIfPresent(id);

        boolean result = true;

        if (dto != null) {
            result = dto.getStateMachine().isComplete();
        }

        return result;
    }

    @Override
    public int getCurrentStateIndex(String id) {
        var dto = setHomeworkCommentCache.getIfPresent(id);

        int result = -1;

        if (dto != null) {
            result = dto.getStateMachine().getState().getId().getIndex();
        }

        return result;
    }
}
