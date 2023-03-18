package core.english.mse2023.handler.impl;

import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.InlineButtonHandler;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.User;
import core.english.mse2023.repository.LessonRepository;
import core.english.mse2023.repository.SubscriptionRepository;
import core.english.mse2023.service.SubscriptionService;
import core.english.mse2023.state.State;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetMoreSubscriptionInfoHandler implements InlineButtonHandler {

    private static final String LESSON_DATA_PATTERN = """
            %s
            """;

    private final SubscriptionService subscriptionService;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {

        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        List<Lesson> lessons = subscriptionService.getAllLessonsForSubscription(UUID.fromString(buttonData.getData()));

        EditMessageReplyMarkup editMessageReplyMarkup = editMessageReplyMarkup(
                update.getCallbackQuery().getMessage().getChatId().toString(),
                update.getCallbackQuery().getMessage().getMessageId(),
                getLessonsInlineButtons(lessons)
        );

        return List.of(editMessageReplyMarkup);
    }

    private InlineKeyboardMarkup getLessonsInlineButtons(List<Lesson> lessons) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (Lesson lesson : lessons) {
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setCallbackData(InlineButtonDTOEncoder.encode(
                    InlineButtonDTO.builder()
                            .command(InlineButtonCommand.LESSON_GET_MORE_DATA)
                            .stateIndex(0)
                            .data(lesson.getId().toString())
                            .build()
            ));

            button.setText(String.format(LESSON_DATA_PATTERN, lesson.getTopic()));

            keyboardRow.add(button);

            keyboard.add(keyboardRow);
        }


        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }

    @Override
    public String getCommand() {
        return InlineButtonCommand.SUBSCRIPTION_GET_MORE_DATA;
    }
}
