package com.OnlineWashing.repo;

import com.OnlineWashing.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineRepo extends JpaRepository<Machine, Long> {
}
