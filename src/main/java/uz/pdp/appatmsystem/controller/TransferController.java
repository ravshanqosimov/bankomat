package uz.pdp.appatmsystem.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

//    @PostMapping("/getMoney")
//    public HttpEntity<?> transfer(@RequestBody TransferMoneyDto moneyDto, HttpServletRequest httpServletRequest) {
//        ApiResponse apiResponse = transferMoneyService.transferMoney(moneyDto, httpServletRequest);
//        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.OK :
//                HttpStatus.CONFLICT).body(apiResponse.getMessage());
//    }

    @GetMapping("/me")
    public HttpEntity<?> transfer() {
    return     ResponseEntity.ok("lsamakl");
    }

}
