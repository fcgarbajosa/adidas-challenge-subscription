package es.adidas.challenge.publicc.network.security.services;

import es.adidas.challenge.publicc.network.security.repositories.UserRepository;
import es.adidas.challenge.publicc.network.security.repositories.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Boolean existsByUserName(String userName){
        return userRepository.existsByUsername(userName);
    }

    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public void save(User user){
        userRepository.save(user);
    }

    public List<User> getUsuarios(){

        return userRepository.findAll();
    }

    public List<User> getUsuariosDynamic(User user, ExampleMatcher matcher){

        return userRepository.findAll(Example.of(user, matcher));
    }

    public void deleteByUsuario(String nombreUsuario){

        Optional<User> usuario = userRepository.findByUsername(nombreUsuario);
        userRepository.delete(usuario.get());
    }
}
