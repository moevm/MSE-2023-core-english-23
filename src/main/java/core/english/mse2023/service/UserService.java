package core.english.mse2023.service;

import core.english.mse2023.exception.UserAlreadyExistsException;
import core.english.mse2023.exception.UserDoesNotExistsException;
import core.english.mse2023.model.Family;
import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.UserRole;

import java.util.List;

public interface UserService {

    /**
     * Gets User object by its Telegram ID
     * @param telegramId User's telegram ID
     * @return User object
     */
    User getUserByTelegramId(String telegramId);

    /**
     * Gets all families with this parent
     * @param parentTelegramId Parent's telegram ID
     * @return List of families
     */
    List<Family> getAllFamiliesWithParent(String parentTelegramId);


    /**
     * Gets all Students in database
     * @return All students
     */
    List<User> getAllStudents();

    /**
     * Gets all Parents in database
     * @return All parents
     */
    List<User> getAllParents();


    /**
     * Gets all Teachers in database
     * @return All teachers
     */
    List<User> getAllTeachers();


    /**
     * Creates user
     * @param telegramId New user telegram ID
     * @param firstName New user first name
     * @param lastName New user second name
     * @return Newly created user
     * @throws UserAlreadyExistsException If user with given telegram ID already exists
     */
    User createUser(String telegramId, String firstName, String lastName) throws UserAlreadyExistsException;


    /**
     * Gets user's role by telegram ID
     * @param telegramId User's telegram ID
     * @return Found Role
     * @throws UserDoesNotExistsException If user with given UUID doesn't exist
     */
    UserRole getUserRole(String telegramId) throws UserDoesNotExistsException;

    /**
     * Changes user's role by telegram ID
     * @param telegramId User's telegram ID
     * @param role New role
     * @throws UserDoesNotExistsException If user with given UUID doesn't exist
     */
    void changeUserRole(String telegramId, UserRole role) throws UserDoesNotExistsException;

    /**
     * Sets user's chat id by telegram ID
     * @param telegramId User's telegram ID
     * @param chatId ID of chat
     */
    void setChatIdForUser(String telegramId, String chatId);

    /**
     * Sets parent to family with particular student
     * @param studentTelegramId Student Telegram ID
     * @param parentTelegramId Parent Telegram ID
     * @return Family that has been changed
     */
    Family setParentForStudent(String studentTelegramId, String parentTelegramId);
}
