package com.sanlamfintech.bankservice.controller;

import com.sanlamfintech.bankservice.model.request.AccountWithdrawRequest;
import com.sanlamfintech.bankservice.model.response.AccountWithdrawResponse;
import com.sanlamfintech.bankservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BankAccountController {

    private final AccountService accountService;

    @PostMapping("/withdraw")
    public ResponseEntity<AccountWithdrawResponse> withdraw(@RequestBody @Validated AccountWithdrawRequest accountWithdrawRequest) {
        AccountWithdrawResponse accountWithdrawResponse = accountService.withdraw(accountWithdrawRequest);

        if (accountWithdrawResponse.getCode() == 0) {
            return ResponseEntity.ok(accountWithdrawResponse);
        } else {
            return ResponseEntity.badRequest().body(accountWithdrawResponse);
        }
    }
}
