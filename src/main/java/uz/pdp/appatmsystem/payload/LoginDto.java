package uz.pdp.appatmsystem.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginDto {

    private String email;
//    @NotNull
    private String password;
}
