package com.sanlamfintech.bankservice.service;

import com.sanlamfintech.bankservice.model.request.AccountWithdrawRequest;
import com.sanlamfintech.bankservice.model.response.AccountWithdrawResponse;

public interface AccountService {

    AccountWithdrawResponse withdraw(AccountWithdrawRequest accountWithdrawRequest);
}
