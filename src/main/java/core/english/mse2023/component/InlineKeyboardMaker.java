package core.english.mse2023.component;


import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.dictionary.LessonStatus;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.util.builder.InlineKeyboardBuilder;
import core.english.mse2023.util.utilities.TelegramInlineButtonsUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Component
public class InlineKeyboardMaker {


    public InlineKeyboardMarkup getLessonMainMenuInlineKeyboard(String lessonId, LessonStatus lessonStatus, boolean hasDate, UserRole role) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.instance();

        switch (role) {
            case STUDENT -> {
                switch (lessonStatus) {
                    case ENDED -> {
                        builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.GET_LESSON_RESULTS,
                                lessonId,
                                0
                        )).row();
                    }
                    case CANCELLED_BY_TEACHER, CANCELLED_BY_STUDENT, CANCELLED -> {
                        builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.GET_CANCEL_COMMENT,
                                lessonId,
                                0
                        )).row();
                    }
                    case NOT_STARTED_YET -> {
                        builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                        InlineButtonCommand.CANCEL_LESSON,
                                        lessonId,
                                        0
                                ))
                                .row();
                    }
                }
            }
            case PARENT -> {
                switch (lessonStatus) {
                    case ENDED -> {
                        builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.GET_LESSON_RESULTS,
                                lessonId,
                                0
                        )).row();
                    }
                    case CANCELLED_BY_TEACHER, CANCELLED_BY_STUDENT, CANCELLED -> {
                        builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.GET_CANCEL_COMMENT,
                                lessonId,
                                0
                        )).row();
                    }
                }

                builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.SET_FAMILY_COMMENT,
                                lessonId,
                                0
                        ))
                        .row();
            }
            case TEACHER, ADMIN -> {
                switch (lessonStatus) {
                    case ENDED -> {
                        builder
                                .button(TelegramInlineButtonsUtils.createInlineButton(
                                        InlineButtonCommand.GET_LESSON_RESULTS,
                                        lessonId,
                                        0
                                )).row()
                                .button(TelegramInlineButtonsUtils.createInlineButton(
                                        InlineButtonCommand.SET_COMMENT_FOR_PARENT,
                                        lessonId,
                                        0
                                )).row();
                    }
                    case CANCELLED_BY_TEACHER, CANCELLED_BY_STUDENT, CANCELLED -> {
                        builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.GET_CANCEL_COMMENT,
                                lessonId,
                                0
                        )).row();
                    }
                    case NOT_STARTED_YET -> {
                        builder
                                .button(TelegramInlineButtonsUtils.createInlineButton(
                                        InlineButtonCommand.CHANGE_LESSON_DATA,
                                        lessonId,
                                        0
                                ))
                                .row()
                                .button(TelegramInlineButtonsUtils.createInlineButton(
                                        InlineButtonCommand.CANCEL_LESSON,
                                        lessonId,
                                        0
                                ))
                                .row()
                                .button(TelegramInlineButtonsUtils.createInlineButton(
                                        InlineButtonCommand.FINISH_LESSON,
                                        lessonId,
                                        0
                                ))
                                .row();

                        if (hasDate) {
                            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                            InlineButtonCommand.RESCHEDULE_LESSON,
                                            lessonId,
                                            0
                                    ))
                                    .row();
                        } else {
                            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                            InlineButtonCommand.SET_LESSON_DATE,
                                            lessonId,
                                            0
                                    ))
                                    .row();
                        }
                    }
                }
            }
        }

        inlineKeyboardMarkup.setKeyboard(builder.build().getKeyboard());

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getLessonAttendanceMenu(String lessonId) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.setKeyboard(
                InlineKeyboardBuilder.instance()
                        .button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.MAIN_MENU_LESSON,
                                lessonId,
                                0
                        ))
                        .row()
                        .button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.SET_LESSON_ATTENDED,
                                lessonId,
                                0
                        ))
                        .row()
                        .button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.SET_LESSON_SKIPPED,
                                lessonId,
                                0
                        ))
                        .row()
                        .build().getKeyboard()
        );

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getSubscriptionLessonsMenu(List<Lesson> lessons, String subscriptionId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.instance()
                .button(TelegramInlineButtonsUtils.createInlineButton(
                        InlineButtonCommand.MAIN_MENU_SUBSCRIPTION,
                        subscriptionId,
                        0
                ))
                .row();

        for (Lesson lesson : lessons) {
            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                            InlineButtonCommand.GET_MORE_LESSON_INFO.getCommand(),
                            lesson.getId().toString(),
                            0,
                            String.format(InlineButtonCommand.GET_MORE_LESSON_INFO.getDescription(), lesson.getTopic())
                    ))
                    .row();
        }

        inlineKeyboardMarkup.setKeyboard(builder.build().getKeyboard());
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getSubscriptionMainMenu(String subscriptionId, UserRole userRole) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.instance()
                .button(TelegramInlineButtonsUtils.createInlineButton(
                        InlineButtonCommand.GET_MORE_SUBSCRIPTION_INFO,
                        subscriptionId,
                        0
                ))
                .row();

        switch (userRole) {
            case TEACHER, ADMIN -> {
                builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.CANCEL_SUBSCRIPTION,
                                subscriptionId,
                                0
                        ))
                        .row();
            }
        }

        inlineKeyboardMarkup.setKeyboard(builder.build().getKeyboard());

        return inlineKeyboardMarkup;
    }

}
