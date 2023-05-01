package core.english.mse2023.service;

import core.english.mse2023.exception.UserDoesNotExistsException;
import core.english.mse2023.exception.UserAlreadyExistsException;
import core.english.mse2023.model.Family;
import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.UserRole;

import java.util.List;

public interface UserService {


    User getUserByTelegramId(String telegramId);

    List<Family> getAllFamiliesWithParent(String parentTelegramId);



    List<User> getAllStudents();

    List<User> getAllTeachers();



    User createUser(String telegramId, String firstName, String lastName) throws UserAlreadyExistsException;



    UserRole getUserRole(String telegramId) throws UserDoesNotExistsException;

    void changeUserRole(String telegramId, UserRole role) throws UserDoesNotExistsException;

    void setChatIdForUser(String userTelegramId, String chatId);
}
