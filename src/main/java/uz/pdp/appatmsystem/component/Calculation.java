package uz.pdp.appatmsystem.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.pdp.appatmsystem.entity.AtmBox;
import uz.pdp.appatmsystem.enums.CardType;
import uz.pdp.appatmsystem.repository.AtmRepository;

@Component
public class Calculation {
    @Autowired
    AtmRepository atmRepository;

    public CardType contains(String test) {
        for (CardType c : CardType.values()) {
            if (c.name().equalsIgnoreCase(test)) {
                return c;
            }
        }
        return null;
    }

    public Integer balance(AtmBox box) {
        return 1_000 * box.getThousand_UZS() +
                5_000 * box.getFiveThousand_UZS() +
                10_000 * box.getTenThousand_UZS() +
                50_000 * box.getFiftyThousand_UZS() +
                100_000 * box.getOneHundredThousand_UZS() +

                box.getOne$() +
                5 * box.getFive$() +
                10 * box.getTen$() +
                20 * box.getTwenty$() +
                100 * box.getOneHundred$();

    }
}
