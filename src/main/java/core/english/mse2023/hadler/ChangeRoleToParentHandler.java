package core.english.mse2023.hadler;

import core.english.mse2023.constant.Command;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChangeRoleToParentHandler implements Handler {

    private static final String SUCCESS_TEXT = "Ваша роль изменена на: %s";
    private static final String FAIL_TEXT = "Невозможно сменить роль на такую же, как у вас.";

    private final UserService service;

    @Override
    public List<SendMessage> handle(Update update) {

        boolean result = service.changeUserRole(update, UserRole.PARENT);

        SendMessage message;

        if (!result) {
            message = createMessage(update, FAIL_TEXT);
        } else {
            message = createMessage(update, String.format(SUCCESS_TEXT, UserRole.PARENT));
        }

        return List.of(message);
    }

    @Override
    public String getCommand() {
        return Command.CHANGE_ROLE_TO_PARENT;
    }
}