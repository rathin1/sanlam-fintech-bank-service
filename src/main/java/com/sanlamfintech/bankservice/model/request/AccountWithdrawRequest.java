package com.sanlamfintech.bankservice.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountWithdrawRequest {

    @NotNull(message = "Account id is required")
    @JsonProperty("account_id")
    private Long accountId;

    @NotNull(message = "Amount is required")
    @JsonProperty("amount")
    private BigDecimal amount;

}
