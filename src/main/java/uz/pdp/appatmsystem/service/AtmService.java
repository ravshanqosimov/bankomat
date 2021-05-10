package uz.pdp.appatmsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.appatmsystem.component.Calculation;
import uz.pdp.appatmsystem.entity.ATM;
import uz.pdp.appatmsystem.entity.AtmBox;
import uz.pdp.appatmsystem.entity.Card;
import uz.pdp.appatmsystem.entity.User;
import uz.pdp.appatmsystem.enums.CardType;
import uz.pdp.appatmsystem.enums.RoleName;
import uz.pdp.appatmsystem.payload.ApiResponse;
import uz.pdp.appatmsystem.payload.AtmDto;
import uz.pdp.appatmsystem.payload.CardDto;
import uz.pdp.appatmsystem.payload.LoginDto;
import uz.pdp.appatmsystem.repository.AtmBoxRepository;
import uz.pdp.appatmsystem.repository.AtmRepository;
import uz.pdp.appatmsystem.repository.CardRepository;
import uz.pdp.appatmsystem.repository.UserRepository;
import uz.pdp.appatmsystem.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class AtmService {
    @Autowired
    AtmRepository atmRepository;
    @Autowired
    AtmBoxRepository boxRepository;
    @Autowired
    Calculation calculation;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    MyAuthService myAuthService;
    @Autowired
    PasswordEncoder passwordEncoder;

    Map<String, Integer> hm = new HashMap<>();

    public ApiResponse addATM(AtmDto dto, HttpServletRequest httpServletRequest) {
        boolean b = atmRepository.existsBySerialNumber(dto.getSerialNumber());
        if (b) return new ApiResponse("ushbu bankomat avval ro`yxatdan o`tkazilgan", false);

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String email = jwtProvider.getEmailFromToken(token);
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (!optionalUser.isPresent()) return new ApiResponse("tizimdagi userga tegishli email topilmadi", false);
        if (!optionalUser.get().getRoleName().name().equals(RoleName.DIRECTOR.name()))
            return new ApiResponse("faqat direktor bankomatni qo`sha oladi", false);

        CardType contains = calculation.contains(dto.getCardType());

        if (contains == null) return new ApiResponse("karta turi noto`g`ri kiritildi", false);

        ATM atm = new ATM();
        atm.setCardType(contains);
        atm.setSerialNumber(dto.getSerialNumber());
        atm.setBankName(dto.getBankName());
        atm.setMaxAmount(dto.getMaxAmount());
        atm.setMinAmount(dto.getMinAmount());
        atm.setAddress(dto.getAddress());
        atmRepository.save(atm);
        return new ApiResponse("bankomat ro`yxatdan o`tkazildi. Endi bankomat hisobini to`ldiring", true);


    }

    public ApiResponse verifyCard(CardDto cardDto) {
        Optional<Card> optionalCard1 = cardRepository.findByNumber(cardDto.getNumber());
        if (!optionalCard1.isPresent() || !optionalCard1.get().isStatus())
            return new ApiResponse("karta yaroqsiz, yoki bloklangan", false);
        Card card = optionalCard1.get();
        boolean b = false;
        if (!card.getPassword().equals(cardDto.getPassword())) {
            for (Map.Entry<String, Integer> entry : hm.entrySet()) {
                if (entry.getKey().equals(cardDto.getNumber())) {
                    b = true;
                    if (entry.getValue() == 3) {
                        card.setStatus(false);
                        cardRepository.save(card);
                        return new ApiResponse("kartangiz bloklandi", false);
                    } else entry.setValue(entry.getValue() + 1);
                }
            }
            if (!b) {
                hm.put(cardDto.getNumber(), 1);
                return new ApiResponse("parol xato. Qaytadan urinib ko`ring", false);
            } else return new ApiResponse("parol xato. Qaytadan urinib ko`ring", false);
        }
        Optional<User> byEmail = userRepository.findByEmail(cardDto.getNumber() + "@gmail.com");
        if (byEmail.isPresent()) {
            userRepository.delete(byEmail.get());
            return new ApiResponse("xatolik. Qaytadan urinib ko`ring", false);
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


    }

    public ApiResponse balance(UUID id, HttpServletRequest httpServletRequest) {
        Optional<ATM> optionalATM = atmRepository.findById(id);
        if (!optionalATM.isPresent()) return new ApiResponse("bunday bankomat topilamdi", false);
        User user = (User) SecurityContextHolder.getContext().getAuthentication();
        if (!user.getRoleName().name().equals(RoleName.WORKER.name()) ||
                !user.getRoleName().name().equals(RoleName.DIRECTOR.name()))
            return new ApiResponse("sizda bunday huquq mavjud " +
                    "emas", false);
        ATM atm = optionalATM.get();
        Integer balance = calculation.balance(atm.getAtmBox());
        return new ApiResponse("bankomatdagi pul miqdori: ", true, balance);
    }

    public ApiResponse deleteToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String email = jwtProvider.getEmailFromToken(token);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();
        if (!user.getRoleName().name().equals(RoleName.USER.name()))
            return new ApiResponse("siz bu so`rovni amalga oshira olmaysiz", false);

        userRepository.delete(user);
        return new ApiResponse("Tizimdan chiqdingiz. Endi tokeningiz yaroqsiz", true);
    }
}
