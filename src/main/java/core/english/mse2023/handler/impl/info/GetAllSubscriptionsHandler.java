package core.english.mse2023.handler.impl.info;

import core.english.mse2023.aop.annotation.handler.AllRegisteredRoles;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.component.InlineKeyboardMaker;
import core.english.mse2023.component.MessageTextMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.constant.Command;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.Subscription;
import core.english.mse2023.model.dictionary.SubscriptionStatus;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@TextCommandType
@AllRegisteredRoles
@RequiredArgsConstructor
public class GetAllSubscriptionsHandler implements Handler {

    @Value("${messages.handlers.get-all-subscriptions.no-family-subscriptions}")
    private String noFamilySubscriptionsText;

    @Value("${messages.handlers.get-all-subscriptions.no-teacher-subscriptions}")
    private String noTeacherSubscriptionsText;

    @Value("${messages.handlers.get-all-subscriptions.no-student-subscriptions}")
    private String noStudentSubscriptionsText;

    @Value("${messages.handlers.get-all-subscriptions.no-subscriptions}")
    private String noSubscriptionsText;

    @Value("${messages.handlers.get-all-subscriptions.data-pattern}")
    private String dataPattern;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");


    private final SubscriptionService subscriptionService;

    private final InlineKeyboardMaker inlineKeyboardMaker;
    private final MessageTextMaker messageTextMaker;

    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {

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
    public Command getCommandObject() {
        return ButtonCommand.GET_ALL_SUBSCRIPTIONS;
    }

    private List<PartialBotApiMethod<?>> createMessagesWithButton(List<Subscription> subscriptions, String chatId, UserRole userRole) {

        if (subscriptions.size() == 0 || subscriptions.stream().allMatch((subscription -> subscription.getStatus() == SubscriptionStatus.CANCELLED))) {
            if (userRole == UserRole.PARENT) {
                return List.of(SendMessage.builder()
                        .chatId(chatId)
                        .text(noFamilySubscriptionsText)
                        .build());
            } else if (userRole == UserRole.TEACHER) {
                return List.of(SendMessage.builder()
                        .chatId(chatId)
                        .text(noTeacherSubscriptionsText)
                        .build());
            } else if (userRole == UserRole.STUDENT) {
                return List.of(SendMessage.builder()
                        .chatId(chatId)
                        .text(noStudentSubscriptionsText)
                        .build());
            } else {
                return List.of(SendMessage.builder()
                        .chatId(chatId)
                        .text(noSubscriptionsText)
                        .build());
            }

        }

        List<PartialBotApiMethod<?>> messages = new ArrayList<>();

        for (Subscription subscription : subscriptions) {

            if (subscription.getStatus() == SubscriptionStatus.CANCELLED)
                continue;

            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(String.format(dataPattern,
                            subscription.getType(),
                            subscription.getStatus(),
                            subscription.getLessonsRest(),
                            messageTextMaker.userDataPatternMessageText(subscription.getTeacher().getName(), subscription.getTeacher().getLastName()),
                            messageTextMaker.userDataPatternMessageText(subscription.getStudent().getName(), subscription.getStudent().getLastName()),
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
