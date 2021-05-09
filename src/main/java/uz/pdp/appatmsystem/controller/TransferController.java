package uz.pdp.appatmsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appatmsystem.entity.AtmBox;
import uz.pdp.appatmsystem.payload.ApiResponse;
import uz.pdp.appatmsystem.payload.CardDto;
import uz.pdp.appatmsystem.payload.TransferDto;
import uz.pdp.appatmsystem.service.TransferService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class TransferController {
    @Autowired
    TransferService service;

    //bankamatdan pul yechish
    @PutMapping("/outcome")
    public HttpEntity<?> outcome(@RequestBody TransferDto transferDto, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = service.getMoney(transferDto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse.getMessage());
    }

    //    bankamatga pul solish
    @PutMapping("/income")
    public HttpEntity<?> income(@RequestBody String atmSerialNumber, AtmBox atmBox,
                             HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = service.update(atmSerialNumber, atmBox, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse.getMessage());
    }


}
