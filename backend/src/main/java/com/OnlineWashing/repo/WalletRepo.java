package com.OnlineWashing.repo;

import com.OnlineWashing.model.Draft;
import com.OnlineWashing.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepo extends JpaRepository<Wallet, Long> {
}
