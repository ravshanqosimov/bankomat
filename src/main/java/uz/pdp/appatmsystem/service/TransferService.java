package uz.pdp.appatmsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.appatmsystem.entity.Card;
import uz.pdp.appatmsystem.entity.User;
import uz.pdp.appatmsystem.enums.RoleName;
import uz.pdp.appatmsystem.payload.ApiResponse;
import uz.pdp.appatmsystem.payload.LoginDto;
import uz.pdp.appatmsystem.payload.TransferDto;
import uz.pdp.appatmsystem.payload.VerifyCardDto;
import uz.pdp.appatmsystem.repository.CardRepository;
import uz.pdp.appatmsystem.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TransferService {
    @Autowired
    MyAuthService myAuthService;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    Map<String, Integer> hm = new HashMap<>();

    public ApiResponse getMoney(TransferDto transferDto, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        if (token == null)
//        token = token.substring(7);
//        String email = jwtProvider.getEmailFromToken(token);
//        Optional<User> optionalUser = userRepository.findByEmail(email);
            return new ApiResponse("yaxsh", true);
        return new ApiResponse("yaxshmas", false);
    }


    public ApiResponse verifyCard(VerifyCardDto cardDto) {
        Optional<Card> optionalCard1 = cardRepository.findByNumber(cardDto.getNumber());
        if (!optionalCard1.isPresent() || !optionalCard1.get().isStatus())
            return new ApiResponse("karta yaroqsiz, yoki bloklangan", false);
        Card card = optionalCard1.get();
        boolean b = false;
        if (!card.getPassword().equals(cardDto.getPassword())) {
            for (Map.Entry<String, Integer> entry : hm.entrySet()) {
                if (entry.getKey().equals(cardDto.getNumber())) {
                    b = true;
                    if (entry.getValue() == 2) {
                        card.setStatus(false);
                        cardRepository.save(card);
                        return new ApiResponse("kartangiz bloklandi", false);
                    } else entry.setValue(entry.getValue() + 1);
                    break;
                }
            }
            if (!b) {
                hm.put(cardDto.getNumber(), 1);
                return new ApiResponse("parol xato. Qaytadan kiriting", false);
            }
        }


        User user1 = new User();
        user1.setEmail(cardDto.getNumber() + "@gmail.com");
        user1.setPassword(passwordEncoder.encode(cardDto.getPassword()));
        user1.setRoleName(RoleName.USER);
        userRepository.save(user1);
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(cardDto.getNumber() + "@gmail.com");
        loginDto.setPassword(cardDto.getPassword());

        return myAuthService.login(loginDto);
//        return new ApiResponse("yamac", true);


    }
}
