package core.english.mse2023.service.impl;


import core.english.mse2023.exception.UserAlreadyExistsException;
import core.english.mse2023.exception.NoSuchUserException;
import core.english.mse2023.model.Family;
import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.repository.FamilyRepository;
import core.english.mse2023.repository.UserRepository;
import core.english.mse2023.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;

    @Override
    @Transactional
    public User getUserByTelegramId(String telegramId) {
        return userRepository.findByTelegramId(telegramId);
    }

    @Override
    @Transactional
    public List<Family> getAllFamiliesWithParent(String parentTelegramId) {
        return familyRepository.getAllByParent(getUserByTelegramId(parentTelegramId));
    }

    @Override
    @Transactional
    public List<User> getAllStudents() {
        return userRepository.findAllByRole(UserRole.STUDENT);
    }

    @Override
    public List<User> getAllParents() {
        return userRepository.findAllByRole(UserRole.PARENT);
    }

    @Override
    public List<User> getAllGuests() {
        return userRepository.findAllByRole(UserRole.GUEST);
    }

    @Override
    @Transactional
    public List<User> getAllTeachers() {
        return userRepository.findAllByRole(UserRole.TEACHER);
    }


    @Override
    @Transactional
    public User createUser(String telegramId, String firstName, String lastName, String chatId) {
        User user = getUserByTelegramId(telegramId);

        if (user != null)
            throw new UserAlreadyExistsException(telegramId);

        user = User.builder()
                .name(firstName)
                .lastName(lastName)
                .telegramId(telegramId)
                .role(UserRole.STUDENT)
                .chatId(chatId)
                .build();

        userRepository.save(user);

        return user;
    }


    @Override
    @Transactional
    public UserRole getUserRole(String telegramId) {
        User user = getUserByTelegramId(telegramId);

        if (user == null)
            throw new NoSuchUserException(String.format("User with telegram id %s hasn't been found", telegramId));

        return user.getRole();
    }

    @Override
    @Transactional
    public void changeUserRole(String telegramId, UserRole role) {
        User user = getUserByTelegramId(telegramId);

        if (user == null)
            throw new NoSuchUserException(String.format("User with telegram id %s hasn't been found", telegramId));

        user.setRole(role);
    }

    @Override
    @Transactional
    public void setChatIdForUser(String telegramId, String chatId) {
        User user = getUserByTelegramId(telegramId);
        user.setChatId(chatId);
    }

    @Override
    @Transactional
    public Family setParentForStudent(String studentTelegramId, String parentTelegramId) {
        User student = getUserByTelegramId(studentTelegramId);
        User parent = getUserByTelegramId(parentTelegramId);

        Family family = familyRepository.getByStudent(student);

        if (family == null) {
            family = new Family();
            family.setStudent(student);
        }

        family.setParent(parent);

        return family;
    }

    @Override
    @Transactional
    public User setRoleForUser(String chosenGuestTelegramId, UserRole chosenRole) {
        User user = getUserByTelegramId(chosenGuestTelegramId);

        if (user == null)
            throw new NoSuchUserException(String.format("User with telegram id %s hasn't been found", chosenGuestTelegramId));

        user.setRole(chosenRole);

        return user;
    }

}
