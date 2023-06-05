package core.english.mse2023.handler.impl.action;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import core.english.mse2023.aop.annotation.handler.AdminRole;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.aop.annotation.handler.TeacherRole;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.dto.LessonCreationDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.exception.IllegalUserInputException;
import core.english.mse2023.handler.InteractiveHandler;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.dictionary.SubscriptionStatus;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.LessonService;
import core.english.mse2023.service.SubscriptionService;
import core.english.mse2023.state.createLesson.LessonCreationEvent;
import core.english.mse2023.state.createLesson.LessonCreationState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@InlineButtonType
@AdminRole
@TeacherRole
@RequiredArgsConstructor
public class CreateLessonHandler implements InteractiveHandler {

    @Value("${handlers.create-lesson-handler.start-text}")
    private String startText;

    @Value("${handlers.create-lesson-handler.data-form-text}")
    private String dataFormText;

    @Value("${handlers.create-lesson-handler.subscription-not-active-text}")
    private String subscriptionNotActiveText;

    private static final String SUCCESS_TEXT = "Новый урок добавлен.";

    private final Cache<String, LessonCreationDTO> lessonCreationCache = Caffeine.newBuilder()
            .build();

    private final LessonService lessonService;
    private final SubscriptionService subscriptionService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Qualifier("lessonCreationStateMachineFactory")
    private final StateMachineFactory<LessonCreationState, LessonCreationEvent> stateMachineFactory;

    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {

        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        Subscription subscription = subscriptionService.getSubscriptionById(UUID.fromString(buttonData.getData()));

        if (subscription.getStatus() != SubscriptionStatus.ACTIVE) {
            SendMessage message;

            message = SendMessage.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .text(subscriptionNotActiveText)
                    .build();

            return List.of(message);
        }

        StateMachine<LessonCreationState, LessonCreationEvent> stateMachine =
                stateMachineFactory.getStateMachine();
        stateMachine.start();

        LessonCreationDTO dto = LessonCreationDTO.builder()
                .stateMachine(stateMachine)
                .subscription(subscription)
                .build();

        lessonCreationCache.put(update.getCallbackQuery().getFrom().getId().toString(), dto);

        // Sending start message
        SendMessage message;

        message = SendMessage.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                .text(String.format(startText, dataFormText))
                .build();

        message.setParseMode(ParseMode.MARKDOWNV2);

        return List.of(message);
    }

    @Override
    public List<PartialBotApiMethod<?>> update(Update update, UserRole userRole) throws IllegalUserInputException, IllegalStateException {

        LessonCreationDTO dto = lessonCreationCache.getIfPresent(update.getMessage().getFrom().getId().toString());

        if (dto == null) {
            log.error("DTO instance wasn't created yet! Cannot continue. User id: {}", update.getMessage().getFrom().getId());
            throw new IllegalStateException("There's no DTO created to continue building Lesson.");
        }

        var stateMachine = dto.getStateMachine();

        if (stateMachine.getState().getId() != LessonCreationState.DATE_CHOOSING) {
            log.error("Update method has been called, but interactive handler has the wrong state. User id: {}", update.getMessage().getFrom().getId());
            throw new IllegalStateException(String.format("DATE_CHOOSING state expected. Current state: %s", stateMachine.getState().toString()));
        }
        stateMachine.sendEvent(LessonCreationEvent.CHOOSE_DATE);
        stateMachine.sendEvent(LessonCreationEvent.CHOOSE_TOPIC);
        stateMachine.sendEvent(LessonCreationEvent.CHOOSE_LINK);

        parseInput(update.getMessage().getText(), dto);

        lessonService.createLesson(dto.getSubscription(), dto.getDate(), dto.getTopic(), dto.getLink());

        // Sending buttons with students. Data from them will be used in the next state
        SendMessage sendMessage = SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(SUCCESS_TEXT)
                .build();

        return List.of(sendMessage);
    }

    private void parseInput(String input, LessonCreationDTO dto) throws IllegalUserInputException {
        Map<String, String> data = Arrays.stream(input.split("\n"))
                .map(field -> field.split(" "))
                .collect(Collectors.toMap(e -> e[0].toLowerCase().substring(0, e[0].length() - 1), e -> {
                    String[] tmp = new String[e.length - 1];
                    System.arraycopy(e, 1, tmp, 0, e.length - 1);
                    return  String.join(" ", tmp);
                }));

        if (data.size() < 2) {
            throw new IllegalUserInputException("Wrong amount of parameters present");
        }

        for (String key :
                data.keySet()) {
            switch (key) {
                case "date" -> {
                    try {
                        Date parsedDate = dateFormat.parse(data.get(key));
                        dto.setDate(new Timestamp(parsedDate.getTime()));
                    } catch (ParseException e) {
                        throw new IllegalUserInputException("Wrong parameters!");
                    }
                }
                case "topic" -> dto.setTopic(data.get(key));
                case "link" -> dto.setLink(data.get(key));
                default -> throw new IllegalUserInputException("Wrong parameters!");
            }
        }
    }



    @Override
    public void removeFromCacheBy(String id) {
        if (lessonCreationCache.getIfPresent(id) != null)
            lessonCreationCache.invalidate(id);
    }

    @Override
    public boolean hasFinished(String id) {
        var dto = lessonCreationCache.getIfPresent(id);

        boolean result = true;

        if (dto != null) {
            result = dto.getStateMachine().isComplete();
        }

        return result;
    }

    @Override
    public int getCurrentStateIndex(String id) {
        var dto = lessonCreationCache.getIfPresent(id);

        int result = -1;

        if (dto != null) {
            result = dto.getStateMachine().getState().getId().getIndex();
        }

        return result;
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.CREATE_LESSON;
    }

}
