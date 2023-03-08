package core.english.mse2023.repository;

import core.english.mse2023.model.User;
import core.english.mse2023.model.dictionary.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByTelegramId(String telegramId);

    List<User> findAllByRole(UserRole role);
}
