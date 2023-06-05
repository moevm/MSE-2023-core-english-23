package core.english.mse2023.handler.impl.action;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import core.english.mse2023.aop.annotation.handler.AdminRole;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.component.MessageTextMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.constant.Command;
import core.english.mse2023.dto.AssignRoleForGuestDTO;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.exception.IllegalUserInputException;
import core.english.mse2023.handler.InteractiveHandler;
import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.UserService;
import core.english.mse2023.state.assignRoleForGuest.AssignRoleForGuestEvent;
import core.english.mse2023.state.assignRoleForGuest.AssignRoleForGuestState;
import core.english.mse2023.util.builder.InlineKeyboardBuilder;
import core.english.mse2023.util.utilities.TelegramInlineButtonsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class AssignRoleForGuest implements InteractiveHandler {

    private static final String CHOOSE_GUEST_TEXT = "Выберите гостя:";

    private static final String GUESTS_NOT_FOUND = "Незарегистрированных пользователей в системе не найдено.";

    private static final String CHOOSE_ROLE_TEXT = "Выберите новую роль гостю:";

    private static final String SUCCESS_TEXT = "Для пользователя \"%s\" успешно установлена роль \"%s\"";
    private final MessageTextMaker messageTextMaker;

    private final UserService userService;

    // This cache works in manual mode. It means - no evictions was configured
    private final Cache<String, AssignRoleForGuestDTO> assignRoleForGuestCache = Caffeine.newBuilder()
            .build();

    @Qualifier("assignRoleForGuestStateMachineFactory")
    private final StateMachineFactory<AssignRoleForGuestState, AssignRoleForGuestEvent> stateMachineFactory;


    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {
        StateMachine<AssignRoleForGuestState, AssignRoleForGuestEvent> stateMachine =
                stateMachineFactory.getStateMachine();
        stateMachine.start();

        AssignRoleForGuestDTO dto = AssignRoleForGuestDTO.builder()
                .stateMachine(stateMachine)
                .build();

        assignRoleForGuestCache.put(update.getMessage().getFrom().getId().toString(), dto);

        List<User> students = userService.getAllGuests();

        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(students.isEmpty() ? GUESTS_NOT_FOUND : CHOOSE_GUEST_TEXT)
                .replyMarkup(getGuestsButtons(students, stateMachine.getState().getId().getIndex()))
                .build();

        return List.of(message);
    }

    @Override
    public List<PartialBotApiMethod<?>> update(Update update, UserRole role) throws IllegalUserInputException, IllegalStateException {
        AssignRoleForGuestDTO dto = assignRoleForGuestCache.getIfPresent(update.getCallbackQuery().getFrom().getId().toString());

        if (dto == null) {
            log.error("DTO instance wasn't created yet! Cannot continue. User id: {}", update.getCallbackQuery().getFrom().getId());
            throw new IllegalStateException("There's no DTO created to receive message for admin.");
        }

        var stateMachine = dto.getStateMachine();

        InlineButtonDTO inlineButtonDTO = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        List<PartialBotApiMethod<?>> actions = new ArrayList<>();

        if (stateMachine.getState().getId() == AssignRoleForGuestState.GUEST_CHOOSING) {

            dto.setChosenGuest(inlineButtonDTO.getData());

            stateMachine.sendEvent(AssignRoleForGuestEvent.CHOOSE_GUEST);

            actions.add(SendMessage.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .text(CHOOSE_ROLE_TEXT)
                    .replyMarkup(getRolesButtons(stateMachine.getState().getId().getIndex()))
                    .build());
            actions.add(new AnswerCallbackQuery(update.getCallbackQuery().getId()));
        } else if (stateMachine.getState().getId() == AssignRoleForGuestState.ROLE_CHOOSING) {

            dto.setChosenRole(inlineButtonDTO.getData());

            stateMachine.sendEvent(AssignRoleForGuestEvent.CHOOSE_ROLE);

            User user = userService.setRoleForUser(dto.getChosenGuest(), UserRole.valueOf(dto.getChosenRole()));

            actions.add(SendMessage.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .text(String.format(SUCCESS_TEXT,
                            messageTextMaker.userDataPatternMessageText(
                                    (user.getLastName() != null) ? (user.getLastName() + " ") : "", // Student's last name if present
                                    user.getName() // Student's name (always present)
                            ),
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

    private InlineKeyboardMarkup getGuestsButtons(List<User> users, int stateIndex) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.instance();

        for (User user : users) {
            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                            ButtonCommand.ASSIGN_ROLE_FOR_GUEST.getCommand(),
                            user.getTelegramId(),
                            stateIndex,
                            messageTextMaker.userDataPatternMessageText(
                                    (user.getLastName() != null) ? (user.getLastName() + " ") : "", // Student's last name if present
                                    user.getName() // Student's name (always present)
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
                            ButtonCommand.ASSIGN_ROLE_FOR_GUEST.getCommand(),
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
        return ButtonCommand.ASSIGN_ROLE_FOR_GUEST;
    }

    @Override
    public void removeFromCacheBy(String id) {
        if (assignRoleForGuestCache.getIfPresent(id) != null)
            assignRoleForGuestCache.invalidate(id);
    }

    @Override
    public boolean hasFinished(String id) {
        var dto = assignRoleForGuestCache.getIfPresent(id);

        boolean result = true;

        if (dto != null) {
            result = dto.getStateMachine().isComplete();
        }

        return result;
    }

    @Override
    public int getCurrentStateIndex(String id) {
        var dto = assignRoleForGuestCache.getIfPresent(id);

        int result = -1;

        if (dto != null) {
            result = dto.getStateMachine().getState().getId().getIndex();
        }

        return result;
    }
}
