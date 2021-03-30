package es.adidas.challenge.email.network.security.services;

import es.adidas.challenge.email.network.security.repositories.RoleRepository;
import es.adidas.challenge.email.network.security.repositories.entities.ERole;
import es.adidas.challenge.email.network.security.repositories.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Optional<Role> findByName(ERole name) {
        return roleRepository.findByName(name);
    }
    public void save(Role role){
        roleRepository.save(role);
    }
}
