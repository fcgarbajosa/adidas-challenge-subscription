package es.adidas.challenge.subscription;

import es.adidas.challenge.subscription.network.security.repositories.entities.ERole;
import es.adidas.challenge.subscription.network.security.repositories.entities.Role;
import es.adidas.challenge.subscription.network.security.repositories.entities.User;
import es.adidas.challenge.subscription.network.security.services.RoleService;
import es.adidas.challenge.subscription.network.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class SubscriptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubscriptionApplication.class, args);
    }

    @Autowired
    PasswordEncoder encoder;

    @Value("${adminUser}")
    private String adminUser;

    @Value("${adminPassword}")
    private String adminPassword;

    @Value("${adminPassword}")
    private String adminEmail;

    @Bean
    public CommandLineRunner init(RoleService roleService, UserService userService) {

        return (args) -> {
            if (roleService.findByName(ERole.ROLE_ADMIN).isEmpty())
                roleService.save(new Role(ERole.ROLE_ADMIN));
            if (roleService.findByName(ERole.ROLE_USER).isEmpty())
                roleService.save(new Role(ERole.ROLE_USER));
            if (!userService.existsByUserName(adminUser)) {

                Set<Role> roles = new HashSet<>();
                Role adminRole = roleService.findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(adminRole);

                User user = new User(adminUser,
                        adminEmail,
                        encoder.encode(adminPassword));
                user.setRoles(roles);
                userService.save(user);
            }
        };
    }
}
