package com.newOnlinewWashing.repo;

import com.newOnlinewWashing.models.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineRepo extends JpaRepository<Machine, Long> {
}
