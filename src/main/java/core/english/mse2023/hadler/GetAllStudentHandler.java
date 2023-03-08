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
public class GetAllStudentHandler implements Handler {

    private static final String START_TEXT = "Список зарегестрированных студентов:";
    private static final String USER_DATA_PATTERN = " - %s";

    private final UserService service;

    @Override
    public List<SendMessage> handle(Update update) {

        List<User> students = service.getAllUsersOneType(UserRole.STUDENT);

        return List.of(createMessage(update, START_TEXT + "\n" + getStudentsDataText(students)));
    }

    private String getStudentsDataText(List<User> students) {
        StringBuilder stringBuilder = new StringBuilder();

        for (User student : students) {

            stringBuilder.append(
                    String.format(USER_DATA_PATTERN, (student.getLastName() != null) ? (student.getLastName() + " ") : "" + student.getName())
            );
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    private SendMessage createMessage(Update update, String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        message.setText(msg);

        return message;
    }

    @Override
    public String getCommand() {
        return Command.GET_ALL_STUDENT;
    }
}
