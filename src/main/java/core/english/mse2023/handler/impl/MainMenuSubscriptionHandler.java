package core.english.mse2023.handler.impl;

import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.util.builder.InlineKeyboardBuilder;
import core.english.mse2023.util.factory.TelegramInlineButtonsUtils;
import core.english.mse2023.util.factory.TelegramMessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Component
@InlineButtonType
@RequiredArgsConstructor
public class MainMenuSubscriptionHandler implements Handler {

    private static final String BUTTON_TEXT = "Подробнее";

    private static final String CANCEL_SUBSCRIPTION_TEXT = "Отменить подписку";


    @Override
    public List<BotApiMethod<?>> handle(Update update) {
        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        return List.of(TelegramMessageUtils.editMessageReplyMarkup(
                update.getCallbackQuery().getMessage().getChatId().toString(),
                update.getCallbackQuery().getMessage().getMessageId(),
                getMarkupWithInlineButton(buttonData.getData())
        ));
    }

    @Override
    public BotCommand getCommandObject() {
        return new BotCommand(InlineButtonCommand.MAIN_MENU_SUBSCRIPTION, "");
    }

    private InlineKeyboardMarkup getMarkupWithInlineButton(String subscriptionId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.instance();

        builder
                .button(TelegramInlineButtonsUtils.createInlineButton(
                        InlineButtonCommand.GET_MORE_SUBSCRIPTION_INFO,
                        subscriptionId,
                        0,
                        BUTTON_TEXT
                ))
                .row()
                .button(TelegramInlineButtonsUtils.createInlineButton(
                        InlineButtonCommand.CANCEL_SUBSCRIPTION,
                        subscriptionId,
                        0,
                        CANCEL_SUBSCRIPTION_TEXT
                ))
                .row();

        inlineKeyboardMarkup.setKeyboard(builder.build().getKeyboard());
        return inlineKeyboardMarkup;
    }
}
