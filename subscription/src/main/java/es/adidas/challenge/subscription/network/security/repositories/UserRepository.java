package es.adidas.challenge.subscription.network.security.repositories;

import es.adidas.challenge.subscription.network.security.repositories.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username, String password);
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
