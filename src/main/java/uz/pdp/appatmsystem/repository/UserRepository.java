package uz.pdp.appatmsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appatmsystem.entity.User;
import uz.pdp.appatmsystem.enums.RoleName;

import javax.validation.constraints.Email;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(@Email String email);
    Optional<User> findByRoleName(RoleName roleName);

    void deleteAllByRoleName(RoleName roleName);

    boolean existsByEmail(@Email String email);
}
