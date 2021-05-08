package uz.pdp.appatmsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AtmBox {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private Integer id;

    private Integer thousand_UZS = 0;
    private Integer fiveThousand_UZS = 0;
    private Integer tenThousand_UZS = 0;
    private Integer fiftyThousand_UZS = 0;
    private Integer oneHundredThousand_UZS = 0;

    private Integer one$ = 0;
    private Integer five$ = 0;
    private Integer ten$ = 0;
    private Integer twenty$ = 0;
    private Integer oneHundred$ = 0;

    private Date date;
}
