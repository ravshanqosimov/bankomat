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

    private Integer uzs_1_000 = 0;
    private Integer uzs_5_000  = 0;
    private Integer uzs_10_000  = 0;
    private Integer uzs_50_000  = 0;
    private Integer uzs_100_000  = 0;

    private Integer usd_1 = 0;
    private Integer usd_5 = 0;
    private Integer usd_10 = 0;
    private Integer usd_50 = 0;
    private Integer usd_100 = 0;

//    private Date date;


    }

