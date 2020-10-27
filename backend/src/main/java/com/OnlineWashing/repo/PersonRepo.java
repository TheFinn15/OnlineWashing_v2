package com.OnlineWashing.repo;

import com.OnlineWashing.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepo extends JpaRepository<Person, Long> {
}
