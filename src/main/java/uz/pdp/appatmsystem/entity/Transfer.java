package uz.pdp.appatmsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.appatmsystem.enums.RoleName;
import uz.pdp.appatmsystem.enums.TransferType;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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

    private String cardNumber;

    private String atmSerialNumber;

    @ManyToOne
    private AtmBox atmBox;

    private double commissionAmount;

    private SimpleDateFormat date;

    //ushbu field bankomatga nisbatan olinadi yani karta orqali bankomatga pul qo`shildi yoki yechiladi
    private TransferType transferType;
    private RoleName roleName;

}
