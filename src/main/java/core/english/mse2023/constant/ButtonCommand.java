package core.english.mse2023.constant;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;


public interface ButtonCommand {

    BotCommand START = new BotCommand("/start", "Start");

    BotCommand TO_MAIN_MENU = new BotCommand("/toMainMenu", "В главное меню");

    BotCommand STUDY = new BotCommand("/study", "Учеба");
    BotCommand FEEDBACK_ON_LESSON = new BotCommand("/feedbackOnLesson", "Оставить отзыв о зянятии");
    BotCommand ADD_INFO_ABOUT_LESSON = new BotCommand("/addInfoAboutLesson", "Добавить информацию о занятии");
    BotCommand TO_STUDY_MENU = new BotCommand("/toStudyMenu", "В меню учебы");
    BotCommand STATISTICS = new BotCommand("/statistics", "Статистика");
    BotCommand TO_STATISTICS_MENU = new BotCommand("/toStatisticsMenu", "В меню статистики");
    BotCommand SCHOOL_STATISTICS = new BotCommand("/schoolStatistics", "Статистика по школе");
    BotCommand WEEK_FULL_STAT = new BotCommand("/weekFullStat", "Статистика по школе за неделю");
    BotCommand MONTH_FULL_STAT = new BotCommand("/monthFullStat", "Статистика по школе за месяц");
    BotCommand TEACHER_STATISTICS = new BotCommand("/teacherStatistics", "Статистика по преподавателю");
    BotCommand WEEK_TEACHER_STAT = new BotCommand("/weekTeacherStat", "Статистика по преподавателю за неделю");
    BotCommand MONTH_TEACHER_STAT = new BotCommand("/monthTeacherStat", "Статистика по преподавателю за месяц");
    BotCommand STUDENT_STATISTICS = new BotCommand("/studentStatistics", "Статистика по ученику");
    BotCommand WEEK_STUDENT_STAT = new BotCommand("/weekStudentStat", "Успеваемость за неделю");
    BotCommand MONTH_STUDENT_STAT = new BotCommand("/monthStudentStat", "Успеваемость за месяц");

    BotCommand SUBSCRIPTION = new BotCommand("/subscription", "Абонемент");
    BotCommand SUBSCRIPTION_INFO = new BotCommand("/subscriptionInfo", "Информация о подписке");
    BotCommand CARRY_LESSON = new BotCommand("/carryLesson", "Перенести занятие");
    BotCommand CANCEL_LESSON = new BotCommand("/cancelLesson", "Отменить занятие");
    BotCommand CANCEL_SUBSCRIPTION = new BotCommand("/cancelSubscription", "Отменить подписку");
    BotCommand CREATE_SUBSCRIPTION = new BotCommand("/createSubscription", "Создать подписку");

    BotCommand ASSIGN_ROLE = new BotCommand("/assignRole", "Назначить роль");
    BotCommand CHANGE_ROLE_TO_ADMIN = new BotCommand("/changeRoleToAdmin", "Назначить администратором");
    BotCommand CHANGE_ROLE_TO_PARENT = new BotCommand("/changeRoleToParent", "Назначить роль родителя");
    BotCommand CHANGE_ROLE_TO_STUDENT = new BotCommand("/changeRoleToStudent", "Назначить роль студента");
    BotCommand CHANGE_ROLE_TO_TEACHER = new BotCommand("/changeRoleToTeacher", "Назначить роль учителя");

    BotCommand GET_ALL_STUDENTS = new BotCommand("/getAllStudents", "Все студенты");
    BotCommand GET_ALL_TEACHERS = new BotCommand("/getAllTeachers", "Все учителя");
    BotCommand GET_ALL_SUBSCRIPTIONS = new BotCommand("/getAllSubscriptions", "Все абонементы");
}
