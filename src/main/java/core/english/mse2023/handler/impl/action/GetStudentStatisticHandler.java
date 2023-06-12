package core.english.mse2023.handler.impl.action;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.lowagie.text.DocumentException;
import core.english.mse2023.aop.annotation.handler.TeacherRole;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.component.MessageTextMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.dto.interactiveHandler.GetStudentStatisticDTO;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.exception.IllegalUserInputException;
import core.english.mse2023.exception.UnexpectedUpdateType;
import core.english.mse2023.handler.InteractiveHandler;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.PDFService;
import core.english.mse2023.service.SubscriptionService;
import core.english.mse2023.state.getStudentStatistic.GetStudentStatisticEvent;
import core.english.mse2023.state.getStudentStatistic.GetStudentStatisticState;
import core.english.mse2023.util.builder.InlineKeyboardBuilder;
import core.english.mse2023.util.utilities.TelegramInlineButtonsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.time.format.*;

@Component
@TeacherRole
@TextCommandType
@RequiredArgsConstructor
@Slf4j
public class GetStudentStatisticHandler implements InteractiveHandler {

    @Value("${statistics.student.pdf-template-name}")
    private String pdfTemplateName;

    @Value("${messages.handlers.get-student-statistic.choose-student-query}")
    private String chooseStudentQueryText;

    @Value("${messages.handlers.get-student-statistic.enter-interval-query}")
    private String enterIntervalQueryText;

    @Value("${messages.handlers.get-student-statistic.data-form}")
    private String dataFormText;

    @Value("${messages.handlers.get-student-statistic.complete}")
    private String completeText;


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    // This cache works in manual mode. It means - no evictions was configured
    private final Cache<String, GetStudentStatisticDTO> getStudentStatisticCache = Caffeine.newBuilder()
            .build();

    @Qualifier("getStudentStatisticStateMachineFactory")
    private final StateMachineFactory<GetStudentStatisticState, GetStudentStatisticEvent> stateMachineFactory;

    private final MessageTextMaker messageTextMaker;

    private final SubscriptionService subscriptionService;
    private final PDFService pdfService;


    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {
        String teacherTelegramId = update.getMessage().getFrom().getId().toString();


        StateMachine<GetStudentStatisticState, GetStudentStatisticEvent> stateMachine =
                stateMachineFactory.getStateMachine();
        stateMachine.start();

        GetStudentStatisticDTO dto = GetStudentStatisticDTO.builder()
                .stateMachine(stateMachine)
                .build();

        getStudentStatisticCache.put(teacherTelegramId, dto);


        List<Subscription> subscriptionsWithTeacher = subscriptionService.getAllSubscriptionsWithTeacher(teacherTelegramId);

        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(chooseStudentQueryText)
                .replyMarkup(getStudentsButtons(subscriptionsWithTeacher, stateMachine.getState().getId().getIndex()))
                .build();

        return List.of(message);
    }

    @Override
    public List<PartialBotApiMethod<?>> update(Update update, UserRole role) throws IllegalUserInputException, IllegalStateException {
        ArrayList<PartialBotApiMethod<?>> messages = new ArrayList<>();

        GetStudentStatisticDTO dto;

        if (update.hasMessage()) {
            dto = getStudentStatisticCache.getIfPresent(update.getMessage().getFrom().getId().toString());
        } else if (update.hasCallbackQuery()) {
            dto = getStudentStatisticCache.getIfPresent(update.getCallbackQuery().getFrom().getId().toString());
        } else {
            throw new UnexpectedUpdateType("Update type for CreateSubscriptionHandler wasn't message or callback query.");
        }

        if (dto == null) {
            log.error("DTO instance wasn't created yet! Cannot continue. User id: {}", update.getMessage().getFrom().getId());
            throw new IllegalStateException("There's no DTO created to continue building Subscription.");
        }

        StateMachine<GetStudentStatisticState, GetStudentStatisticEvent> stateMachine = dto.getStateMachine();

        switch (stateMachine.getState().getId()) {
            case STUDENT_CHOOSING -> {
                InlineButtonDTO inlineButtonDTO = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

                dto.setStudentSubscription(subscriptionService.getSubscriptionById(UUID.fromString(inlineButtonDTO.getData())));

                stateMachine.sendEvent(GetStudentStatisticEvent.CHOOSE_STUDENT);

                SendMessage message = SendMessage.builder()
                        .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                        .text(String.format(enterIntervalQueryText, dataFormText))
                        .build();

                message.setParseMode(ParseMode.MARKDOWNV2);

                messages.add(message);
            }
            case ENTERING_INTERVAL -> {
                // Parsing data from user
                parseInput(update.getMessage().getText(), dto);

                stateMachine.sendEvent(GetStudentStatisticEvent.ENTER_INTERVAL);

                try {
                    messages.add(createPDFDocument(dto, update.getMessage().getChatId().toString()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }

        return messages;
    }

    private void parseInput(String input, GetStudentStatisticDTO dto) throws IllegalUserInputException {
        Map<String, String> data = Arrays.stream(input.split("\n"))
                .map(field -> field.split(" "))
                .collect(Collectors.toMap(e -> e[0].toLowerCase().substring(0, e[0].length() - 1), e -> e[1]));

        if (data.size() != 2) {
            throw new IllegalUserInputException("Wrong amount of parameters present");
        }

        for (String key : data.keySet()) {
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
                        LocalDateTime localDateTime = LocalDate.parse(data.get(key), DateTimeFormatter.ofPattern("dd.MM.yyyy")).atStartOfDay();

                        dto.setEndDate(Timestamp.valueOf(localDateTime.toLocalDate().atTime(LocalTime.MAX)));
                    } catch (DateTimeParseException e) {
                        log.error("Failed to parse date format on EndDate parameter. Raw data: " + data.get(key));
                        throw new IllegalUserInputException("Failed to parse date format on EndDate parameter");
                    }

                }
                default -> throw new IllegalUserInputException("Wrong parameters!");
            }
        }

        if (dto.getStartDate().after(dto.getEndDate())) {
            throw new IllegalUserInputException("Start date cannot go after the end date.");
        }

        if (dto.getStartDate().after(new Date())) {
            throw new IllegalUserInputException("Start date cannot go after current date.");
        }

        if (dto.getEndDate().after(Timestamp.valueOf(LocalDate.now().atTime(LocalTime.MAX)))) {
            throw new IllegalUserInputException("End date cannot go after the end of current date.");
        }

    }

    private InlineKeyboardMarkup getStudentsButtons(List<Subscription> subscriptions, int stateIndex) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.instance();

        for (Subscription subscription : subscriptions) {
            User student = subscription.getStudent();

            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                            ButtonCommand.STUDENT_STATISTICS.getCommand(),
                            subscription.getId().toString(),
                            stateIndex,
                            messageTextMaker.userDataPatternMessageText(student.getName(), student.getLastName())
                    ))
                    .row();
        }

        inlineKeyboardMarkup.setKeyboard(builder.build().getKeyboard());
        return inlineKeyboardMarkup;
    }

    private SendDocument createPDFDocument(GetStudentStatisticDTO getStudentStatisticDTO, String chatId) throws DocumentException, IOException {

        InputStream generatedPDF = pdfService.generateStudentStatisticsPDF(
                pdfTemplateName,
                getStudentStatisticDTO.getStudentSubscription(),
                getStudentStatisticDTO.getStartDate(),
                getStudentStatisticDTO.getEndDate()
        );

        InputFile inputFile = new InputFile(generatedPDF, "Statistic.pdf");

        return SendDocument.builder()
                .chatId(chatId)
                .document(inputFile)
                .caption(completeText)
                .build();
    }


    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.STUDENT_STATISTICS;
    }

    @Override
    public void removeFromCacheBy(String id) {
        if (getStudentStatisticCache.getIfPresent(id) != null)
            getStudentStatisticCache.invalidate(id);
    }

    @Override
    public boolean hasFinished(String id) {
        var dto = getStudentStatisticCache.getIfPresent(id);

        boolean result = true;

        if (dto != null) {
            result = dto.getStateMachine().isComplete();
        }

        return result;
    }

    @Override
    public int getCurrentStateIndex(String id) {
        var dto = getStudentStatisticCache.getIfPresent(id);

        int result = -1;

        if (dto != null) {
            result = dto.getStateMachine().getState().getId().getIndex();
        }

        return result;
    }
}
