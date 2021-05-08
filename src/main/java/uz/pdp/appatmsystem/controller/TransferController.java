package uz.pdp.appatmsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appatmsystem.payload.ApiResponse;
import uz.pdp.appatmsystem.payload.TransferDto;
import uz.pdp.appatmsystem.payload.VerifyCardDto;
import uz.pdp.appatmsystem.service.TransferService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class TransferController {
    @Autowired
    TransferService service;

    //pin kodni tekshirish
    @PostMapping("/card/verify")
    public HttpEntity<?>verifyCard(@RequestBody VerifyCardDto cardDto){

        ApiResponse apiResponse = service.verifyCard(cardDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }



    //bankamatdan pul yechish
    @PostMapping("/output")
    public HttpEntity<?> getMoney(@RequestBody TransferDto transferDto, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = service.getMoney(transferDto,httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse.getMessage());
    }

    //bankamatga pul solish
//    @PostMapping("/input")
//    public HttpEntity<?> addUser(@RequestBody TransferDto transferDto,HttpServletRequest httpServletRequest) {
//        ApiResponse apiResponse = service.getMoney(transferDto, httpServletRequest);
//        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse.getMessage());
//    }


}
