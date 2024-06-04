package com.sanlamfintech.bankservice.service;

import com.sanlamfintech.bankservice.model.*;
import com.sanlamfintech.bankservice.model.entity.Account;
import com.sanlamfintech.bankservice.model.event.WithdrawalEvent;
import com.sanlamfintech.bankservice.model.request.AccountWithdrawRequest;
import com.sanlamfintech.bankservice.model.response.AccountWithdrawResponse;
import com.sanlamfintech.bankservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
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

        if (accountWithdrawRequest.getAmount() == null || accountWithdrawRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Invalid accountWithdrawRequest.getAmount(): {}", accountWithdrawRequest.getAmount());
            return new AccountWithdrawResponse(1, WithdrawalStatus.INVALID_AMOUNT.description);
        }

        Optional<Account> account = getAccount(accountWithdrawRequest.getAccountId());

        if (account.isPresent()) {
            log.info("Account found: {}", accountWithdrawRequest.getAccountId());
            Account accountEntity = account.get();
            BigDecimal balance = accountEntity.getBalance();
            if (balance.compareTo(accountWithdrawRequest.getAmount()) >= 0) {
                accountEntity.setBalance(balance.subtract(accountWithdrawRequest.getAmount()));
                Account updatedAccount = accountRepository.save(accountEntity);
                log.info("Withdrawal successful for account: {}", updatedAccount.getAccountId());

                publishWithdrawalEvent(accountWithdrawRequest.getAmount(), accountWithdrawRequest.getAccountId(), WithdrawalStatus.SUCCESSFUL.description);
                return new AccountWithdrawResponse(0, WithdrawalStatus.SUCCESSFUL.description, updatedAccount.getBalance());
            } else {
                log.error("Insufficient funds for withdrawal: {}", accountWithdrawRequest.getAmount());
                return new AccountWithdrawResponse(1, WithdrawalStatus.INSUFFICIENT_FUNDS.description);
            }
        }
        log.error("Account not found: {}", accountWithdrawRequest.getAccountId());
        return new AccountWithdrawResponse(1, WithdrawalStatus.ACCOUNT_NOT_FOUND.description);

    }

    private Optional<Account> getAccount(Long accountId) {
        return accountRepository.findById(accountId);
    }

    private void publishWithdrawalEvent(BigDecimal amount, Long accountId, String status) {
        final WithdrawalEvent withdrawalEvent = new WithdrawalEvent(amount, accountId, status);
        amazonSnsService.publishMessage(withdrawalEvent);
    }
}
