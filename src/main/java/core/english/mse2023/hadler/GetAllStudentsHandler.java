package core.english.mse2023.hadler;

import core.english.mse2023.constant.Command;
import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllStudentsHandler implements Handler {

    private static final String START_TEXT = "Список зарегистрированных студентов:\n%s";
    private static final String NO_STUDENTS_TEXT = "Зарегистрированные студенты отсутствуют в системе.";
    private static final String USER_DATA_PATTERN = " - %s%s";

    private final UserService service;

    @Override
    public List<SendMessage> handle(Update update) {

        List<User> students = service.getAllStudents();

        SendMessage sendMessage;

        if (students.isEmpty()) {
            sendMessage = createMessage(update, NO_STUDENTS_TEXT);
        } else {
            sendMessage = createMessage(update, String.format(START_TEXT, getStudentsDataText(students)));
        }

        return List.of(sendMessage);
    }

    private String getStudentsDataText(List<User> students) {
        StringBuilder stringBuilder = new StringBuilder();

        for (User student : students) {

            stringBuilder.append(
                    String.format(USER_DATA_PATTERN,
                            (student.getLastName() != null) ? (student.getLastName() + " ") : "", // Student's last name if present
                            student.getName() // Student's name (always present)
                    )
            );
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public String getCommand() {
        return Command.GET_ALL_STUDENTS;
    }
}
