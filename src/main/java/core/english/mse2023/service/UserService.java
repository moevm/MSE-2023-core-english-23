package core.english.mse2023.service;

import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.UserRole;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(Update update);

    List<User> getAllStudents();

    List<User> getAllTeachers();

    Optional<User> getUser(UUID id);

    boolean changeUserRole(Update update, UserRole role);
}
