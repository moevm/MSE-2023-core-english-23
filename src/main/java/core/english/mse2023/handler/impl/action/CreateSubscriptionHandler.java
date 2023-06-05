package core.english.mse2023.handler.impl.action;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import core.english.mse2023.aop.annotation.handler.AdminRole;
import core.english.mse2023.aop.annotation.handler.TeacherRole;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.component.MessageTextMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.exception.IllegalUserInputException;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.dto.SubscriptionCreationDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.exception.UnexpectedUpdateType;
import core.english.mse2023.handler.InteractiveHandler;
import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.SubscriptionType;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.SubscriptionService;
import core.english.mse2023.service.UserService;
import core.english.mse2023.state.subcription.SubscriptionCreationEvent;
import core.english.mse2023.state.subcription.SubscriptionCreationState;
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
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@TextCommandType
@AdminRole
@TeacherRole
@RequiredArgsConstructor
public class CreateSubscriptionHandler implements InteractiveHandler {

    @Value("${handlers.create-subscription-handler.start-text}")
    private String startText;

    @Value("${handlers.create-subscription-handler.data-form-text}")
    private String dataFormText;

    private static final String STUDENT_CHOOSE_TEXT = "Далее выберите студента, с которым будут проводиться занятия:";
    private static final String TEACHER_CHOOSE_TEXT = "Также выберите учителя, который будет проводить занятия:";

    private static final String SUCCESS_TEXT = "Новый абонемент и требуемые уроки созданы.";

    // This cache works in manual mode. It means - no evictions was configured
    private final Cache<String, SubscriptionCreationDTO> subscriptionCreationCache = Caffeine.newBuilder()
            .build();

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    private final MessageTextMaker messageTextMaker;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Qualifier("subscriptionCreationStateMachineFactory")
    private final StateMachineFactory<SubscriptionCreationState, SubscriptionCreationEvent> stateMachineFactory;

    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {

        StateMachine<SubscriptionCreationState, SubscriptionCreationEvent> stateMachine =
                stateMachineFactory.getStateMachine();
        stateMachine.start();
        stateMachine.getExtendedState().getVariables().put("UserRole", userRole);

        // Creating new DTO for this user
        SubscriptionCreationDTO dto = SubscriptionCreationDTO.builder()
                .stateMachine(stateMachine)
                .build();

        subscriptionCreationCache.put(update.getMessage().getFrom().getId().toString(), dto);

        // Sending start message
        SendMessage message;

        message = SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(String.format(startText, dataFormText))
                .build();

        message.setParseMode(ParseMode.MARKDOWNV2);

        return List.of(message);
    }

    @Override
    public List<PartialBotApiMethod<?>> update(Update update, UserRole userRole) throws IllegalUserInputException, IllegalStateException {
        ArrayList<PartialBotApiMethod<?>> messages = new ArrayList<>();

        SubscriptionCreationDTO dto;

        if (update.hasMessage()) {
            dto = subscriptionCreationCache.getIfPresent(update.getMessage().getFrom().getId().toString());
        } else if (update.hasCallbackQuery()) {
            dto = subscriptionCreationCache.getIfPresent(update.getCallbackQuery().getFrom().getId().toString());
        } else {
            throw new UnexpectedUpdateType("Update type for CreateSubscriptionHandler wasn't message or callback query.");
        }

        if (dto == null) {
            log.error("DTO instance wasn't created yet! Cannot continue. User id: {}", update.getMessage().getFrom().getId());
            throw new IllegalStateException("There's no DTO created to continue building Subscription.");
        }

        var stateMachine = dto.getStateMachine();

        if (stateMachine.getState().getId() == SubscriptionCreationState.START_DATE_CHOOSING) {

            // Parsing data from user
            parseInput(update.getMessage().getText(), dto);

            // TODO - ask user in future?
            dto.setType(SubscriptionType.QUANTITY_BASED);

            stateMachine.sendEvent(SubscriptionCreationEvent.CHOOSE_START_DATE);
            stateMachine.sendEvent(SubscriptionCreationEvent.CHOOSE_END_DATE);
            stateMachine.sendEvent(SubscriptionCreationEvent.ENTER_LESSON_AMOUNT);

            // Sending buttons with students. Data from them will be used in the next state
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(update.getMessage().getChatId().toString())
                    .text(STUDENT_CHOOSE_TEXT)
                    .replyMarkup(getStudentsButtons(userService.getAllStudents(), stateMachine.getState().getId()))
                    .build();

            messages.add(sendMessage);

        } else if (stateMachine.getState().getId() == SubscriptionCreationState.STUDENT_CHOOSING) {

            InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());
            dto.setStudentTelegramId(buttonData.getData());

            stateMachine.sendEvent(SubscriptionCreationEvent.CHOOSE_STUDENT);

            if (userRole == UserRole.TEACHER) {
                dto.setTeacherTelegramId(update.getCallbackQuery().getFrom().getId().toString());

                subscriptionService.createSubscription(dto);

                SendMessage sendMessage = SendMessage.builder()
                        .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                        .text(SUCCESS_TEXT)
                        .build();

                messages.add(sendMessage);
                messages.add(new AnswerCallbackQuery(update.getCallbackQuery().getId()));
            } else {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                        .replyMarkup(getTeachersButtons(userService.getAllTeachers(), stateMachine.getState().getId()))
                        .text(TEACHER_CHOOSE_TEXT)
                        .build();

                messages.add(sendMessage);
                messages.add(new AnswerCallbackQuery(update.getCallbackQuery().getId()));
            }

        } else if (stateMachine.getState().getId() == SubscriptionCreationState.TEACHER_CHOOSING) {
            InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());
            dto.setTeacherTelegramId(buttonData.getData());

            stateMachine.sendEvent(SubscriptionCreationEvent.CHOOSE_TEACHER);

            subscriptionService.createSubscription(dto);

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .text(SUCCESS_TEXT)
                    .build();

            messages.add(sendMessage);
            messages.add(new AnswerCallbackQuery(update.getCallbackQuery().getId()));
        }

        return messages;
    }

    private void parseInput(String input, SubscriptionCreationDTO dto) throws IllegalUserInputException {
        Map<String, String> data = Arrays.stream(input.split("\n"))
                .map(field -> field.split(" "))
                .collect(Collectors.toMap(e -> e[0].toLowerCase().substring(0, e[0].length() - 1), e -> e[1]));

        if (data.size() != 3) {
            throw new IllegalUserInputException("Wrong amount of parameters present");
        }

        for (String key :
                data.keySet()) {
            switch (key) {
                case "startdate" -> {
                    try {
                        Date parsedDate = dateFormat.parse(data.get(key));
                        dto.setStartDate(new Timestamp(parsedDate.getTime()));
                    } catch (ParseException e) {
                        log.error("Failed to parse date format on StartDate parameter. Raw data: " + data.get(key));
                        throw new IllegalUserInputException("Failed to parse date format on StartDate parameter");
                    }
                }
                case "enddate" -> {
                    try {
                        Date parsedDate = dateFormat.parse(data.get(key));
                        dto.setEndDate(new Timestamp(parsedDate.getTime()));
                    } catch (ParseException e) {
                        log.error("Failed to parse date format on EndDate parameter. Raw data: " + data.get(key));
                        throw new IllegalUserInputException("Failed to parse date format on EndDate parameter");
                    }

                }
                case "lessonamount" -> dto.setLessonsRest(Integer.parseInt(data.get(key)));
                default -> throw new IllegalUserInputException("Wrong parameters!");
            }
        }

        if (dto.getStartDate().after(dto.getEndDate())) {
            throw new IllegalUserInputException("Start date cannot go after the end date.");
        }

        if (dto.getLessonsRest() < 1) {
            throw new IllegalUserInputException("Subscription has to have a least 1 lesson in it.");
        }
    }

    private InlineKeyboardMarkup getStudentsButtons(List<User> students, SubscriptionCreationState state) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.instance();

        for (User student : students) {
            builder
                    .button(TelegramInlineButtonsUtils.createInlineButton(
                            getCommandObject().getCommand(),
                            student.getTelegramId(),
                            state.getIndex(),
                            messageTextMaker.userDataPatternMessageText(student.getName(), student.getLastName())
                    ))
                    .row();
        }


        inlineKeyboardMarkup.setKeyboard(builder.build().getKeyboard());
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup getTeachersButtons(List<User> teachers, SubscriptionCreationState state) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.instance();

        for (User teacher : teachers) {
            builder
                    .button(TelegramInlineButtonsUtils.createInlineButton(
                            getCommandObject().getCommand(),
                            teacher.getTelegramId(),
                            state.getIndex(),
                            messageTextMaker.userDataPatternMessageText(teacher.getName(), teacher.getLastName())
                    ))
                    .row();
        }


        inlineKeyboardMarkup.setKeyboard(builder.build().getKeyboard());
        return inlineKeyboardMarkup;
    }

    @Override
    public void removeFromCacheBy(String id) {
        if (subscriptionCreationCache.getIfPresent(id) != null)
            subscriptionCreationCache.invalidate(id);
    }

    @Override
    public boolean hasFinished(String id) {
        var dto = subscriptionCreationCache.getIfPresent(id);

        boolean result = true;

        if (dto != null) {
            result = dto.getStateMachine().isComplete();
        }

        return result;
    }

    @Override
    public int getCurrentStateIndex(String id) {
        var dto = subscriptionCreationCache.getIfPresent(id);

        int result = -1;

        if (dto != null) {
            result = dto.getStateMachine().getState().getId().getIndex();
        }

        return result;
    }

    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.CREATE_SUBSCRIPTION;
    }

}
