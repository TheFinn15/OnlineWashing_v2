package com.newOnlinewWashing.repo;

import com.newOnlinewWashing.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepo extends JpaRepository<Stock, Long> {
}
