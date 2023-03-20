package core.english.mse2023.handler.impl;

import core.english.mse2023.aop.annotation.handler.InlineButtonType;
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

import java.util.List;
import java.util.UUID;

@Component
@InlineButtonType
@RequiredArgsConstructor
public class GetMoreSubscriptionInfoHandler implements Handler {

    private static final String LESSON_DATA_PATTERN = "%s";
    private static final String BACK_TO_MAIN_MENU_TEXT = "◄ Назад в главное меню ◄";

    private final SubscriptionService subscriptionService;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {

        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        List<Lesson> lessons = subscriptionService.getAllLessonsForSubscription(UUID.fromString(buttonData.getData()));

        EditMessageReplyMarkup editMessageReplyMarkup = editMessageReplyMarkup(
                update.getCallbackQuery().getMessage().getChatId().toString(),
                update.getCallbackQuery().getMessage().getMessageId(),
                getLessonsInlineButtons(lessons, buttonData.getData())
        );

        return List.of(editMessageReplyMarkup);
    }

    private InlineKeyboardMarkup getLessonsInlineButtons(List<Lesson> lessons, String subscriptionId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.instance();

        createInlineButtonInBuilderRow(
                InlineButtonCommand.MAIN_MENU_SUBSCRIPTION,
                subscriptionId,
                0,
                BACK_TO_MAIN_MENU_TEXT,
                builder
        );

        for (Lesson lesson : lessons) {
            createInlineButtonInBuilderRow(
                    InlineButtonCommand.GET_MORE_LESSON_INFO,
                    lesson.getId().toString(),
                    0,
                    String.format(LESSON_DATA_PATTERN, lesson.getTopic()),
                    builder
            );
        }

        inlineKeyboardMarkup.setKeyboard(builder.build().getKeyboard());
        return inlineKeyboardMarkup;
    }

    @Override
    public BotCommand getCommandObject() {
        return new BotCommand(InlineButtonCommand.GET_MORE_SUBSCRIPTION_INFO, "");
    }
}
