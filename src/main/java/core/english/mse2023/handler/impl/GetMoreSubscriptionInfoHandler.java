package core.english.mse2023.handler.impl;

import core.english.mse2023.aop.annotation.handler.InlineButtonHandler;
import core.english.mse2023.aop.annotation.handler.TeacherHandler;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.service.SubscriptionService;
import core.english.mse2023.util.builder.InlineKeyboardBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@InlineButtonHandler
@RequiredArgsConstructor
public class GetMoreSubscriptionInfoHandler implements Handler {

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

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.instance();

        for (Lesson lesson : lessons) {
            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setCallbackData(InlineButtonDTOEncoder.encode(
                    InlineButtonDTO.builder()
                            .command(InlineButtonCommand.LESSON_GET_MORE_DATA)
                            .stateIndex(0)
                            .data(lesson.getId().toString())
                            .build()
            ));

            button.setText(String.format(LESSON_DATA_PATTERN, lesson.getTopic()));

            builder.button(button);
            builder.row();
        }


        inlineKeyboardMarkup.setKeyboard(builder.build().getKeyboard());
        return inlineKeyboardMarkup;
    }

    @Override
    public BotCommand getCommandObject() {
        return new BotCommand(InlineButtonCommand.SUBSCRIPTION_GET_MORE_DATA, "");
    }
}
