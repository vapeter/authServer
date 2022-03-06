package eu.vargapeter.demo.repository;

import eu.vargapeter.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {

    public Optional<User> findUserByEmail(String email);

}
