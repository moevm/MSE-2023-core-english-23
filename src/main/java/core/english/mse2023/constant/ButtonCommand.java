package core.english.mse2023.constant;

import lombok.Getter;

public enum ButtonCommand implements Command {

    START("/start", "Start"),
    TO_MAIN_MENU("/toMainMenu", "В главное меню"),

    STATISTICS("/statistics", "Статистика"),
    CHILDREN_STATISTICS("/statistics", "Статистика по детям"),
    MY_STATISTICS("/myStatistics", "Моя статистика"),

    SCHOOL_STATISTICS("/schoolStatistics", "Статистика по школе"),
    TEACHER_STATISTICS("/teacherStatistics", "Статистика по преподавателю"),
    STUDENT_STATISTICS("/studentStatistics", "Статистика по ученику"),

    DATA("/data", "Работа с данными"),
    ASSIGN_ROLE("/assignRole", "Назначить роль"),
    ASSIGN_ROLE_FOR_GUEST("/assignRoleForGuest", "Назначить роль гостю"),

    GET_ALL_TEACHERS("/getAllTeachers", "Все учителя"),
    GET_ALL_STUDENTS("/getAllStudents", "Все студенты"),

    SET_PARENT_FOR_STUDENT("/setParentForStudent", "Назначить родителя ученику"),

    GET_ALL_SUBSCRIPTIONS("/getAllSubscriptions", "Вывести абонементы"),
    GET_ALL_UNFINISHED_TASKS("/getAllUnfinishedTasks", "Вывести предстоящие ИДЗ"),

    CREATE_SUBSCRIPTION("/createSubscription", "Создать абонемент"),
    WORK_WITH_SUBSCRIPTIONS("/workWithSubscriptions", "Работа с абонементами");


    private final String command;
    private final String description;
    ButtonCommand(String command, String description) {
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
