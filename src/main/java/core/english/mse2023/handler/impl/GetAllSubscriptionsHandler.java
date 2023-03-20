package core.english.mse2023.handler.impl;

import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.service.SubscriptionService;
import core.english.mse2023.util.builder.InlineKeyboardBuilder;
import core.english.mse2023.util.factory.TelegramInlineButtonsUtils;
import core.english.mse2023.util.factory.TelegramMessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@TextCommandType
@RequiredArgsConstructor
public class GetAllSubscriptionsHandler implements Handler {


    private static final String WELCOME_TEXT = "Список подписок в системе:";
    private static final String DATA_PATTERN = """
            Тип: %s
            Статус: %s
            Уроков осталось: %d
                        
            Учитель: %s
            Студент: %s
                        
            Дата начала: %s,
            Дата завершения: %s
            """;

    private static final String USER_DATA_PATTERN = "%s%s";

    private static final String BUTTON_TEXT = "Подробнее";

    private static final String CANCEL_SUBSCRIPTION_TEXT = "Отменить подписку";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");


    private final SubscriptionService subscriptionService;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {

        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();

        return createMessagesWithButton(subscriptions, update.getMessage().getChatId().toString());
    }

    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.GET_ALL_SUBSCRIPTIONS;
    }

    private List<BotApiMethod<?>> createMessagesWithButton(List<Subscription> subscriptions, String chatId) {
        List<BotApiMethod<?>> messages = new ArrayList<>();

        messages.add(TelegramMessageUtils.createMessage(chatId, WELCOME_TEXT));

        for (Subscription subscription : subscriptions) {
            SendMessage message = TelegramMessageUtils.createMessage(
                    chatId,
                    String.format(DATA_PATTERN,
                            subscription.getType(),
                            subscription.getStatus(),
                            subscription.getLessonsRest(),
                            String.format(USER_DATA_PATTERN,
                                    (subscription.getTeacher().getLastName() != null) ? (subscription.getTeacher().getLastName() + " ") : "", // Teacher's last name if present
                                    subscription.getTeacher().getName() // Teacher's name (always present)
                            ),
                            String.format(USER_DATA_PATTERN,
                                    (subscription.getStudent().getLastName() != null) ? (subscription.getStudent().getLastName() + " ") : "", // Student's last name if present
                                    subscription.getStudent().getName() // Student's name (always present)
                            ),
                            dateFormat.format(subscription.getStartDate()),
                            dateFormat.format(subscription.getEndDate())
                    ),
                    null
            );

            message.setReplyMarkup(getMarkupWithInlineButton(subscription.getId()));

            messages.add(message);
        }

        return messages;
    }

    private InlineKeyboardMarkup getMarkupWithInlineButton(UUID subscriptionId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.instance();

        builder
                .button(TelegramInlineButtonsUtils.createInlineButton(
                        InlineButtonCommand.GET_MORE_SUBSCRIPTION_INFO,
                        subscriptionId.toString(),
                        0,
                        BUTTON_TEXT
                ))
                .row();

        builder
                .button(TelegramInlineButtonsUtils.createInlineButton(
                        InlineButtonCommand.CANCEL_SUBSCRIPTION,
                        subscriptionId.toString(),
                        0,
                        CANCEL_SUBSCRIPTION_TEXT
                ))
                .row();

        inlineKeyboardMarkup.setKeyboard(builder.build().getKeyboard());
        return inlineKeyboardMarkup;
    }
}
