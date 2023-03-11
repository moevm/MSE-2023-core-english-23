package core.english.mse2023.hadler;

import core.english.mse2023.constant.Command;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.UserService;
import core.english.mse2023.state.State;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChangeRoleToAdminHandler implements Handler {

    private static final String SUCCESS_TEXT = "Ваша роль изменена на: %s";
    private static final String FAIL_TEXT = "Невозможно сменить роль на такую же, как у вас.";

    private final UserService service;

    @Override
    public List<SendMessage> handle(Update update) {

        boolean result = service.changeUserRole(update, UserRole.ADMIN);

        SendMessage message;

        if (!result) {
            message = createMessage(update.getMessage().getChatId().toString(), FAIL_TEXT);
        } else {
            message = createMessage(update.getMessage().getChatId().toString(), String.format(SUCCESS_TEXT, UserRole.ADMIN));
        }

        return List.of(message);
    }

    @Override
    public String getCommand() {
        return Command.CHANGE_ROLE_TO_ADMIN;
    }

    @Override
    public List<SendMessage> update(Update update, State state) {
        throw new RuntimeException("The " + getClass() + " doesn't support update method.");
    }

    @Override
    public boolean needsUserInteraction() {
        return false;
    }

    @Override
    public State getInitialState() {
        throw new RuntimeException("The " + getClass() + " doesn't support states.");
    }

    @Override
    public void cleanUp(String id) {
        throw new RuntimeException("The " + getClass() + " doesn't support cleanup method.");
    }
}
