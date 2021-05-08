package uz.pdp.appatmsystem.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import uz.pdp.appatmsystem.enums.CardType;

import javax.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ATM {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    //har bir bankomatning seria raqami bo`ladi
    @Column(nullable = false, unique = true)
    private String serialNumber;

    private String bankName;

    private Integer maxAmount;

    private Integer minAmount;

    private String address;

    @OneToOne
    private  AtmBox balance;

    private boolean status;

}
