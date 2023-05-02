package core.english.mse2023.service;

import core.english.mse2023.exception.NoSuchUserException;
import core.english.mse2023.model.Family;
import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.UserRole;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.UUID;

public interface UserService {

    void setChatIdForUser(String userTelegramId, String chatId);

    User getUserOrCreateNewOne(Update update);

    List<User> getAllStudents();

    List<User> getAllParents();

    List<User> getAllTeachers();

    UserRole getUserRole(String telegramId) throws NoSuchUserException;

    boolean changeUserRole(String telegramId, UserRole role);

    Family setParentForStudent(String studentTelegramId, String parentTelegramId);
}
