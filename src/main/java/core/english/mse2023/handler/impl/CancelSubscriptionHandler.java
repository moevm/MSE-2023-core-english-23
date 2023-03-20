package core.english.mse2023.handler.impl;

import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.service.SubscriptionService;
import core.english.mse2023.util.factory.TelegramMessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;
import java.util.UUID;

@Component
@InlineButtonType
@RequiredArgsConstructor
public class CancelSubscriptionHandler implements Handler {

    private final static String DONE_TEXT = "Подписка отменена";

    private final SubscriptionService subscriptionService;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {

        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        subscriptionService.cancelSubscription(UUID.fromString(buttonData.getData()));

        return List.of(TelegramMessageUtils.createMessage(
                update.getCallbackQuery().getMessage().getChatId().toString(),
                DONE_TEXT
        ));
    }

    @Override
    public BotCommand getCommandObject() {
        return new BotCommand(InlineButtonCommand.CANCEL_SUBSCRIPTION, "");
    }
}
