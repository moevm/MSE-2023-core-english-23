package core.english.mse2023.service.impl;


import core.english.mse2023.exception.UserDoesNotExistsException;
import core.english.mse2023.exception.UserAlreadyExistsException;
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

    private final UserRepository repository;
    private final FamilyRepository familyRepository;

    @Override
    @Transactional
    public User getUserByTelegramId(String telegramId) {
        return repository.findByTelegramId(telegramId);
    }

    @Override
    @Transactional
    public List<Family> getAllFamiliesWithParent(String parentTelegramId) {
        return familyRepository.getAllByParent(getUserByTelegramId(parentTelegramId));
    }

    @Override
    @Transactional
    public List<User> getAllStudents() {
        return repository.findAllByRole(UserRole.STUDENT);
    }

    @Override
    @Transactional
    public List<User> getAllTeachers() {
        return repository.findAllByRole(UserRole.TEACHER);
    }


    @Override
    @Transactional
    public User createUser(String telegramId, String firstName, String lastName) throws UserAlreadyExistsException {
        User user = repository.findByTelegramId(telegramId);

        if (user != null)
            throw new UserAlreadyExistsException(String.format("User with telegram id \"%s\" already exists.", telegramId));

        user = User.builder()
                .name(firstName)
                .lastName(lastName)
                .telegramId(telegramId)
                .role(UserRole.GUEST)
                .build();

        repository.save(user);

        return user;
    }


    @Override
    @Transactional
    public UserRole getUserRole(String telegramId) throws UserDoesNotExistsException {
        User user = repository.findByTelegramId(telegramId);

        if (user == null)
            throw new UserDoesNotExistsException(String.format("User with telegram id %s hasn't been found", telegramId));

        return user.getRole();
    }

    @Override
    @Transactional
    public void changeUserRole(String telegramId, UserRole role) throws UserDoesNotExistsException {
        User user = repository.findByTelegramId(telegramId);

        if (user == null)
            throw new UserDoesNotExistsException(String.format("User with telegram id %s hasn't been found", telegramId));

        user.setRole(role);
    }

    @Override
    @Transactional
    public void setChatIdForUser(String userTelegramId, String chatId) {
        User user = repository.findByTelegramId(userTelegramId);
        user.setChatId(chatId);
    }

}
