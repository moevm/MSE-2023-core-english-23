package core.english.mse2023.repository;

import core.english.mse2023.model.Family;
import core.english.mse2023.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FamilyRepository extends JpaRepository<Family, UUID> {

    List<Family> getAllByParent(User parent);
}
