package core.english.mse2023.constant;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;


public interface ButtonCommand {

    BotCommand START = new BotCommand("/start", "Start");

    BotCommand TO_MAIN_MENU = new BotCommand("/toMainMenu", "В главное меню");

    BotCommand STATISTICS = new BotCommand("/statistics", "Статистика");
    BotCommand CHILDREN_STATISTICS = new BotCommand("/statistics", "Статистика по детям");
    BotCommand MY_STATISTICS = new BotCommand("/myStatistics", "Моя статистика");

    BotCommand SCHOOL_STATISTICS = new BotCommand("/schoolStatistics", "Статистика по школе");
    BotCommand TEACHER_STATISTICS = new BotCommand("/teacherStatistics", "Статистика по преподавателю");
    BotCommand STUDENT_STATISTICS = new BotCommand("/studentStatistics", "Статистика по ученику");

    BotCommand DATA = new BotCommand("/data", "Работа с данными");

    BotCommand ASSIGN_ROLE = new BotCommand("/assignRole", "Назначить роль");

    BotCommand GET_ALL_TEACHERS = new BotCommand("/getAllTeachers", "Все учителя");
    BotCommand GET_ALL_STUDENTS = new BotCommand("/getAllStudents", "Все студенты");
    BotCommand GET_ALL_SUBSCRIPTIONS = new BotCommand("/getAllSubscriptions", "Вывести абонементы");

    BotCommand CREATE_SUBSCRIPTION = new BotCommand("/createSubscription", "Создать абонемент");

    BotCommand WORK_WITH_SUBSCRIPTIONS = new BotCommand("/workWithSubscriptions", "Работа с абонементами");

}
