package core.english.mse2023.handler.impl.info;

import core.english.mse2023.aop.annotation.handler.AllRoles;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.component.InlineKeyboardMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@TextCommandType
@AllRoles
@RequiredArgsConstructor
public class GetAllSubscriptionsHandler implements Handler {

    private static final String NO_FAMILY_SUBSCRIPTIONS_TEXT = "В вашей семье нет подписок.";
    private static final String NO_TEACHER_SUBSCRIPTIONS_TEXT = "Нет подписок, в которых вы являетесь преподавателем.";
    private static final String NO_STUDENT_SUBSCRIPTIONS_TEXT = "Нет подписок, в которых вы являетесь студентом.";
    private static final String NO_SUBSCRIPTIONS_TEXT = "В системе нет подписок";

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

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");


    private final SubscriptionService subscriptionService;

    private final InlineKeyboardMaker inlineKeyboardMaker;

    @Override
    public List<BotApiMethod<?>> handle(Update update, UserRole userRole) {

        List<Subscription> subscriptions;

        if (userRole == UserRole.PARENT) {
            subscriptions = subscriptionService.getAllSubscriptionsInFamily(update.getMessage().getFrom().getId().toString());
        } else if (userRole == UserRole.TEACHER) {
            subscriptions = subscriptionService.getAllSubscriptionsWithTeacher(update.getMessage().getFrom().getId().toString());
        } else if (userRole == UserRole.STUDENT) {
            subscriptions = subscriptionService.getAllSubscriptionsWithStudent(update.getMessage().getFrom().getId().toString());
        } else {
            subscriptions = subscriptionService.getAllSubscriptions();
        }

        return createMessagesWithButton(subscriptions, update.getMessage().getChatId().toString(), userRole);
    }

    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.GET_ALL_SUBSCRIPTIONS;
    }

    private List<BotApiMethod<?>> createMessagesWithButton(List<Subscription> subscriptions, String chatId, UserRole userRole) {

        if (subscriptions.size() == 0) {
            if (userRole == UserRole.PARENT) {
                return List.of(SendMessage.builder()
                        .chatId(chatId)
                        .text(NO_FAMILY_SUBSCRIPTIONS_TEXT)
                        .build());
            } else if (userRole == UserRole.TEACHER) {
                return List.of(SendMessage.builder()
                        .chatId(chatId)
                        .text(NO_TEACHER_SUBSCRIPTIONS_TEXT)
                        .build());
            } else if (userRole == UserRole.STUDENT) {
                return List.of(SendMessage.builder()
                        .chatId(chatId)
                        .text(NO_STUDENT_SUBSCRIPTIONS_TEXT)
                        .build());
            } else {
                return List.of(SendMessage.builder()
                        .chatId(chatId)
                        .text(NO_SUBSCRIPTIONS_TEXT)
                        .build());
            }

        }

        List<BotApiMethod<?>> messages = new ArrayList<>();

        for (Subscription subscription : subscriptions) {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(String.format(DATA_PATTERN,
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
                    ))
                    .build();

            message.setReplyMarkup(inlineKeyboardMaker.getSubscriptionMainMenu(subscription.getId().toString(), userRole));

            messages.add(message);
        }

        return messages;
    }


}
