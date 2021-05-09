package uz.pdp.appatmsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.pdp.appatmsystem.component.Calculation;
import uz.pdp.appatmsystem.component.EmailSender;
import uz.pdp.appatmsystem.entity.*;
import uz.pdp.appatmsystem.enums.CardType;
import uz.pdp.appatmsystem.enums.RoleName;
import uz.pdp.appatmsystem.enums.TransferType;
import uz.pdp.appatmsystem.payload.*;
import uz.pdp.appatmsystem.repository.*;
import uz.pdp.appatmsystem.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TransferService {

    @Autowired
    CardRepository cardRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Calculation calculation;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AtmRepository atmRepository;
    @Autowired
    AtmBoxRepository boxRepository;
    @Autowired
    TransferRepository transferRepository;
    @Autowired
    EmailSender emailSender;

    public ApiResponse getMoney(TransferDto dto, HttpServletRequest httpServletRequest) {
        Double commission = 0.01; //1% komissiya

        Optional<ATM> optionalATM = atmRepository.findBySerialNumber(dto.getAtmSerialNumber());
        if (!optionalATM.isPresent() || !optionalATM.get().isStatus()) return new ApiResponse("bankomat ishlamaydi",
                false);

        ATM atm = optionalATM.get();

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String email = jwtProvider.getEmailFromToken(token);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            return new ApiResponse("tizimdagi userga tegishli email topilmadi", false);


        User user = optionalUser.get();
        if (!user.getRoleName().name().equals(RoleName.USER.name()))
            return new ApiResponse("siz pul yecha olmaysiz", false);


        String cardNumber = user.getEmail().substring(0, 16);
        Optional<Card> byNumber = cardRepository.findByNumber(cardNumber);
        Card card = byNumber.get();//user yani foydalanuvchi kartasi
        if (!card.getCardType().equals(atm.getCardType()))
            return new ApiResponse("Sizning kartaning turi bu bankomatga" +
                    " mos emas", false);


        AtmBox atmBox = atm.getAtmBox();
        Integer balance = calculation.balance(atmBox);

        if (balance < (dto.getAmount() * 0.01) + dto.getAmount() ||
                dto.getAmount() > atm.getMaxAmount() ||
                dto.getAmount() < atm.getMinAmount())
            return new ApiResponse("mumkin bo`lmagan pul miqdori",
                    false);
        try {


            if (card.getCardType().equals(CardType.VISA)) {
                AtmBox atmBox1 = calculation.balanceToAtm(dto.getAmount(), "usd");
                if (atmBox1 == null) return new ApiResponse("noto`gri balans", false);
                if (atmBox.getUsd_100() >= atmBox1.getUsd_100() &&
                        atmBox.getUsd_50() >= atmBox1.getUsd_50() &&
                        atmBox.getUsd_10() >= atmBox1.getUsd_10() &&
                        atmBox.getUsd_5() >= atmBox1.getUsd_5() &&
                        atmBox.getUsd_1() >= atmBox1.getUsd_1()) {
                    atmBox.setUsd_100(atmBox.getUsd_100() - atmBox1.getUsd_100());
                    atmBox.setUsd_50(atmBox.getUsd_50() - atmBox1.getUsd_50());
                    atmBox.setUsd_10(atmBox.getUsd_10() - atmBox1.getUsd_10());
                    atmBox.setUsd_5(atmBox.getUsd_5() - atmBox1.getUsd_5());
                    atmBox.setUsd_1(atmBox.getUsd_1() - atmBox1.getUsd_1());
                    boxRepository.save(atmBox);
                    atm.setAtmBox(atmBox);
                    atmRepository.save(atm);
                    card.setBalance(card.getBalance() - (balance + balance * commission));
                    cardRepository.save(card);
                    Transfer transfer = new Transfer();
                    transfer.setCardNumber(cardNumber);
                    transfer.setAtmSerialNumber(atm.getSerialNumber());
                    transfer.setAtmBox(atmBox1);
                    transfer.setDate(new SimpleDateFormat());
                    transfer.setTransferType(TransferType.OUTCOME);
                    transfer.setRoleName(user.getRoleName());
                    transferRepository.save(transfer);
                    return new ApiResponse("muvaffaqiyatli", true);
                } else return new ApiResponse("kiritilgan balansga mos kupyura topilmadi", false);
            } else {
                AtmBox atmBox1 = calculation.balanceToAtm(dto.getAmount(), "uzs");
                if (atmBox1 == null) return new ApiResponse("noto`gri balans", false);
                if (atmBox.getUzs_100_000() >= atmBox1.getUzs_100_000() &&
                        atmBox.getUzs_50_000() >= atmBox1.getUzs_50_000() &&
                        atmBox.getUzs_10_000() >= atmBox1.getUzs_10_000() &&
                        atmBox.getUzs_5_000() >= atmBox1.getUzs_5_000() &&
                        atmBox.getUzs_1_000() >= atmBox1.getUzs_1_000()) {
                    atmBox.setUzs_100_000(atmBox.getUzs_100_000() - atmBox1.getUzs_100_000());
                    atmBox.setUzs_50_000(atmBox.getUzs_50_000() - atmBox1.getUzs_50_000());
                    atmBox.setUzs_10_000(atmBox.getUzs_10_000() - atmBox1.getUzs_10_000());
                    atmBox.setUzs_5_000(atmBox.getUzs_5_000() - atmBox1.getUzs_5_000());
                    atmBox.setUzs_1_000(atmBox.getUzs_1_000() - atmBox1.getUzs_1_000());
                    boxRepository.save(atmBox);
                    atm.setAtmBox(atmBox);
                    atmRepository.save(atm);
                    card.setBalance(card.getBalance() - (balance + balance * commission));
                    cardRepository.save(card);
                    Transfer transfer = new Transfer();
                    transfer.setCardNumber(cardNumber);
                    transfer.setAtmSerialNumber(atm.getSerialNumber());
                    transfer.setAtmBox(atmBox1);
                    transfer.setDate(new SimpleDateFormat());
                    transfer.setTransferType(TransferType.OUTCOME);
                    transfer.setRoleName(user.getRoleName());
                    transferRepository.save(transfer);

                    Integer newBalance = calculation.balance(atmBox);
                    if (newBalance <= 10_000_000) {
                        Optional<User> byRoleName = userRepository.findByRoleName(RoleName.WORKER);
                        User xodim = byRoleName.get();
                        boolean b1 = emailSender.sendEmail(xodim.getEmail(), "bankomatda 10_000_000 dan kam mablag` " +
                                "qoldi");
                        if (b1) return new ApiResponse("yangi xodim emailiga xabar yuborildi", true);

                        return new ApiResponse("xatolik yuz berdi", false);
                    }


                    return new ApiResponse("muvaffaqiyatli", true);
                } else return new ApiResponse("kiritilgan balansga mos kupyura topilmadi", false);
            }
        } catch (Exception e) {
            return new ApiResponse("xatolik", false);
        }

    }

    public ApiResponse update(String atmSerialNumber, AtmBox dto, HttpServletRequest httpServletRequest) {

        Optional<ATM> optionalATM = atmRepository.findBySerialNumber(atmSerialNumber);
        if (!optionalATM.isPresent()) return new ApiResponse("bunday bankomat topilamdi", false);
        ATM atm = optionalATM.get();

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String email = jwtProvider.getEmailFromToken(token);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) return new ApiResponse("tizimdagi userga tegishli email topilmadi", false);
        User user = optionalUser.get();

        //ishci uchun bankomatga pul joylash
        if (user.getRoleName().name().equals(RoleName.WORKER.name())) {
            AtmBox atmBox = new AtmBox();
            if (atm.getCardType().name().equalsIgnoreCase("visa")) {

                atmBox.setUsd_1(dto.getUsd_1());
                atmBox.setUsd_5(dto.getUsd_5());
                atmBox.setUsd_10(dto.getUsd_10());
                atmBox.setUsd_50(dto.getUsd_50());
                atmBox.setUsd_100(dto.getUsd_100());
                boxRepository.save(atmBox);
                atm.setAtmBox(atmBox);
                atm.setStatus(true);
                atmRepository.save(atm);
                Transfer transfer = new Transfer();
                transfer.setAtmSerialNumber(atmSerialNumber);
                transfer.setAtmBox(dto);
                transfer.setDate(new SimpleDateFormat());
                transfer.setTransferType(TransferType.INCOME);
                transfer.setRoleName(user.getRoleName());
                transferRepository.save(transfer);
                return new ApiResponse("bankomat hisobi to`ldirildi", true);

            } else {
                atmBox.setUzs_1_000(dto.getUzs_1_000());
                atmBox.setUzs_5_000(dto.getUzs_5_000());
                atmBox.setUzs_10_000(dto.getUzs_10_000());
                atmBox.setUzs_50_000(dto.getUzs_50_000());
                atmBox.setUzs_100_000(dto.getUzs_100_000());
                boxRepository.save(atmBox);
                atm.setAtmBox(atmBox);
                atm.setStatus(true);
                atmRepository.save(atm);
                Transfer transfer = new Transfer();
                transfer.setAtmSerialNumber(atmSerialNumber);
                transfer.setAtmBox(dto);
                transfer.setDate(new SimpleDateFormat());
                transfer.setTransferType(TransferType.INCOME);
                transfer.setRoleName(user.getRoleName());
                transferRepository.save(transfer);
                return new ApiResponse("bankomat hisobi to`ldirildi", true);
            }
        }

        //userlar yani foydalanuvchilar uchun bankomatga pul kiritish
        else if (user.getRoleName().name().equals(RoleName.USER.name())) {
            if (!atm.isStatus()) return new ApiResponse("bankomat ishlamaydi", false);
            String cardNumber = user.getEmail().substring(0, 16);
            Optional<Card> byNumber = cardRepository.findByNumber(cardNumber);
            Card card = byNumber.get();//user yani foydalanuvchi kartasi
            if (!card.getCardType().equals(atm.getCardType()))
                return new ApiResponse("Sizning kartaning turi bu bankomatga" +
                        " mos emas", false);
            if (card.getCardType().equals(CardType.VISA)) {


                AtmBox atmBox = atm.getAtmBox();
                atmBox.setUsd_1(atmBox.getUsd_1() + dto.getUsd_1());
                atmBox.setUsd_5(atmBox.getUsd_5() + dto.getUsd_5());
                atmBox.setUsd_10(atmBox.getUsd_10() + dto.getUsd_10());
                atmBox.setUsd_50(atmBox.getUsd_50() + dto.getUsd_50());
                atmBox.setUsd_100(atmBox.getUsd_100() + dto.getUsd_100());
                boxRepository.save(atmBox);
                atm.setAtmBox(atmBox);
                atmRepository.save(atm);
                Integer balance = calculation.balance(atmBox);
                card.setBalance(card.getBalance() + balance);
                cardRepository.save(card);
                Transfer transfer = new Transfer();
                transfer.setCardNumber(cardNumber);
                transfer.setAtmSerialNumber(atmSerialNumber);
                transfer.setAtmBox(dto);
                transfer.setDate(new SimpleDateFormat());
                transfer.setTransferType(TransferType.INCOME);
                transfer.setRoleName(user.getRoleName());
                transferRepository.save(transfer);
                return new ApiResponse("pul o`tkazildi", true);
            } else {
                AtmBox atmBox = atm.getAtmBox();
                atmBox.setUzs_1_000(atmBox.getUzs_1_000() + dto.getUzs_1_000());
                atmBox.setUzs_5_000(atmBox.getUzs_5_000() + dto.getUzs_5_000());
                atmBox.setUzs_10_000(atmBox.getUzs_10_000() + dto.getUzs_10_000());
                atmBox.setUzs_50_000(atmBox.getUzs_50_000() + dto.getUzs_50_000());
                atmBox.setUzs_100_000(atmBox.getUzs_100_000() + dto.getUzs_100_000());
                boxRepository.save(atmBox);
                atm.setAtmBox(atmBox);
                atmRepository.save(atm);
                Integer balance = calculation.balance(atmBox);
                card.setBalance(card.getBalance() + balance);
                cardRepository.save(card);
                Transfer transfer = new Transfer();
                transfer.setCardNumber(cardNumber);
                transfer.setAtmSerialNumber(atmSerialNumber);
                transfer.setAtmBox(dto);
                transfer.setDate(new SimpleDateFormat());
                transfer.setTransferType(TransferType.INCOME);
                transfer.setRoleName(user.getRoleName());
                transferRepository.save(transfer);
                return new ApiResponse("pul o`tkazildi", true);
            }

        }
        return new ApiResponse("sizning rolingiz mos kelmaydi", false);
    }

    public ApiResponse getAll(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String email = jwtProvider.getEmailFromToken(token);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            return new ApiResponse("tizimdagi userga tegishli email topilmadi", false);
        if (!optionalUser.get().getRoleName().name().equals(RoleName.DIRECTOR.name()))
            return new ApiResponse("faqat direktorlarga ruxsat bor", false);

        List<Transfer> all = transferRepository.findAll();
        return new ApiResponse("List: ", true, all);

    }

    public ApiResponse getIncome(String serilNumber, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String email = jwtProvider.getEmailFromToken(token);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            return new ApiResponse("tizimdagi userga tegishli email topilmadi", false);
        if (!optionalUser.get().getRoleName().name().equals(RoleName.DIRECTOR.name()))
            return new ApiResponse("faqat direktorlarga ruxsat bor", false);
        Optional<ATM> optionalATM = atmRepository.findBySerialNumber(serilNumber);
        if (!optionalATM.isPresent()) return new ApiResponse("bankomat topilmadi", false);

        List<Transfer> all = transferRepository.findAllByAtmSerialNumberAndTransferTypeAndDate(serilNumber,
                TransferType.INCOME, new SimpleDateFormat());

        return new ApiResponse("List: ", true, all);

    }

    public ApiResponse getOutcome(String serilNumber, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String email = jwtProvider.getEmailFromToken(token);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            return new ApiResponse("tizimdagi userga tegishli email topilmadi", false);
        if (!optionalUser.get().getRoleName().name().equals(RoleName.DIRECTOR.name()))
            return new ApiResponse("faqat direktorlarga ruxsat bor", false);

        Optional<ATM> optionalATM = atmRepository.findBySerialNumber(serilNumber);
        if (!optionalATM.isPresent()) return new ApiResponse("bankomat topilmadi", false);

        List<Transfer> all = transferRepository.findAllByAtmSerialNumberAndTransferTypeAndDate(serilNumber,
                TransferType.OUTCOME, new SimpleDateFormat());

        return new ApiResponse("List: ", true, all);

    }

    public ApiResponse getByWorker(String serilNumber, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String email = jwtProvider.getEmailFromToken(token);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            return new ApiResponse("tizimdagi userga tegishli email topilmadi", false);
        if (!optionalUser.get().getRoleName().name().equals(RoleName.DIRECTOR.name()))
            return new ApiResponse("faqat direktorlarga ruxsat bor", false);

        Optional<ATM> optionalATM = atmRepository.findBySerialNumber(serilNumber);
        if (!optionalATM.isPresent()) return new ApiResponse("bankomat topilmadi", false);

        List<Transfer> all = transferRepository.findAllByAtmSerialNumberAndRoleName(serilNumber,
                RoleName.WORKER);

        return new ApiResponse("List: ", true, all);

    }
}
