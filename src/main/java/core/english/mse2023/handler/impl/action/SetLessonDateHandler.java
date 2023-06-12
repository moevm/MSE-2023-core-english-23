package core.english.mse2023.handler.impl.action;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import core.english.mse2023.aop.annotation.handler.AdminRole;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.dto.SetLessonDateDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.exception.IllegalUserInputException;
import core.english.mse2023.handler.InteractiveHandler;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.LessonService;
import core.english.mse2023.service.SubscriptionService;
import core.english.mse2023.state.setLessonDate.SetLessonDateEvent;
import core.english.mse2023.state.setLessonDate.SetLessonDateState;
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

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@InlineButtonType
@AdminRole
@RequiredArgsConstructor
public class SetLessonDateHandler implements InteractiveHandler {

    @Value("${messages.handlers.set-lesson-date-handler.start}")
    private String startText;

    @Value("${messages.handlers.set-lesson-date-handler.data-form}")
    private String dataFormText;

    @Value("${messages.handlers.set-lesson-date-handler.success}")
    private String successText;


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat dateOutputFormat = new SimpleDateFormat("dd\\.MM\\.yyyy");


    private final LessonService lessonService;
    private final SubscriptionService subscriptionService;

    // This cache works in manual mode. It means - no evictions was configured
    private final Cache<String, SetLessonDateDTO> setLessonDateCache = Caffeine.newBuilder()
            .build();

    @Qualifier("setLessonDateStateMachineFactory")
    private final StateMachineFactory<SetLessonDateState, SetLessonDateEvent> stateMachineFactory;


    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {
        InlineButtonDTO inlineButtonDTO = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        StateMachine<SetLessonDateState, SetLessonDateEvent> stateMachine =
                stateMachineFactory.getStateMachine();
        stateMachine.start();

        UUID lessonId = UUID.fromString(inlineButtonDTO.getData());
        Subscription subscription = lessonService.getLessonById(lessonId).getSubscription();

        SetLessonDateDTO dto = SetLessonDateDTO.builder()
                .lessonId(lessonId)
                .subscriptionStartDate(subscription.getStartDate())
                .subscriptionEndDate(subscription.getEndDate())
                .stateMachine(stateMachine)
                .build();

        setLessonDateCache.put(update.getCallbackQuery().getFrom().getId().toString(), dto);

        // Sending start message
        SendMessage message;

        message = SendMessage.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                .text(String.format(startText, dataFormText, dateOutputFormat.format(subscription.getStartDate()), dateOutputFormat.format(subscription.getEndDate())))
                .parseMode(ParseMode.MARKDOWNV2)
                .build();

        return List.of(message, new AnswerCallbackQuery(update.getCallbackQuery().getId()));
    }

    @Override
    public List<PartialBotApiMethod<?>> update(Update update, UserRole role) throws IllegalUserInputException, IllegalStateException {
        SetLessonDateDTO dto = setLessonDateCache.getIfPresent(update.getMessage().getFrom().getId().toString());

        if (dto == null) {
            log.error("DTO instance wasn't created yet! Cannot continue. User id: {}", update.getMessage().getFrom().getId());
            throw new IllegalStateException("There's no DTO created to receive message for parent.");
        }

        var stateMachine = dto.getStateMachine();

        if (stateMachine.getState().getId() != SetLessonDateState.WAITING_FOR_DATE) {
            log.error("Update method has been called, but interactive handler has the wrong state. User id: {}", update.getMessage().getFrom().getId());
            throw new IllegalStateException(String.format("WAITING_FOR_DATE state expected. Current state: %s", stateMachine.getState().toString()));
        }

        try {
            Date parsedDate = dateFormat.parse(update.getMessage().getText());

            Timestamp date = new Timestamp(parsedDate.getTime());

            dto.setDate(date);
        } catch (ParseException e) {
            log.error("Failed to parse date format on StartDate parameter. Raw data: " + update.getMessage().getText());
            throw new IllegalUserInputException("Failed to parse date format on StartDate parameter");
        }

        Lesson lesson = lessonService.setLessonDate(dto.getDate(), dto.getLessonId());

        stateMachine.sendEvent(SetLessonDateEvent.ENTER_DATE);

        // Sending buttons with students. Data from them will be used in the next state

        SendMessage sendMessage = SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(String.format(successText, lesson.getTopic()))
                .build();

        return List.of(sendMessage);
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.SET_LESSON_DATE;
    }

    @Override
    public void removeFromCacheBy(String id) {
        if (setLessonDateCache.getIfPresent(id) != null)
            setLessonDateCache.invalidate(id);
    }

    @Override
    public boolean hasFinished(String id) {
        var dto = setLessonDateCache.getIfPresent(id);

        boolean result = true;

        if (dto != null) {
            result = dto.getStateMachine().isComplete();
        }

        return result;
    }

    @Override
    public int getCurrentStateIndex(String id) {
        var dto = setLessonDateCache.getIfPresent(id);

        int result = -1;

        if (dto != null) {
            result = dto.getStateMachine().getState().getId().getIndex();
        }

        return result;
    }
}
