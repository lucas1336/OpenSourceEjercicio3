package com.upc.OpenSourceEjercicio3.service.impl;

import com.upc.OpenSourceEjercicio3.model.Account;
import com.upc.OpenSourceEjercicio3.repository.AccountRepository;
import com.upc.OpenSourceEjercicio3.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }
}
