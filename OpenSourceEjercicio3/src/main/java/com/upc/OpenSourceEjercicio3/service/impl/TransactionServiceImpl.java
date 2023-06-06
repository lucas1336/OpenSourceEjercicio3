package com.upc.OpenSourceEjercicio3.service.impl;

import com.upc.OpenSourceEjercicio3.model.Transaction;
import com.upc.OpenSourceEjercicio3.repository.TransactionRepository;
import com.upc.OpenSourceEjercicio3.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl  implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
