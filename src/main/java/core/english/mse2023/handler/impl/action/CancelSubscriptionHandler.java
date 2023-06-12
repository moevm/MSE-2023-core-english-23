package core.english.mse2023.handler.impl.action;

import core.english.mse2023.aop.annotation.handler.AdminRole;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.aop.annotation.handler.TeacherRole;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.exception.SubscriptionAlreadyCanceledException;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@AdminRole
@TeacherRole
@InlineButtonType
@RequiredArgsConstructor
public class CancelSubscriptionHandler implements Handler {

    @Value("${messages.handlers.cancel-subscription.done}")
    private String doneText;

    @Value("${messages.handlers.cancel-subscription.already-canceled}")
    private String alreadyCanceledText;


    private final SubscriptionService subscriptionService;

    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {

        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        UUID subscriptionId = UUID.fromString(buttonData.getData());

        List<PartialBotApiMethod<?>> actions = new ArrayList<>();

        try {
            subscriptionService.cancelSubscription(subscriptionId);

            SendMessage message = SendMessage.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .text(doneText)
                    .build();

            actions.add(message);

        } catch (SubscriptionAlreadyCanceledException e) {
            SendMessage message = SendMessage.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .text(alreadyCanceledText)
                    .build();

            actions.add(message);
        }

        actions.add(new AnswerCallbackQuery(update.getCallbackQuery().getId()));

        return actions;
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.CANCEL_SUBSCRIPTION;
    }
}
