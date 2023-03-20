package core.english.mse2023.repository;

import core.english.mse2023.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Subscription getSubscriptionsById(UUID subscriptionId);
}
