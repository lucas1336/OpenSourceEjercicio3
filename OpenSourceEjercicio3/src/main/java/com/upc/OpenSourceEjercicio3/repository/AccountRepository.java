package com.upc.OpenSourceEjercicio3.repository;

import com.upc.OpenSourceEjercicio3.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Boolean existsByNameCustomerAndNumberAccount(String nameCustomer, String numberAccount);
}
