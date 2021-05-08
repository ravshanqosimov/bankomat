package uz.pdp.appatmsystem.payload;

import lombok.Data;

@Data
public class TransferDto {

    private Integer atmId;
    private Integer cardId;
    private String cardCode;
    private Integer amount;
    private String transferType;
}
