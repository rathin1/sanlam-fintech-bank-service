package com.sanlamfintech.bankservice.repository;

import com.sanlamfintech.bankservice.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
