package core.english.mse2023.handler.impl.action;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import core.english.mse2023.aop.annotation.handler.AdminRole;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.component.MessageTextMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.constant.Command;
import core.english.mse2023.dto.interactiveHandler.AssignRoleDTO;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.exception.IllegalUserInputException;
import core.english.mse2023.handler.InteractiveHandler;
import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.UserService;
import core.english.mse2023.state.assignRoleForGuest.AssignRoleEvent;
import core.english.mse2023.state.assignRoleForGuest.AssignRoleState;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@TextCommandType
@AdminRole
@RequiredArgsConstructor
public class AssignRoleHandler implements InteractiveHandler {

    @Value("${messages.handlers.assign-role.choose-guest}")
    private String chooseGuestText;

    @Value("${messages.handlers.assign-role.guests-not-found}")
    private String usersNotFoundText;

    @Value("${messages.handlers.assign-role.choose-role}")
    private String chooseRoleText;

    @Value("${messages.handlers.assign-role.success}")
    private String successText;

    @Value("${messages.handlers.assign-role.user-pattern-with-role}")
    private String userPatternWithRole;


    private final MessageTextMaker messageTextMaker;

    private final UserService userService;

    // This cache works in manual mode. It means - no evictions was configured
    private final Cache<String, AssignRoleDTO> assignRoleCache = Caffeine.newBuilder()
            .build();

    @Qualifier("assignRoleStateMachineFactory")
    private final StateMachineFactory<AssignRoleState, AssignRoleEvent> stateMachineFactory;


    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {
        StateMachine<AssignRoleState, AssignRoleEvent> stateMachine =
                stateMachineFactory.getStateMachine();
        stateMachine.start();

        AssignRoleDTO dto = AssignRoleDTO.builder()
                .stateMachine(stateMachine)
                .build();

        assignRoleCache.put(update.getMessage().getFrom().getId().toString(), dto);

        List<User> users = userService.getAllUsersExceptOne(update.getMessage().getFrom().getId().toString());

        if (users.isEmpty()) {
            stateMachine.stop();
            SendMessage message = SendMessage.builder()
                    .chatId(update.getMessage().getChatId().toString())
                    .text(usersNotFoundText)
                    .build();
            return List.of(message);
        }

        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(chooseGuestText)
                .replyMarkup(getUsersButtons(users, stateMachine.getState().getId().getIndex()))
                .build();
        return List.of(message);
    }

    @Override
    public List<PartialBotApiMethod<?>> update(Update update, UserRole role) throws IllegalUserInputException, IllegalStateException {
        if (update.getCallbackQuery() == null) {
            return new ArrayList<>();
        }

        AssignRoleDTO dto = assignRoleCache.getIfPresent(update.getCallbackQuery().getFrom().getId().toString());

        if (dto == null) {
            log.error("DTO instance wasn't created yet! Cannot continue. User id: {}", update.getCallbackQuery().getFrom().getId());
            throw new IllegalStateException("There's no DTO created to receive message for admin.");
        }

        var stateMachine = dto.getStateMachine();

        InlineButtonDTO inlineButtonDTO = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        List<PartialBotApiMethod<?>> actions = new ArrayList<>();

        if (stateMachine.getState().getId() == AssignRoleState.USER_CHOOSING) {

            dto.setChosenGuest(inlineButtonDTO.getData());

            stateMachine.sendEvent(AssignRoleEvent.CHOOSE_USER);

            actions.add(SendMessage.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .text(chooseRoleText)
                    .replyMarkup(getRolesButtons(stateMachine.getState().getId().getIndex()))
                    .build());
            actions.add(new AnswerCallbackQuery(update.getCallbackQuery().getId()));
        } else if (stateMachine.getState().getId() == AssignRoleState.ROLE_CHOOSING) {

            dto.setChosenRole(inlineButtonDTO.getData());

            stateMachine.sendEvent(AssignRoleEvent.CHOOSE_ROLE);

            User user = userService.setRoleForUser(dto.getChosenGuest(), UserRole.valueOf(dto.getChosenRole()));

            actions.add(SendMessage.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .text(String.format(successText,
                            messageTextMaker.userDataPatternMessageText(user.getName(), user.getLastName()),
                            user.getRole().getString()
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
                            ButtonCommand.ASSIGN_ROLE.getCommand(),
                            user.getTelegramId(),
                            stateIndex,
                            String.format(userPatternWithRole,
                                    messageTextMaker.userDataPatternMessageText(user.getName(), user.getLastName()),
                                    user.getRole().getString()
                            )
                    ))
                    .row();
        }

        inlineKeyboardMarkup.setKeyboard(builder.build().getKeyboard());
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup getRolesButtons(int stateIndex) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.instance();

        for (UserRole role : UserRole.values()) {
            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                            ButtonCommand.ASSIGN_ROLE.getCommand(),
                            role.toString(),
                            stateIndex,
                            role.getString()
                    ))
                    .row();
        }

        inlineKeyboardMarkup.setKeyboard(builder.build().getKeyboard());
        return inlineKeyboardMarkup;

    }

    @Override
    public Command getCommandObject() {
        return ButtonCommand.ASSIGN_ROLE;
    }

    @Override
    public void removeFromCacheBy(String id) {
        if (assignRoleCache.getIfPresent(id) != null)
            assignRoleCache.invalidate(id);
    }

    @Override
    public boolean hasFinished(String id) {
        var dto = assignRoleCache.getIfPresent(id);

        boolean result = true;

        if (dto != null) {
            result = dto.getStateMachine().isComplete();
        }

        return result;
    }

    @Override
    public int getCurrentStateIndex(String id) {
        var dto = assignRoleCache.getIfPresent(id);

        int result = -1;

        if (dto != null) {
            result = dto.getStateMachine().getState().getId().getIndex();
        }

        return result;
    }
}
