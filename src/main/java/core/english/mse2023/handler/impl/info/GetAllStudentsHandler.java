package core.english.mse2023.handler.impl.info;

import core.english.mse2023.aop.annotation.handler.AdminRole;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.component.MessageTextMaker;
import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.UserService;
import core.english.mse2023.util.builder.InlineKeyboardBuilder;
import core.english.mse2023.util.utilities.TelegramInlineButtonsUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Component
@TextCommandType
@AdminRole
@RequiredArgsConstructor
public class GetAllStudentsHandler implements Handler {

    @Value("${messages.handlers.get-all-students.start}")
    private String startText;

    @Value("${messages.handlers.get-all-students.no-students}")
    private String noStudentsText;

    private final MessageTextMaker messageTextMaker;

    private final UserService service;

    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {

        List<User> students = service.getAllStudents();

        SendMessage sendMessage;

        if (students.isEmpty()) {
            sendMessage = SendMessage.builder()
                    .chatId(update.getMessage().getChatId().toString())
                    .text(noStudentsText)
                    .build();
        } else {
            sendMessage = SendMessage.builder()
                    .chatId(update.getMessage().getChatId().toString())
                    .text(startText)
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
                            messageTextMaker.userDataPatternMessageText(student.getName(), student.getLastName())
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
