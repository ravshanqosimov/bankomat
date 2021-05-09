package uz.pdp.appatmsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.appatmsystem.enums.CardType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Card    {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false, unique = true)
    @Size(min = 16,max = 16)
    private String number;

        @Column(nullable = false)
        @Size(min = 4,max = 4)
    private String password;

    private String bankName;

    private String CVVcode;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)//amal qilish muddati
    private Timestamp validityPeriod;

    private double balance = 0;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    private boolean status = true;

}
