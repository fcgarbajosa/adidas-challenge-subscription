package es.adidas.challenge.subscription.business.repositories;

import es.adidas.challenge.subscription.business.repositories.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);


}
