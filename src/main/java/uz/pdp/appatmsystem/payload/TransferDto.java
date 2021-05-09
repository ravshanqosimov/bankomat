package uz.pdp.appatmsystem.payload;

import lombok.Data;

@Data
public class TransferDto {
    private String atmSerialNumber;
    private Integer amount;
}
