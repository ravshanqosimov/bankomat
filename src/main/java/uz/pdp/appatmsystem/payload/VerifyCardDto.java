package uz.pdp.appatmsystem.payload;

import lombok.Data;

@Data
public class VerifyCardDto {
    private String number;
    private String password;

}
