package com.OnlineWashing.repo;

import com.OnlineWashing.model.Draft;
import com.OnlineWashing.model.HistoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryTransactionRepo extends JpaRepository<HistoryTransaction, Long> {
}
