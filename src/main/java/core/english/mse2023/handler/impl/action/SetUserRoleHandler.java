package core.english.mse2023.handler.impl.action;

import core.english.mse2023.aop.annotation.handler.AllRoles;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.component.ReplyKeyboardMaker;
import core.english.mse2023.constant.Command;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.UserService;
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

@Component
@AllRoles
@InlineButtonType
@RequiredArgsConstructor
public class SetUserRoleHandler implements Handler {

    @Value("${messages.handlers.set-user-role.user-role-successfully-changed-message}")
    private String userRoleSuccessfullyChangedMessageText;

    @Value("${messages.handlers.set-user-role.failed-to-change-user-role-message}")
    private String failedToChangeUserRoleMessageText;

    private final ReplyKeyboardMaker replyKeyboardMaker;

    private final UserService service;

    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {

        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());
        UserRole newRole = UserRole.valueOf(buttonData.getData());

        List<PartialBotApiMethod<?>> actions = new ArrayList<>();

        if (userRole == newRole) {
            SendMessage message = SendMessage.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .text(failedToChangeUserRoleMessageText)
                    .build();

            actions.add(message);
        } else {
            service.changeUserRole(update.getCallbackQuery().getFrom().getId().toString(), newRole);
            SendMessage message = SendMessage.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .text(String.format(userRoleSuccessfullyChangedMessageText, newRole))
                    .replyMarkup(replyKeyboardMaker.getMainMenuKeyboard(newRole))
                    .build();

            actions.add(message);
        }

        actions.add(new AnswerCallbackQuery(update.getCallbackQuery().getId()));

        return actions;
    }

    @Override
    public Command getCommandObject() {
        return InlineButtonCommand.SET_USER_ROLE;
    }
}
