package core.english.mse2023.component;

import core.english.mse2023.constant.ButtonCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ReplyKeyboardMaker {

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(ButtonCommand.STUDY.getDescription()));
        row.add(new KeyboardButton(ButtonCommand.SUBSCRIPTION.getDescription()));
        row.add(new KeyboardButton(ButtonCommand.ASSIGN_ROLE.getDescription()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row);

        return getReplyKeyboardMarkup(keyboard);
    }

    public ReplyKeyboardMarkup getStudyKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(ButtonCommand.FEEDBACK_ON_LESSON.getDescription()));
        row.add(new KeyboardButton(ButtonCommand.ADD_INFO_ABOUT_LESSON.getDescription()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(new KeyboardRow(Collections.singletonList(new KeyboardButton(ButtonCommand.STATISTICS.getDescription()))));
        keyboard.add(row);
        keyboard.add(new KeyboardRow(Collections.singletonList(new KeyboardButton(ButtonCommand.TO_MAIN_MENU.getDescription()))));

        return getReplyKeyboardMarkup(keyboard);
    }

    public ReplyKeyboardMarkup getStatisticsKeyboard() {

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(new KeyboardRow(Collections.singletonList(new KeyboardButton(ButtonCommand.SCHOOL_STATISTICS.getDescription()))));
        keyboard.add(new KeyboardRow(Collections.singletonList(new KeyboardButton(ButtonCommand.TEACHER_STATISTICS.getDescription()))));
        keyboard.add(new KeyboardRow(Collections.singletonList(new KeyboardButton(ButtonCommand.STUDENT_STATISTICS.getDescription()))));
        keyboard.add(new KeyboardRow(Collections.singletonList(new KeyboardButton(ButtonCommand.TO_STUDY_MENU.getDescription()))));

        return getReplyKeyboardMarkup(keyboard);
    }

    public ReplyKeyboardMarkup getTeacherStatisticsKeyboard() {
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(new KeyboardRow(Collections.singletonList(new KeyboardButton(ButtonCommand.WEEK_TEACHER_STAT.getDescription()))));
        keyboard.add(new KeyboardRow(Collections.singletonList(new KeyboardButton(ButtonCommand.MONTH_TEACHER_STAT.getDescription()))));
        keyboard.add(new KeyboardRow(Collections.singletonList(new KeyboardButton(ButtonCommand.TO_STATISTICS_MENU.getDescription()))));

        return getReplyKeyboardMarkup(keyboard);
    }

    public ReplyKeyboardMarkup getStudentStatisticsKeyboard() {
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(new KeyboardRow(Collections.singletonList(new KeyboardButton(ButtonCommand.WEEK_STUDENT_STAT.getDescription()))));
        keyboard.add(new KeyboardRow(Collections.singletonList(new KeyboardButton(ButtonCommand.MONTH_STUDENT_STAT.getDescription()))));
        keyboard.add(new KeyboardRow(Collections.singletonList(new KeyboardButton(ButtonCommand.TO_STATISTICS_MENU.getDescription()))));

        return getReplyKeyboardMarkup(keyboard);
    }

    public ReplyKeyboardMarkup getFullStatisticsKeyboard() {
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(new KeyboardRow(Collections.singletonList(new KeyboardButton(ButtonCommand.WEEK_FULL_STAT.getDescription()))));
        keyboard.add(new KeyboardRow(Collections.singletonList(new KeyboardButton(ButtonCommand.MONTH_FULL_STAT.getDescription()))));
        keyboard.add(new KeyboardRow(Collections.singletonList(new KeyboardButton(ButtonCommand.TO_STATISTICS_MENU.getDescription()))));

        return getReplyKeyboardMarkup(keyboard);
    }

    public ReplyKeyboardMarkup getSubscriptionKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        row1.add(new KeyboardButton(ButtonCommand.SUBSCRIPTION_INFO.getDescription()));
        row1.add(new KeyboardButton(ButtonCommand.CANCEL_SUBSCRIPTION.getDescription()));
        row1.add(new KeyboardButton(ButtonCommand.CREATE_SUBSCRIPTION.getDescription()));

        row2.add(new KeyboardButton(ButtonCommand.CARRY_LESSON.getDescription()));
        row2.add(new KeyboardButton(ButtonCommand.CANCEL_LESSON.getDescription()));
        row2.add(new KeyboardButton(ButtonCommand.TO_MAIN_MENU.getDescription()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        return getReplyKeyboardMarkup(keyboard);
    }

    public ReplyKeyboardMarkup getAssignRoleKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();

        row1.add(new KeyboardButton(ButtonCommand.CHANGE_ROLE_TO_ADMIN.getDescription()));
        row1.add(new KeyboardButton(ButtonCommand.CHANGE_ROLE_TO_TEACHER.getDescription()));

        row2.add(new KeyboardButton(ButtonCommand.CHANGE_ROLE_TO_PARENT.getDescription()));
        row2.add(new KeyboardButton(ButtonCommand.CHANGE_ROLE_TO_STUDENT.getDescription()));

        row3.add(new KeyboardButton(ButtonCommand.TO_MAIN_MENU.getDescription()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

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
