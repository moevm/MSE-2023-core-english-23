package core.english.mse2023.service.impl;


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
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;

    @Override
    @Transactional
    public void setChatIdForUser(String userTelegramId, String chatId) {
        User user = repository.findByTelegramId(userTelegramId);
        user.setChatId(chatId);
    }

    @Transactional
    @Override
    public User getUserOrCreateNewOne(Update update) {
        String telegramId = update.getMessage().getFrom().getId().toString();

        User user = userRepository.findByTelegramId(telegramId);

        if (user == null) {
            user = new User();
            user.setName(update.getMessage().getFrom().getFirstName());
            user.setLastName(update.getMessage().getFrom().getLastName());
            user.setTelegramId(telegramId);
            user.setRole(UserRole.GUEST);
            user.setChatId(update.getMessage().getChatId().toString());

            userRepository.save(user);
        }

        return user;
    }

    @Override
    @Transactional
    public List<User> getAllStudents() {
        return userRepository.findAllByRole(UserRole.STUDENT);
    }

    @Override
    @Transactional
    public List<User> getAllParents() {
        return userRepository.findAllByRole(UserRole.PARENT);
    }

    @Override
    @Transactional
    public List<User> getAllTeachers() {
        return userRepository.findAllByRole(UserRole.TEACHER);
    }

    @Override
    @Transactional
    public UserRole getUserRole(String telegramId) throws NoSuchUserException {
        User user = userRepository.findByTelegramId(telegramId);

        if (user == null)
            throw new NoSuchUserException(String.format("User with telegram id %s hasn't been found", telegramId));

        return user.getRole();
    }

    @Override
    @Transactional
    public boolean changeUserRole(String telegramId, UserRole role) {
        boolean roleHasBeenChanged = false;

        User user = userRepository.findByTelegramId(telegramId);

        if (user.getRole() != role) {
            user.setRole(role);
            userRepository.save(user);

            roleHasBeenChanged = true;
        }

        return roleHasBeenChanged;
    }

    @Override
    @Transactional
    public Family setParentForStudent(String studentTelegramId, String parentTelegramId) {
        User student = userRepository.findByTelegramId(studentTelegramId);
        User parent = userRepository.findByTelegramId(parentTelegramId);

        Family family = familyRepository.getByStudent(student);

        family.setParent(parent);

        return family;
    }

}
