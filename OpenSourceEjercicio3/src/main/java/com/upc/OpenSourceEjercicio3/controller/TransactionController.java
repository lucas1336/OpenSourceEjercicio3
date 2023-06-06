package com.upc.OpenSourceEjercicio3.controller;

import com.upc.OpenSourceEjercicio3.exception.ResourceNotFoundException;
import com.upc.OpenSourceEjercicio3.exception.ValidationException;
import com.upc.OpenSourceEjercicio3.model.Account;
import com.upc.OpenSourceEjercicio3.model.Transaction;
import com.upc.OpenSourceEjercicio3.repository.AccountRepository;
import com.upc.OpenSourceEjercicio3.repository.TransactionRepository;
import com.upc.OpenSourceEjercicio3.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/bank/v1")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionController(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    // URL: http://localhost:8080/api/bank/v1/transactions/filterByNameCustomer
    // Method: GET
    @Transactional
    @RequestMapping("/transactions/filterByNameCustomer")
    public ResponseEntity<List<Transaction>> filterByNameCustomer(@RequestParam(name = "nameCustomer") String nameCustomer) {
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction:transactionRepository.findAll()) {
            if (transaction.getAccount().getNameCustomer().equals(nameCustomer)) {
                transactions.add(transaction);
            }
        }
        return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/bank/v1/transactions/filterByCreateDateRange
    // Method: GET
    @Transactional
    @RequestMapping("/transactions/filterByCreateDateRange")
    public ResponseEntity<List<Transaction>> filterByCreateDateRange(@RequestParam(name = "startDate") LocalDate startDate, @RequestParam(name = "endDate") LocalDate endDate) {
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction:transactionRepository.findAll()) {
            if (transaction.getCreateDate().isAfter(startDate) && transaction.getCreateDate().isBefore(endDate)) {
                transactions.add(transaction);
            }
        }
        return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
    }

    // URL: http://localhost:8080/api/bank/v1/accounts/{id}/transactions
    // Method: POST
    @Transactional
    @RequestMapping("/accounts/{id}/transactions")
    public ResponseEntity<Transaction> createTransaction(@PathVariable(value = "id") Long accountId, @RequestBody Transaction transaction) {
        Account account = accountRepository.findById(accountId).orElseThrow(()->
                new ResourceNotFoundException("Account not found with id: " + accountId));
        validateTransaction(transaction);

        transaction.setCreateDate(LocalDate.now());

        if (transaction.getType().equals("Retiro")){
            transaction.setBalance(transaction.getBalance()-transaction.getAmount());
        }
        else {
            transaction.setBalance(transaction.getBalance()+transaction.getAmount());
        }

        return new ResponseEntity<Transaction>(transactionService.createTransaction(transaction), HttpStatus.CREATED);
    }

    private void validateTransaction(Transaction transaction){
        if (transaction.getType() == null || transaction.getType().isEmpty()){
            throw new ValidationException("El tipo de transacción bancaria debe ser obligatorio");
        }
        if (!(transaction.getType().equals("Retiro")) && !(transaction.getType().equals("Deposito"))){
            throw new ValidationException("El tipo de transacción bancaria debe ser Retiro o Deposito");
        }
        if (transaction.getAmount()<=0){
            throw new ValidationException("El monto en una transacción bancaria debe ser mayor de S/.0.0");
        }
        if (transaction.getAmount()>transaction.getBalance()){
            throw new ValidationException("En una transacción bancaria tipo retiro el monto no puede ser mayor al saldo");
        }
    }
}
