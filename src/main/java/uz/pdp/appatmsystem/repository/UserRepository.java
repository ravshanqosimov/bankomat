package uz.pdp.appatmsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appatmsystem.entity.User;

import javax.validation.constraints.Email;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(@Email String email);

//    Optional<User> findByEmail(@Email String email)
}
