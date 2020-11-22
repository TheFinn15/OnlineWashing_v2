package com.newOnlinewWashing.repo;

import com.newOnlinewWashing.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepo extends JpaRepository<Wallet, Long> {
}
