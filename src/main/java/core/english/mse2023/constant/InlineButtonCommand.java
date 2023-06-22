package core.english.mse2023.constant;

public enum InlineButtonCommand implements Command {
    GET_MORE_SUBSCRIPTION_INFO("/subscriptionGetMore", "Подробнее"),
    GET_MORE_LESSON_INFO("/lessonGetMore", "%s"),
    CANCEL_SUBSCRIPTION("/cancelSubscription", "Отменить абонемент"),
    MAIN_MENU_SUBSCRIPTION("/mainMenuSubscription", "◄ Назад в главное меню ◄"),

    SET_LESSON_ATTENDED("/setLessonAttended", "Посетил"),
    SET_LESSON_SKIPPED("/setLessonSkipped", "Пропустил"),
    SET_HOMEWORK_COMPLETED("/setHomeworkCompleted", "ДЗ сделано"),
    SET_HOMEWORK_NOT_COMPLETED("/setHomeworkNotCompleted", "ДЗ не сделано"),
    MAIN_MENU_LESSON("/mainMenuLesson", "◄ Назад в главное меню ◄"),
    GET_ATTENDANCE_MENU("/getAttendanceMenu", "Отметить посещение"),

    SET_USER_ROLE("/setUserRole", ""),
    GET_MORE_USER_INFO("/getMoreUserInfo", ""),

    GET_CANCEL_COMMENT("/getCancelComment", "Показать причину отмены занятия"),
    FINISH_LESSON("/finishLesson", "Закончить урок"),

    SET_COMMENT_FOR_PARENT("/setCommentForParent", "Оставить комментарий Родителю"),
    SHOW_COMMENT_FOR_PARENT("/showCommentForParent", "Показать комментарий для Родителя"),
    SET_HOMEWORK_COMMENT("/setHomeworkComment", "Оставить домашнее задание (комментарий)"),
    SHOW_HOMEWORK_COMMENT("/showHomeworkComment", "Показать домашнее задание (комментарий)"),

    SET_MARK_MENU("/setMarkMenu", "Добавить оценку за занятие"),
    SET_MARK("/setMark", "Добавить оценку за занятие"),
    SHOW_MARK("/showMark", "Показать оценку за занятие"),

    CHANGE_LESSON_DATA("/changeLessonData", "Изменить данные занятия"),
    SET_LESSON_DATE("/setLessonDate", "Назначить дату"),
    CANCEL_LESSON("/cancelLesson", "Отменить урок"),
    CREATE_LESSON("/createLesson", "Добавить урок"),

    SET_FAMILY_COMMENT("/setFamilyComment", "Дать отзыв");


    private final String command;
    private final String description;

    InlineButtonCommand(String command, String description) {
        this.command = command;
        this.description = description;
    }


    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
