package core.english.mse2023.handler.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.exception.IllegalUserInputException;
import core.english.mse2023.constant.Command;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.dto.SubscriptionCreationDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.InteractiveHandler;
import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.SubscriptionType;
import core.english.mse2023.service.SubscriptionService;
import core.english.mse2023.service.UserService;
import core.english.mse2023.state.State;
import core.english.mse2023.state.subcription.InitializedState;
import core.english.mse2023.state.subcription.PartiallyCreatedState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateSubscriptionHandler implements InteractiveHandler {
    private static final String START_TEXT = "Для создания новой подписки заполните и отправьте форму с данными " +
            "\\(каждое поле на новой строке в одном сообщении в том же порядке\\)\\. Пример:\n%s";

    private static final String DATA_FORM_TEXT = """
            `startDate: 11\\.03\\.2023`
            `endDate: 30\\.04\\.2023`
            `lessonAmount: 7`
            """;

    private static final String USER_CHOOSE_TEXT = "Далее выберите студента, с которым будете заниматься:";

    private static final String USER_DATA_PATTERN = "%s%s";

    private static final String SUCCESS_TEXT = "Новая подписка и требуемые уроки созданы.";

    // This cache works in manual mode. It means - no evictions was configured
    private final Cache<String, SubscriptionCreationDTO> subscriptionCreationCache = Caffeine.newBuilder()
            .build();

    private final UserService userService;

    private final SubscriptionService subscriptionService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");


    @Override
    public List<SendMessage> handle(Update update) {

        // Creating new DTO for this user
        SubscriptionCreationDTO dto = SubscriptionCreationDTO.builder()
                .teacherTelegramId(update.getMessage().getFrom().getId().toString())
                .build();

        subscriptionCreationCache.put(update.getMessage().getFrom().getId().toString(), dto);

        // Sending start message
        SendMessage message;

        message = createMessage(update.getMessage().getChatId().toString(), String.format(START_TEXT, DATA_FORM_TEXT), null);

        message.setParseMode(ParseMode.MARKDOWNV2);

        return List.of(message);
    }

    @Override
    public List<SendMessage> update(Update update, State state) throws IllegalUserInputException, IllegalStateException {
        ArrayList<SendMessage> messages = new ArrayList<>();

        if (state instanceof InitializedState) {
            // Data comes from Message field

            // Getting current DTO
            SubscriptionCreationDTO dto = subscriptionCreationCache.getIfPresent(update.getMessage().getFrom().getId().toString());

            if (dto == null) {
                log.error("DTO instance wasn't created yet! Cannot continue. User id: {}; Current state: {}", update.getMessage().getFrom().getId(), state.getClass().getName());
                throw new IllegalStateException("There's no DTO created to continue building Subscription.");
            }

            // Parsing data from user
            parseInput(update.getMessage().getText(), dto);

            // TODO - ask user in future?
            dto.setType(SubscriptionType.QUANTITY_BASED);

            // Sending buttons with students. Data from them will be used in the next state
            SendMessage sendMessage = createMessage(update.getMessage().getChatId().toString(), USER_CHOOSE_TEXT, null);
            sendMessage.setReplyMarkup(getStudentsButtons(userService.getAllStudents(), state));

            messages.add(sendMessage);

        } else if (state instanceof PartiallyCreatedState) {
            // Data comes from Data field of CallbackQuery

            // Getting current DTO
            SubscriptionCreationDTO dto = subscriptionCreationCache.getIfPresent(update.getCallbackQuery().getFrom().getId().toString());
            if (dto == null) {
                log.error("DTO instance wasn't created yet! Cannot continue. User id: {}; Current state: {}", update.getCallbackQuery().getFrom().getId(), state.getClass().getName());
                throw new IllegalStateException("There's no DTO created to continue building Subscription.");
            }

            InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());
            dto.setStudentTelegramId(buttonData.getData());

            // Since DTO is completely full - it's time to pass creation of the Subscription object to its service
            subscriptionService.createSubscription(dto);

            removeFromCacheBy(update.getCallbackQuery().getFrom().getId().toString());

            SendMessage sendMessage = createMessage(update.getCallbackQuery().getMessage().getChatId().toString(), SUCCESS_TEXT, null);
            messages.add(sendMessage);
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
    }

    private InlineKeyboardMarkup getStudentsButtons(List<User> students, State state) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (User student : students) {
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setCallbackData(InlineButtonDTOEncoder.encode(new InlineButtonDTO(getCommand().getCommand(), state.getStateIndex(), student.getTelegramId())));

            button.setText(String.format(USER_DATA_PATTERN,
                    (student.getLastName() != null) ? (student.getLastName() + " ") : "", // Student's last name if present
                    student.getName() // Student's name (always present)
            ));

            keyboardRow.add(button);

            keyboard.add(keyboardRow);
        }


        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }

    @Override
    public State getInitialState() {
        return new InitializedState();
    }

    @Override
    public void removeFromCacheBy(String id) {
        if (subscriptionCreationCache.getIfPresent(id) != null)
            subscriptionCreationCache.invalidate(id);
    }

    @Override
    public BotCommand getCommand() {
        return ButtonCommand.CREATE_SUBSCRIPTION;
    }

}
