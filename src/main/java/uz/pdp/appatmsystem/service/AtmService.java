package uz.pdp.appatmsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.appatmsystem.component.Calculation;
import uz.pdp.appatmsystem.entity.ATM;
import uz.pdp.appatmsystem.entity.AtmBox;
import uz.pdp.appatmsystem.entity.User;
import uz.pdp.appatmsystem.enums.CardType;
import uz.pdp.appatmsystem.enums.RoleName;
import uz.pdp.appatmsystem.payload.ApiResponse;
import uz.pdp.appatmsystem.payload.AtmDto;
import uz.pdp.appatmsystem.repository.AtmBoxRepository;
import uz.pdp.appatmsystem.repository.AtmRepository;
import uz.pdp.appatmsystem.repository.UserRepository;
import uz.pdp.appatmsystem.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

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

    public ApiResponse update(UUID id, AtmBox dto) {
        Optional<ATM> optionalATM = atmRepository.findById(id);
        if (!optionalATM.isPresent()) return new ApiResponse("bunday bankomat topilamdi", false);
        User user = (User) SecurityContextHolder.getContext().getAuthentication();
        if (!user.getRoleName().name().equals(RoleName.WORKER.name()))
            return new ApiResponse("sizda bunday huquq mavjud " +
                    "emas", false);
        ATM atm = optionalATM.get();
        AtmBox atmBox = new AtmBox();
        atmBox.setThousand_UZS(dto.getThousand_UZS());
        atmBox.setFiveThousand_UZS(dto.getFiveThousand_UZS());
        atmBox.setTenThousand_UZS(dto.getTenThousand_UZS());
        atmBox.setFiftyThousand_UZS(dto.getFiftyThousand_UZS());
        atmBox.setOneHundredThousand_UZS(dto.getOneHundredThousand_UZS());
        atmBox.setOne$(dto.getOne$());
        atmBox.setFive$(dto.getFive$());
        atmBox.setTen$(dto.getTen$());
        atmBox.setTwenty$(dto.getTwenty$());
        atmBox.setOneHundred$(dto.getOneHundred$());
        atmBox.setDate(new Date());
        boxRepository.save(atmBox);
        atm.setBalance(atmBox);
        atm.setStatus(true);
        atmRepository.save(atm);
        return new ApiResponse("bankomat hisobi to`ldirildi", true);

    }
}
