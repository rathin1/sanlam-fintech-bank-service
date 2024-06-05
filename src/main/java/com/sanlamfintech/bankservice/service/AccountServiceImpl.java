package com.sanlamfintech.bankservice.service;

import com.sanlamfintech.bankservice.exception.AccountNotFoundException;
import com.sanlamfintech.bankservice.exception.InsufficientFundsException;
import com.sanlamfintech.bankservice.exception.InvalidAmountException;
import com.sanlamfintech.bankservice.model.WithdrawalStatus;
import com.sanlamfintech.bankservice.model.entity.Account;
import com.sanlamfintech.bankservice.model.event.WithdrawalEvent;
import com.sanlamfintech.bankservice.model.request.AccountWithdrawRequest;
import com.sanlamfintech.bankservice.model.response.AccountWithdrawResponse;
import com.sanlamfintech.bankservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AmazonSnsService amazonSnsService;

    @Override
    public AccountWithdrawResponse withdraw(AccountWithdrawRequest accountWithdrawRequest) {
        log.info("Withdrawal request received for account: {}", accountWithdrawRequest.getAccountId());

        Account account = getAccount(accountWithdrawRequest.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountWithdrawRequest.getAccountId()));

        checkSufficientFunds(account, accountWithdrawRequest.getAmount());

        account.setBalance(account.getBalance().subtract(accountWithdrawRequest.getAmount()));
        Account updatedAccount = accountRepository.save(account);

        publishWithdrawalEvent(accountWithdrawRequest.getAmount(), accountWithdrawRequest.getAccountId(), WithdrawalStatus.SUCCESSFUL.description);

        return new AccountWithdrawResponse(0, WithdrawalStatus.SUCCESSFUL.description, updatedAccount.getBalance());
    }

    private Optional<Account> getAccount(Long accountId) {
        return accountRepository.findById(accountId);
    }

    private void publishWithdrawalEvent(BigDecimal amount, Long accountId, String status) {
        final WithdrawalEvent withdrawalEvent = new WithdrawalEvent(amount, accountId, status);
        amazonSnsService.publishMessage(withdrawalEvent);
    }

    private void checkSufficientFunds(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal: " + amount);
        }
    }
}
