package uz.pdp.appatmsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import uz.pdp.appatmsystem.enums.CardType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Card {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String number;

    private String bankName;

    private String CVVcode;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)//amal qilish muddati
    private Timestamp validityPeriod;

    @Column(nullable = false)
    private String code;

    private double balance = 0;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    private boolean status = true;

}
