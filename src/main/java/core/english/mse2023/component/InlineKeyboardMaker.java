package core.english.mse2023.component;


import core.english.mse2023.constant.ButtonCommand;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.LessonInfo;
import core.english.mse2023.model.dictionary.LessonStatus;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.util.builder.InlineKeyboardBuilder;
import core.english.mse2023.util.utilities.TelegramInlineButtonsUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Component
public class InlineKeyboardMaker {


    public InlineKeyboardMarkup getLessonMainMenuInlineKeyboard(Lesson lesson, LessonInfo lessonInfo, UserRole role) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.instance();

        switch (role) {
            case STUDENT -> {
                if (lessonInfo.getHomeworkCompleted() != null) {
                    if (lessonInfo.getHomeworkCompleted()) {
                        builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                        InlineButtonCommand.SET_HOMEWORK_NOT_COMPLETED,
                                        lesson.getId().toString(),
                                        0
                                ))
                                .row();
                    } else {
                        builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                        InlineButtonCommand.SET_HOMEWORK_COMPLETED,
                                        lesson.getId().toString(),
                                        0
                                ))
                                .row();
                    }

                }
                switch (lesson.getStatus()) {
                    case ENDED -> {
                        builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.GET_LESSON_RESULTS,
                                lesson.getId().toString(),
                                0
                        )).row();

                        if (lessonInfo.getTeacherComment() != null) {
                            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                    InlineButtonCommand.SHOW_HOMEWORK_COMMENT,
                                    lesson.getId().toString(),
                                    0
                            )).row();
                        }
                    }
                    case CANCELLED_BY_TEACHER, CANCELLED_BY_STUDENT, CANCELLED -> {
                        builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.GET_CANCEL_COMMENT,
                                lesson.getId().toString(),
                                0
                        )).row();
                    }
                    case NOT_STARTED_YET -> {
                        builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                        InlineButtonCommand.CANCEL_LESSON,
                                        lesson.getId().toString(),
                                        0
                                ))
                                .row();
                    }
                }
            }
            case PARENT -> {
                switch (lesson.getStatus()) {
                    case ENDED -> {
                        builder
                                .button(TelegramInlineButtonsUtils.createInlineButton(
                                        InlineButtonCommand.GET_LESSON_RESULTS,
                                        lesson.getId().toString(),
                                        0
                                )).row();

                        if (lessonInfo.getTeacherCommentForParent() != null){
                            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                    InlineButtonCommand.SHOW_COMMENT_FOR_PARENT,
                                    lesson.getId().toString(),
                                    0
                            )).row();
                        }

                        if (lessonInfo.getTeacherComment() != null) {
                            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                    InlineButtonCommand.SHOW_HOMEWORK_COMMENT,
                                    lesson.getId().toString(),
                                    0
                            )).row();
                        }

                    }
                    case CANCELLED_BY_TEACHER, CANCELLED_BY_STUDENT, CANCELLED -> {
                        builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.GET_CANCEL_COMMENT,
                                lesson.getId().toString(),
                                0
                        )).row();
                    }
                }

                builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.SET_FAMILY_COMMENT,
                                lesson.getId().toString(),
                                0
                        ))
                        .row();
            }
            case TEACHER -> {
                switch (lesson.getStatus()) {
                    case ENDED -> {
                        builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.GET_LESSON_RESULTS,
                                lesson.getId().toString(),
                                0
                        )).row();

                        if (lessonInfo.getTeacherCommentForParent() != null) {
                            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                    InlineButtonCommand.SHOW_COMMENT_FOR_PARENT,
                                    lesson.getId().toString(),
                                    0
                            )).row();
                        } else {
                            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                    InlineButtonCommand.SET_COMMENT_FOR_PARENT,
                                    lesson.getId().toString(),
                                    0
                            )).row();
                        }

                        if (lessonInfo.getTeacherComment() != null) {
                            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                    InlineButtonCommand.SHOW_HOMEWORK_COMMENT,
                                    lesson.getId().toString(),
                                    0
                            )).row();
                        } else {
                            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                    InlineButtonCommand.SET_HOMEWORK_COMMENT,
                                    lesson.getId().toString(),
                                    0
                            )).row();
                        }
                    }
                    case CANCELLED_BY_TEACHER, CANCELLED_BY_STUDENT, CANCELLED -> {
                        builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.GET_CANCEL_COMMENT,
                                lesson.getId().toString(),
                                0
                        )).row();
                    }
                    case NOT_STARTED_YET -> {
                        builder
                                .button(TelegramInlineButtonsUtils.createInlineButton(
                                        InlineButtonCommand.CHANGE_LESSON_DATA,
                                        lesson.getId().toString(),
                                        0
                                ))
                                .row()
                                .button(TelegramInlineButtonsUtils.createInlineButton(
                                        InlineButtonCommand.FINISH_LESSON,
                                        lesson.getId().toString(),
                                        0
                                ))
                                .row();
                    }
                }
            }
            case ADMIN -> {
                switch (lesson.getStatus()) {
                    case ENDED -> {
                        builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.GET_LESSON_RESULTS,
                                lesson.getId().toString(),
                                0
                        )).row();

                        if (lessonInfo.getTeacherCommentForParent() != null) {
                            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                    InlineButtonCommand.SHOW_COMMENT_FOR_PARENT,
                                    lesson.getId().toString(),
                                    0
                            )).row();
                        } else {
                            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                    InlineButtonCommand.SET_COMMENT_FOR_PARENT,
                                    lesson.getId().toString(),
                                    0
                            )).row();
                        }

                        if (lessonInfo.getTeacherComment() != null) {
                            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                    InlineButtonCommand.SHOW_HOMEWORK_COMMENT,
                                    lesson.getId().toString(),
                                    0
                            )).row();
                        } else {
                            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                    InlineButtonCommand.SET_HOMEWORK_COMMENT,
                                    lesson.getId().toString(),
                                    0
                            )).row();
                        }
                    }
                    case CANCELLED_BY_TEACHER, CANCELLED_BY_STUDENT, CANCELLED -> {
                        builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                InlineButtonCommand.GET_CANCEL_COMMENT,
                                lesson.getId().toString(),
                                0
                        )).row();
                    }
                    case NOT_STARTED_YET -> {
                        builder
                                .button(TelegramInlineButtonsUtils.createInlineButton(
                                        InlineButtonCommand.CHANGE_LESSON_DATA,
                                        lesson.getId().toString(),
                                        0
                                ))
                                .row()
                                .button(TelegramInlineButtonsUtils.createInlineButton(
                                        InlineButtonCommand.CANCEL_LESSON,
                                        lesson.getId().toString(),
                                        0
                                ))
                                .row()
                                .button(TelegramInlineButtonsUtils.createInlineButton(
                                        InlineButtonCommand.FINISH_LESSON,
                                        lesson.getId().toString(),
                                        0
                                ))
                                .row();


                        if (lesson.getDate() != null) {
                            builder.button(TelegramInlineButtonsUtils.createInlineButtonWithDescriptionOverride(
                                            InlineButtonCommand.SET_LESSON_DATE,
                                            "Перенести",
                                            lesson.getId().toString(),
                                            0
                                    ))
                                    .row();
                        } else {
                            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                                            InlineButtonCommand.SET_LESSON_DATE,
                                            lesson.getId().toString(),
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

    public InlineKeyboardMarkup getSubscriptionLessonsMenu(List<Lesson> lessons, String subscriptionId, UserRole userRole) {
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

        if (userRole == UserRole.ADMIN || userRole == UserRole.TEACHER) {
            builder.button(TelegramInlineButtonsUtils.createInlineButton(
                            InlineButtonCommand.CREATE_LESSON,
                            subscriptionId,
                            0
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
