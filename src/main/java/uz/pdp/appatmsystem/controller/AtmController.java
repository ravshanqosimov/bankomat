package uz.pdp.appatmsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appatmsystem.entity.AtmBox;
import uz.pdp.appatmsystem.payload.ApiResponse;
import uz.pdp.appatmsystem.payload.AtmDto;
import uz.pdp.appatmsystem.payload.CardDto;
import uz.pdp.appatmsystem.service.AtmService;
import uz.pdp.appatmsystem.service.TransferService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/atm")
public class AtmController {
    @Autowired
    AtmService atmService;
    @Autowired
    TransferService transferService;


    //bankomatni ro`yxatdan o`tkazish
//    faqat direktor uchun
    @PostMapping
    public HttpEntity<?> addATM(@RequestBody AtmDto atmDto, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = atmService.addATM(atmDto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse.getMessage());
    }
    //pin kodni tekshirish
    @PostMapping("/card/verify")
    public HttpEntity<?>verifyCard(@RequestBody CardDto cardDto){

        ApiResponse apiResponse = atmService.verifyCard(cardDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    //bankamat hisobini xodim tomonidan to`ldirish
    @PutMapping("/{atmSerialNumber}")
    public HttpEntity<?> create(@PathVariable String atmSerialNumber, @RequestBody AtmBox dto,
                                HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = transferService.update(atmSerialNumber, dto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse.getMessage());
    }

    @DeleteMapping("/card/exit")
    public HttpEntity<?>deleteToken(HttpServletRequest httpServletRequest){

        ApiResponse apiResponse = atmService.deleteToken(httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 204: 409).body(apiResponse);
    }
}
