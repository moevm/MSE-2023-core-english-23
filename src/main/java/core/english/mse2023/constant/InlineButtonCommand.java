package core.english.mse2023.constant;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

/**
 * Commands for inline buttons. Be mindful to make commands small enough to fit in 64 bytes in the InlineButton.
 * @see core.english.mse2023.dto.InlineButtonDTO
 */
public interface InlineButtonCommand {
    BotCommand GET_MORE_SUBSCRIPTION_INFO = new BotCommand("/subscriptionGetMore", "Подробнее");
    BotCommand GET_MORE_LESSON_INFO = new BotCommand("/lessonGetMore", "%s");
    BotCommand CANCEL_SUBSCRIPTION = new BotCommand("/cancelSubscription", "Отменить подписку");
    BotCommand MAIN_MENU_SUBSCRIPTION = new BotCommand("/mainMenuSubscription", "◄ Назад в главное меню ◄");

    BotCommand SET_LESSON_ATTENDED = new BotCommand("/setLessonAttended", "Посетил");
    BotCommand SET_LESSON_SKIPPED = new BotCommand("/setLessonSkipped", "Пропустил");
    BotCommand MAIN_MENU_LESSON = new BotCommand("/mainMenuLesson", "◄ Назад в главное меню ◄");
    BotCommand GET_ATTENDANCE_MENU = new BotCommand("/getAttendanceMenu", "Отметить посещение");


    BotCommand SET_USER_ROLE = new BotCommand("/setUserRole", "");

    BotCommand GET_MORE_USER_INFO = new BotCommand("/getMoreUserInfo", "");

    BotCommand GET_LESSON_RESULTS = new BotCommand("/getLessonResults", "Показать результаты занятия");
    BotCommand GET_CANCEL_COMMENT = new BotCommand("/getCancelComment", "Показать причину отмены занятия");
    BotCommand SET_LESSON_RESULTS = new BotCommand("/setLessonResults", "Установить результаты занятия");
    BotCommand SET_COMMENT_FOR_PARENT = new BotCommand("/setCommentForParent", "Оставить комментарий Родителю");
    BotCommand CHANGE_LESSON_DATA = new BotCommand("/changeLessonData", "Установить результаты занятия");
    BotCommand RESCHEDULE_LESSON = new BotCommand("/rescheduleLesson", "Перенести");
    BotCommand CANCEL_LESSON = new BotCommand("/cancelLesson", "Отменить урок");
    BotCommand SET_FAMILY_COMMENT = new BotCommand("/setFamilyComment", "Дать отзыв");
    BotCommand SET_LESSON_DATE = new BotCommand("/setLessonDate", "Назначить дату");


}
