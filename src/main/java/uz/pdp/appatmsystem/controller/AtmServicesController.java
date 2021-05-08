package uz.pdp.appatmsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appatmsystem.payload.ApiResponse;
import uz.pdp.appatmsystem.payload.CardDto;
import uz.pdp.appatmsystem.payload.TransferDto;
import uz.pdp.appatmsystem.service.AtmServicesService;

@RestController
@RequestMapping("/api/service")
public class AtmServicesController {
    @Autowired
    AtmServicesService service;





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
//
////karta balansini tekshirish
//    @GetMapping("/balance/{id}")
//    public HttpEntity<?> getBalance (@PathVariable String id ) {
//        ApiResponse apiResponse = service.getBalance();
//        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getObject());
//        return ResponseEntity.status(409).body(apiResponse.getMessage());
//    }


}
