package core.english.mse2023.service;


import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    @Transactional
    public User create(Update update) {
        String telegramId = update.getMessage().getFrom().getId().toString();

        User user = repository.findByTelegramId(telegramId);

        if (user == null) {
            user = new User();
            user.setName(update.getMessage().getFrom().getFirstName());
            user.setLastName(update.getMessage().getFrom().getLastName());
            user.setTelegramId(telegramId);
            user.setRole(UserRole.STUDENT);

            repository.save(user);
        }

        return user;
    }

    public List<User> getAllStudents() {
        return repository.findAllByRole(UserRole.STUDENT);
    }

}
