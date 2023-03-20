package core.english.mse2023.handler.impl;

import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.User;
import core.english.mse2023.service.UserService;
import core.english.mse2023.util.factory.TelegramMessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

@Component
@TextCommandType
@RequiredArgsConstructor
public class GetAllTeachersHandler implements Handler {

    private static final String START_TEXT = "Список зарегистрированных преподавателей:\n%s";
    private static final String NO_TEACHERS_TEXT = "Зарегистрированные преподаватели отсутствуют в системе.";
    private static final String USER_DATA_PATTERN = " - %s%s";

    private final UserService service;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {

        List<User> teachers = service.getAllTeachers();

        SendMessage sendMessage;

        if (teachers.isEmpty()) {
            sendMessage = TelegramMessageUtils.createMessage(update.getMessage().getChatId().toString(), NO_TEACHERS_TEXT);
        } else {
            sendMessage = TelegramMessageUtils.createMessage(update.getMessage().getChatId().toString(), String.format(START_TEXT, getTeachersDataText(teachers)));
        }

        return List.of(sendMessage);
    }

    private String getTeachersDataText(List<User> teachers) {
        StringBuilder stringBuilder = new StringBuilder();

        for (User teacher : teachers) {

            stringBuilder.append(
                    String.format(USER_DATA_PATTERN,
                            (teacher.getLastName() != null) ? (teacher.getLastName() + " ") : "", // Teacher's last name if present
                            teacher.getName() // Teacher's name (always present)
                    )
            );
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.GET_ALL_TEACHERS;
    }

}
