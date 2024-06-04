package com.sanlamfintech.bankservice.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalEvent {

    private BigDecimal amount;

    private Long accountId;

    private String status;
}
