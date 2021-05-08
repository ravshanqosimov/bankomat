package uz.pdp.appatmsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appatmsystem.entity.AtmBox;
import uz.pdp.appatmsystem.payload.ApiResponse;
import uz.pdp.appatmsystem.payload.AtmDto;
import uz.pdp.appatmsystem.service.AtmService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/atm")
public class AtmController {
    @Autowired
    AtmService atmService;


    //bankomatni ro`yxatdan o`tkazish
//    faqat direktor uchun
    @PostMapping
    public HttpEntity<?> addATM(@RequestBody AtmDto atmDto, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = atmService.addATM(atmDto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse.getMessage());
    }

    //bankomatdagi mablag`
    @GetMapping("/{id}")
    public HttpEntity<?> getBalance(@RequestBody UUID id, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = atmService.balance(id, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    //bankamat hisobini xodim tomonidan to`ldirish
    @PutMapping("/{id}")
    public HttpEntity<?> create(@PathVariable UUID id, @RequestBody AtmBox dto, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = atmService.update(id, dto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse.getMessage());
    }
}
