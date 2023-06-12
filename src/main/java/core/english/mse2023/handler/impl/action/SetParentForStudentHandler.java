package core.english.mse2023.handler.impl.action;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import core.english.mse2023.aop.annotation.handler.AdminRole;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.component.MessageTextMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.dto.interactiveHandler.SetParentForStudentDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.exception.IllegalUserInputException;
import core.english.mse2023.handler.InteractiveHandler;
import core.english.mse2023.model.Family;
import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.UserService;
import core.english.mse2023.state.setParentForStudent.SetParentForStudentEvent;
import core.english.mse2023.state.setParentForStudent.SetParentForStudentState;
import core.english.mse2023.util.builder.InlineKeyboardBuilder;
import core.english.mse2023.util.utilities.TelegramInlineButtonsUtils;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@TextCommandType
@AdminRole
@RequiredArgsConstructor
public class SetParentForStudentHandler implements InteractiveHandler {

    @Value("${messages.handlers.set-parent-for-student.choose-student}")
    private String chooseStudentText;

    @Value("${messages.handlers.set-parent-for-student.choose-parent}")
    private String chooseParentText;

    @Value("${messages.handlers.set-parent-for-student.success}")
    private String successText;

    private final MessageTextMaker messageTextMaker;

    private final UserService userService;

    // This cache works in manual mode. It means - no evictions was configured
    private final Cache<String, SetParentForStudentDTO> setParentForStudentCache = Caffeine.newBuilder()
            .build();

    @Qualifier("setParentForStudentStateMachineFactory")
    private final StateMachineFactory<SetParentForStudentState, SetParentForStudentEvent> stateMachineFactory;


    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {
        StateMachine<SetParentForStudentState, SetParentForStudentEvent> stateMachine =
                stateMachineFactory.getStateMachine();
        stateMachine.start();

        SetParentForStudentDTO dto = SetParentForStudentDTO.builder()
                .stateMachine(stateMachine)
                .build();

        setParentForStudentCache.put(update.getMessage().getFrom().getId().toString(), dto);

        List<User> students = userService.getAllStudents();

        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(chooseStudentText)
                .replyMarkup(getUsersButtons(students, stateMachine.getState().getId().getIndex()))
                .build();

        return List.of(message);
    }

    @Override
    public List<PartialBotApiMethod<?>> update(Update update, UserRole role) throws IllegalUserInputException, IllegalStateException {
        SetParentForStudentDTO dto = setParentForStudentCache.getIfPresent(update.getCallbackQuery().getFrom().getId().toString());

        if (dto == null) {
            log.error("DTO instance wasn't created yet! Cannot continue. User id: {}", update.getCallbackQuery().getFrom().getId());
            throw new IllegalStateException("There's no DTO created to receive message for parent.");
        }

        var stateMachine = dto.getStateMachine();

        InlineButtonDTO inlineButtonDTO = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        List<PartialBotApiMethod<?>> actions = new ArrayList<>();

        if (stateMachine.getState().getId() == SetParentForStudentState.STUDENT_CHOOSING) {

            dto.setChosenStudent(inlineButtonDTO.getData());

            stateMachine.sendEvent(SetParentForStudentEvent.CHOOSE_STUDENT);

            List<User> parents = userService.getAllParents();

            actions.add(SendMessage.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .text(chooseParentText)
                    .replyMarkup(getUsersButtons(parents, stateMachine.getState().getId().getIndex()))
                    .build());
            actions.add(new AnswerCallbackQuery(update.getCallbackQuery().getId()));
        } else if (stateMachine.getState().getId() == SetParentForStudentState.PARENT_CHOOSING) {

            dto.setChosenParent(inlineButtonDTO.getData());

            stateMachine.sendEvent(SetParentForStudentEvent.CHOOSE_PARENT);

            Family family = userService.setParentForStudent(dto.getChosenStudent(), dto.getChosenParent());

            actions.add(SendMessage.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .text(String.format(successText,
                            messageTextMaker.userDataPatternMessageText(family.getStudent().getName(), family.getStudent().getLastName()),
                            messageTextMaker.userDataPatternMessageText(family.getParent().getName(), family.getParent().getLastName())
                    ))
                    .build());
            actions.add(new AnswerCallbackQuery(update.getCallbackQuery().getId()));

        } else {
            log.error("Unexpected state. State: {}", stateMachine.getState().toString());
            throw new IllegalStateException(String.format("Unexpected state. State: %s", stateMachine.getState().toString()));
        }

        return actions;
    }

    private InlineKeyboardMarkup getUsersButtons(List<User> users, int stateIndex) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.instance();

        for (User user : users) {
            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                            ButtonCommand.SET_PARENT_FOR_STUDENT.getCommand(),
                            user.getTelegramId(),
                            stateIndex,
                            messageTextMaker.userDataPatternMessageText(user.getName(), user.getLastName())
                    ))
                    .row();
        }

        inlineKeyboardMarkup.setKeyboard(builder.build().getKeyboard());
        return inlineKeyboardMarkup;
    }

    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.SET_PARENT_FOR_STUDENT;
    }

    @Override
    public void removeFromCacheBy(String id) {
        if (setParentForStudentCache.getIfPresent(id) != null)
            setParentForStudentCache.invalidate(id);
    }

    @Override
    public boolean hasFinished(String id) {
        var dto = setParentForStudentCache.getIfPresent(id);

        boolean result = true;

        if (dto != null) {
            result = dto.getStateMachine().isComplete();
        }

        return result;
    }

    @Override
    public int getCurrentStateIndex(String id) {
        var dto = setParentForStudentCache.getIfPresent(id);

        int result = -1;

        if (dto != null) {
            result = dto.getStateMachine().getState().getId().getIndex();
        }

        return result;
    }
}
