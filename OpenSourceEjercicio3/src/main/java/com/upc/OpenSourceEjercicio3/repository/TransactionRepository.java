package com.upc.OpenSourceEjercicio3.repository;

import com.upc.OpenSourceEjercicio3.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount_Id(long id);
    boolean existsByAmountAndBalance(double amount, double balance);
    boolean existsByAccount_Id(long id);
}
