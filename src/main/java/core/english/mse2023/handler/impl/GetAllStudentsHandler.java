package core.english.mse2023.handler.impl;

import core.english.mse2023.aop.annotation.handler.AllRoles;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.User;
import core.english.mse2023.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@TextCommandType
@AllRoles
@RequiredArgsConstructor
public class GetAllStudentsHandler implements Handler {

    private static final String START_TEXT = "Список зарегистрированных студентов:";
    private static final String NO_STUDENTS_TEXT = "Зарегистрированные студенты отсутствуют в системе.";
    private static final String USER_DATA_PATTERN = "%s%s";

    private final UserService service;

    @Override
    public List<BotApiMethod<?>> handle(Update update) {

        List<User> students = service.getAllStudents();

        SendMessage sendMessage;

        if (students.isEmpty()) {
            sendMessage = SendMessage.builder()
                    .chatId(update.getMessage().getChatId().toString())
                    .text(NO_STUDENTS_TEXT)
                    .build();
        } else {
            sendMessage = SendMessage.builder()
                    .chatId(update.getMessage().getChatId().toString())
                    .text(START_TEXT)
                    .replyMarkup(getStudentsButtons(students))
                    .build();
        }


        return List.of(sendMessage);
    }

    private InlineKeyboardMarkup getStudentsButtons(List<User> students) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (User student : students) {
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();

            // TODO: set appropriate data for callback
            button.setCallbackData(InlineButtonDTOEncoder.encode(InlineButtonDTO.builder()
                    .command(null)
                    .stateIndex(0)
                    .data(null)
                    .build()));
            button.setText(String.format(USER_DATA_PATTERN,
                    (student.getLastName() != null) ? (student.getLastName() + " ") : "", // Student's last name if present
                    student.getName() // Student's name (always present)
            ));

            keyboardRow.add(button);

            keyboard.add(keyboardRow);
        }


        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }

    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.GET_ALL_STUDENTS;
    }
}
