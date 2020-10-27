package com.OnlineWashing.repo;

import com.OnlineWashing.model.Draft;
import com.OnlineWashing.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DraftRepo extends JpaRepository<Draft, Long> {
}
