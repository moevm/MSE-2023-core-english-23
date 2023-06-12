package core.english.mse2023.handler.impl.info;

import core.english.mse2023.aop.annotation.handler.AllRoles;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.util.builder.InlineKeyboardBuilder;
import core.english.mse2023.util.utilities.TelegramInlineButtonsUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Component
@AllRoles
@TextCommandType
@RequiredArgsConstructor
public class AssignRoleHandler implements Handler {

    @Value("${messages.handlers.assign-role.message}")
    private String message;

    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {
        return List.of(SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(message)
                .replyMarkup(getRolesButtons())
                .build());
    }

    private InlineKeyboardMarkup getRolesButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.instance();

        for (UserRole role : UserRole.values()) {
            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                            InlineButtonCommand.SET_USER_ROLE.getCommand(),
                            role.toString(),
                            0,
                            role.getString()
                    ))
                    .row();
        }

        inlineKeyboardMarkup.setKeyboard(builder.build().getKeyboard());
        return inlineKeyboardMarkup;
    }

    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.ASSIGN_ROLE;
    }


}

