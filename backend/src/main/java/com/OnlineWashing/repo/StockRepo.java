package com.OnlineWashing.repo;

import com.OnlineWashing.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepo extends JpaRepository<Stock, Long> {
}
