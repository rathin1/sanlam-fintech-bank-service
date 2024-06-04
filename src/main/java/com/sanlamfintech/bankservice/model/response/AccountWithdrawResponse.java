package com.sanlamfintech.bankservice.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountWithdrawResponse extends Response {

    @JsonProperty("balance")
    private BigDecimal balance;

    public AccountWithdrawResponse(int code, String description) {
        super(code, description);
    }

    public AccountWithdrawResponse(int code, String description, BigDecimal balance) {
        super(code, description);
        this.balance = balance;
    }
}
