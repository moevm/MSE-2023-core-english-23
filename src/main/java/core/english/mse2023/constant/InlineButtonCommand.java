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


}
