package uz.pdp.appatmsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appatmsystem.payload.ApiResponse;
import uz.pdp.appatmsystem.payload.LoginDto;
import uz.pdp.appatmsystem.service.MyAuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    MyAuthService myAuthService;

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDto loginDto) {
        ApiResponse apiResponse = myAuthService.login(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }
}
