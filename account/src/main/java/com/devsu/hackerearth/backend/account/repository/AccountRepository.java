package com.devsu.hackerearth.backend.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.devsu.hackerearth.backend.account.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByNumber(String number);

    boolean existsById(Long id);

    boolean existsByClientId(Long clientId);

    List <Account> findByClientId(Long clientId);
}
