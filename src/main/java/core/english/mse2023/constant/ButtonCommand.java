package core.english.mse2023.constant;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;


public interface ButtonCommand {

    BotCommand START = new BotCommand("/start", "Start");

    BotCommand TO_MAIN_MENU = new BotCommand("/toMainMenu", "В главное меню");

    BotCommand STATISTICS = new BotCommand("/statistics", "Статистика");

    BotCommand SCHOOL_STATISTICS = new BotCommand("/schoolStatistics", "Статистика по школе");
    BotCommand TEACHER_STATISTICS = new BotCommand("/teacherStatistics", "Статистика по преподавателю");
    BotCommand STUDENT_STATISTICS = new BotCommand("/studentStatistics", "Статистика по ученику");

    BotCommand DATA = new BotCommand("/data", "Работа с данными");

    BotCommand ASSIGN_ROLE = new BotCommand("/assignRole", "Назначить роль");

    BotCommand ASSIGN_ROLE_FOR_GUEST = new BotCommand("/assignRoleForGuest", "Назначить роль гостю");

    BotCommand GET_ALL_TEACHERS = new BotCommand("/getAllTeachers", "Все учителя");
    BotCommand GET_ALL_STUDENTS = new BotCommand("/getAllStudents", "Все студенты");

    BotCommand SET_PARENT_FOR_STUDENT = new BotCommand("/setParentForStudent", "Назначить родителя ученику");

    BotCommand GET_ALL_SUBSCRIPTIONS = new BotCommand("/getAllSubscriptions", "Вывести абонементы");
    BotCommand GET_ALL_UNFINISHED_TASKS = new BotCommand("/getAllUnfinishedTasks", "Вывести предстоящие ИДЗ");

    BotCommand CREATE_SUBSCRIPTION = new BotCommand("/createSubscription", "Создать абонемент");

    BotCommand WORK_WITH_SUBSCRIPTIONS = new BotCommand("/workWithSubscriptions", "Работа с абонементами");

}
