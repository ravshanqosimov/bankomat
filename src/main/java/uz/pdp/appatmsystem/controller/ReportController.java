package uz.pdp.appatmsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.appatmsystem.payload.ApiResponse;
import uz.pdp.appatmsystem.service.AtmService;
import uz.pdp.appatmsystem.service.TransferService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;


@RestController
@RequestMapping("/api/report")
public class ReportController {
    @Autowired
    AtmService atmService;
    @Autowired
    TransferService transferService;


    //bankomatdagi mablag`
    @GetMapping("/{id}")
    public HttpEntity<?> getBalance(@RequestBody UUID id, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = atmService.balance(id, httpServletRequest);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getObject());
        return ResponseEntity.status(409).body(apiResponse.getMessage());    }


    @GetMapping
    public HttpEntity<?> getAll(  HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = transferService.getAll(  httpServletRequest);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getObject());
        return ResponseEntity.status(409).body(apiResponse.getMessage());    }

    @GetMapping("/income")
    public HttpEntity<?> getIncome(@RequestBody String atmSerialNumber, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = transferService.getIncome( atmSerialNumber, httpServletRequest);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getObject());
        return ResponseEntity.status(409).body(apiResponse.getMessage());    }

    @GetMapping("/outcome")
    public HttpEntity<?> getOutcome( @RequestBody String atmSerialNumber, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = transferService.getOutcome( atmSerialNumber, httpServletRequest);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getObject());
        return ResponseEntity.status(409).body(apiResponse.getMessage());    }

 @GetMapping("/byWorker")
    public HttpEntity<?> byWorker( @RequestBody String atmSerialNumber, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = transferService.getByWorker( atmSerialNumber, httpServletRequest);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse.getObject());
        return ResponseEntity.status(409).body(apiResponse.getMessage());    }



}
