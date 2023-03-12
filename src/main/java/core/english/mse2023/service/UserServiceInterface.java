package core.english.mse2023.service;

import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.UserRole;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface UserServiceInterface {
    User create(Update update);

    List<User> getAllStudents();

    List<User> getAllTeachers();

    boolean changeUserRole(Update update, UserRole role);
}
