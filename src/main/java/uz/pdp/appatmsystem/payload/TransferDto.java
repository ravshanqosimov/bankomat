package uz.pdp.appatmsystem.payload;

import lombok.Data;

@Data
public class TransferDto {

    private String atmId;
    private String cardId;
    private String cardCode;
    private Integer amount;
    private String transferType;
}
