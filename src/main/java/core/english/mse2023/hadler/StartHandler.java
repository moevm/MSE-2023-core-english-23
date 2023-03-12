package core.english.mse2023.hadler;

import core.english.mse2023.constant.Command;
import core.english.mse2023.hadler.interfaces.Handler;
import core.english.mse2023.model.User;
import core.english.mse2023.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StartHandler implements Handler {

    private static final String GREETING = "Добро пожаловать, %s. Ваша роль: %s";


    private final UserService service;

    @Override
    public List<SendMessage> handle(Update update) {
        User user = service.create(update);

        return List.of(createMessage(update.getMessage().getChatId().toString(), String.format(GREETING, user.getName(), user.getRole())));
    }

    @Override
    public String getCommand() {
        return Command.START;
    }
}
