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
        User user = service.getUserByTelegramId(update.getMessage().getFrom().getId().toString());

        SendMessage message = new SendMessage();

        if (user == null) {
            user = service.create(update);

            message.setChatId(String.valueOf(update.getMessage().getChatId()));
            message.setText("User successfully created! " + user.getName() + " " + user.getLastName() + " " + user.getTelegramId() + " " + user.getRole());
        } else {
            message.setChatId(String.valueOf(update.getMessage().getChatId()));
            message.setText("User has been found! " + user.getName() + " " + user.getLastName() + " " + user.getTelegramId() + " " + user.getRole());
        }

        return List.of(message);
    }

    @Override
    public String getCommand() {
        return Command.START;
    }
}
