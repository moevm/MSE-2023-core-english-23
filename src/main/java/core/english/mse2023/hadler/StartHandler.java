package core.english.mse2023.hadler;

import core.english.mse2023.constant.Command;
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

    private final UserService service;

    @Override
    public List<SendMessage> handle(Update update) {
        User user = service.create(update);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        message.setText("Добро пожаловать, " + user.getName() + ". Ваша роль: " + user.getRole());

        return List.of(message);
    }

    @Override
    public String getCommand() {
        return Command.START;
    }
}
