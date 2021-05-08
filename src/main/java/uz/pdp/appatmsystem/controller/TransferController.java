package uz.pdp.appatmsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.appatmsystem.payload.ApiResponse;
import uz.pdp.appatmsystem.payload.TransferDto;
import uz.pdp.appatmsystem.service.TransferService;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {
    @Autowired
    TransferService service;


    //bankamatdan pul yechish
    @PostMapping("/output")
    public HttpEntity<?> getMoney(@RequestBody TransferDto transferDto) {
        ApiResponse apiResponse = service.getMoney(transferDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse.getMessage());
    }

    //bankamatdan pul solish
    @PostMapping("/input")
    public HttpEntity<?> addUser(@RequestBody TransferDto transferDto) {
        ApiResponse apiResponse = service.getMoney(transferDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse.getMessage());
    }


}
