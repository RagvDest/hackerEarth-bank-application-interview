package com.devsu.hackerearth.backend.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.devsu.hackerearth.backend.account.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    boolean existsById(Long id);

    boolean existsByAccountId(Long accountId);

    List<Transaction> findByAccountIdAndDateBetweenOrderByDateDesc(Long accountId, Date startDate, Date endDate);

    Optional<Transaction> findFirstByAccountIdOrderByDateDesc(Long accountId);
}
