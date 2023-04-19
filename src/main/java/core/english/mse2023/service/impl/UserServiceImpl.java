package core.english.mse2023.service.impl;


import core.english.mse2023.exception.NoSuchUserException;
import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.repository.UserRepository;
import core.english.mse2023.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Transactional
    @Override
    public User getUserOrCreateNewOne(Update update) {
        String telegramId = update.getMessage().getFrom().getId().toString();

        User user = repository.findByTelegramId(telegramId);

        if (user == null) {
            user = new User();
            user.setName(update.getMessage().getFrom().getFirstName());
            user.setLastName(update.getMessage().getFrom().getLastName());
            user.setTelegramId(telegramId);
            user.setRole(UserRole.GUEST);

            repository.save(user);
        }

        return user;
    }

    @Override
    public List<User> getAllStudents() {
        return repository.findAllByRole(UserRole.STUDENT);
    }

    @Override
    public List<User> getAllTeachers() {
        return repository.findAllByRole(UserRole.TEACHER);
    }

    @Override
    public UserRole getUserRole(String telegramId) throws NoSuchUserException {
        User user = repository.findByTelegramId(telegramId);

        if (user == null)
            throw new NoSuchUserException(String.format("User with telegram id %s hasn't been found", telegramId));

        return user.getRole();
    }

    @Override
    public boolean changeUserRole(String telegramId, UserRole role) {
        boolean roleHasBeenChanged = false;

        User user = repository.findByTelegramId(telegramId);

        if (user.getRole() != role) {
            user.setRole(role);
            repository.save(user);

            roleHasBeenChanged = true;
        }

        return roleHasBeenChanged;
    }

}
