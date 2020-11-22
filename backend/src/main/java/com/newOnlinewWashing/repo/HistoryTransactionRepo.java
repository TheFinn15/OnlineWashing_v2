package com.newOnlinewWashing.repo;

import com.newOnlinewWashing.models.HistoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryTransactionRepo extends JpaRepository<HistoryTransaction, Long> {
}
