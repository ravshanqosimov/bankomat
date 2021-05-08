package uz.pdp.appatmsystem.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.appatmsystem.entity.User;
import uz.pdp.appatmsystem.enums.RoleName;
import uz.pdp.appatmsystem.repository.UserRepository;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;


    @Override
    public void run(String... args) throws Exception {

        if (initialMode.equals("always")) {
            User user = new User();
            user.setFullName("Botir Saidov");
            user.setEmail("direktor@gmail.com");
            user.setPassword(passwordEncoder.encode("pdp123"));
            user.setRoleName(RoleName.DIRECTOR);
            userRepository.save(user);
 User worker = new User();
            worker.setFullName("Olim Saidov");
            worker.setEmail("xodim@gmail.com");
            worker.setPassword(passwordEncoder.encode("123"));
            worker.setRoleName(RoleName.WORKER);
            userRepository.save(worker);




        }
    }
}
