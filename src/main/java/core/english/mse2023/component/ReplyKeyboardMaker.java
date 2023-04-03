package core.english.mse2023.component;

import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.exception.IllegalUserRoleForMenu;
import core.english.mse2023.model.dictionary.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class ReplyKeyboardMaker {

    public ReplyKeyboardMarkup getMainMenuKeyboard(UserRole role) {
        List<KeyboardRow> keyboard = new ArrayList<>();

        switch (role) {
            case STUDENT -> {
                KeyboardRow row = new KeyboardRow();
                row.add(new KeyboardButton(ButtonCommand.MY_STATISTICS.getDescription()));
                row.add(new KeyboardButton(ButtonCommand.GET_ALL_SUBSCRIPTIONS.getDescription()));
                row.add(new KeyboardButton(ButtonCommand.ASSIGN_ROLE.getDescription()));
                keyboard.add(row);
            }
            case PARENT -> {
                KeyboardRow row = new KeyboardRow();
                row.add(new KeyboardButton(ButtonCommand.CHILDREN_STATISTICS.getDescription()));
                row.add(new KeyboardButton(ButtonCommand.GET_ALL_SUBSCRIPTIONS.getDescription()));
                row.add(new KeyboardButton(ButtonCommand.ASSIGN_ROLE.getDescription()));
                keyboard.add(row);
            }
            case TEACHER -> {
                KeyboardRow row = new KeyboardRow();
                row.add(new KeyboardButton(ButtonCommand.STATISTICS.getDescription()));
                row.add(new KeyboardButton(ButtonCommand.GET_ALL_SUBSCRIPTIONS.getDescription()));
                row.add(new KeyboardButton(ButtonCommand.ASSIGN_ROLE.getDescription()));
                keyboard.add(row);
            }
            case ADMIN -> {
                KeyboardRow row = new KeyboardRow();
                row.add(new KeyboardButton(ButtonCommand.STATISTICS.getDescription()));
                row.add(new KeyboardButton(ButtonCommand.DATA.getDescription()));
                row.add(new KeyboardButton(ButtonCommand.ASSIGN_ROLE.getDescription()));
                keyboard.add(row);
            }
        }

        return getReplyKeyboardMarkup(keyboard);
    }

    public ReplyKeyboardMarkup getStatisticsMenu(UserRole role) {
        List<KeyboardRow> keyboard = new ArrayList<>();

        switch (role) {
            case STUDENT, PARENT -> {
                log.error(String.format("User with role %s tried to access statistics menu, but it shouldn't be accessible for his role.", role));
                throw new IllegalUserRoleForMenu("Statistics menu cannot be accessed by student or parent.");
            }
            case TEACHER -> {
                keyboard.add(new KeyboardRow(List.of(new KeyboardButton(
                        ButtonCommand.MY_STATISTICS.getDescription()
                ))));
                keyboard.add(new KeyboardRow(List.of(new KeyboardButton(
                        ButtonCommand.STUDENT_STATISTICS.getDescription()
                ))));
                keyboard.add(new KeyboardRow(List.of(new KeyboardButton(
                        ButtonCommand.TO_MAIN_MENU.getDescription()
                ))));
            }
            case ADMIN -> {
                keyboard.add(new KeyboardRow(List.of(new KeyboardButton(
                        ButtonCommand.SCHOOL_STATISTICS.getDescription()
                ))));
                keyboard.add(new KeyboardRow(List.of(new KeyboardButton(
                        ButtonCommand.TEACHER_STATISTICS.getDescription()
                ))));
                keyboard.add(new KeyboardRow(List.of(new KeyboardButton(
                        ButtonCommand.STUDENT_STATISTICS.getDescription()
                ))));
                keyboard.add(new KeyboardRow(List.of(new KeyboardButton(
                        ButtonCommand.TO_MAIN_MENU.getDescription()
                ))));
            }
        }

        return getReplyKeyboardMarkup(keyboard);
    }

    public ReplyKeyboardMarkup getDataMenu() {
        List<KeyboardRow> keyboard = new ArrayList<>();

        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(
                ButtonCommand.GET_ALL_TEACHERS.getDescription()
        ))));
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(
                ButtonCommand.GET_ALL_STUDENTS.getDescription()
        ))));
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(
                ButtonCommand.GET_ALL_SUBSCRIPTIONS.getDescription()
        ))));
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(
                ButtonCommand.CREATE_SUBSCRIPTION.getDescription()
        ))));
        keyboard.add(new KeyboardRow(List.of(new KeyboardButton(
                ButtonCommand.TO_MAIN_MENU.getDescription()
        ))));

        return getReplyKeyboardMarkup(keyboard);
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup(List<KeyboardRow> keyboardRows) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        return replyKeyboardMarkup;
    }
}
