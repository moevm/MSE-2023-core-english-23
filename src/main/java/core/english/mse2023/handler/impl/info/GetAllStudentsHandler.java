package core.english.mse2023.handler.impl.getall;

import core.english.mse2023.aop.annotation.handler.AdminRole;
import core.english.mse2023.aop.annotation.handler.AllRoles;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.UserService;
import core.english.mse2023.util.builder.InlineKeyboardBuilder;
import core.english.mse2023.util.utilities.TelegramInlineButtonsUtils;
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
@AdminRole
@RequiredArgsConstructor
public class GetAllStudentsHandler implements Handler {

    private static final String START_TEXT = "Список зарегистрированных студентов:";
    private static final String NO_STUDENTS_TEXT = "Зарегистрированные студенты отсутствуют в системе.";
    private static final String USER_DATA_PATTERN = "%s%s";

    private final UserService service;

    @Override
    public List<BotApiMethod<?>> handle(Update update, UserRole userRole) {

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

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.instance();

        for (User student : students) {
            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                            InlineButtonCommand.GET_MORE_USER_INFO.getCommand(),
                            student.getTelegramId(),
                            0,
                            String.format(USER_DATA_PATTERN,
                                    (student.getLastName() != null) ? (student.getLastName() + " ") : "", // Student's last name if present
                                    student.getName() // Student's name (always present)
                            )
                    ))
                    .row();
        }

        inlineKeyboardMarkup.setKeyboard(builder.build().getKeyboard());
        return inlineKeyboardMarkup;
    }

    @Override
    public BotCommand getCommandObject() {
        return ButtonCommand.GET_ALL_STUDENTS;
    }
}
