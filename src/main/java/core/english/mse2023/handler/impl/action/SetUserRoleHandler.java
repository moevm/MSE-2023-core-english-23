package core.english.mse2023.handler.impl.action;

import core.english.mse2023.aop.annotation.handler.AllRoles;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.component.ReplyKeyboardMaker;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;

import java.util.List;

@Component
@AllRoles
@InlineButtonType
@RequiredArgsConstructor
public class SetUserRoleHandler implements Handler {

    private final static String USER_ROLE_SUCCESSFULLY_CHANGED_MESSAGE_TEXT = "Ваша роль изменена на: %s";
    private final static String FAILED_TO_CHANGE_USER_ROLE_MESSAGE_TEXT = "Невозможно сменить роль на такую же, как у вас.";

    private final ReplyKeyboardMaker replyKeyboardMaker;

    private final UserService service;

    @Override
    public List<BotApiMethod<?>> handle(Update update, UserRole userRole) {

        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());
        UserRole newRole = UserRole.valueOf(buttonData.getData());

        boolean result = service.changeUserRole(update.getCallbackQuery().getFrom().getId().toString(), newRole);

        SendMessage message = result ?
                SendMessage.builder()
                        .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                        .text(String.format(USER_ROLE_SUCCESSFULLY_CHANGED_MESSAGE_TEXT, newRole))
                        .replyMarkup(replyKeyboardMaker.getMainMenuKeyboard(newRole))
                        .build()
                :
                SendMessage.builder()
                        .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                        .text(FAILED_TO_CHANGE_USER_ROLE_MESSAGE_TEXT)
                        .build();



        return List.of(message, new AnswerCallbackQuery(update.getCallbackQuery().getId()));
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.SET_USER_ROLE;
    }
}
