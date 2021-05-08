package uz.pdp.appatmsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.appatmsystem.enums.TransferType;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//bankomatdan pul solish
//kartadan
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Card Card;

    @ManyToOne
    private ATM bankomat;

    private double amount;

    private double commissionAmount;

    private Date date;
//ushbu field bankomatga nisbatan olinadi yani karta orqali bankomatga pul qo`shildi yoki olindi
    private TransferType transferType;


}
