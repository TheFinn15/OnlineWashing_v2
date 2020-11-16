package com.OnlineWashing.repo;

import com.OnlineWashing.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepo extends JpaRepository<Person, Long> {
    @Query("select u from Person u where u.sessionId = ?1")
    Person findPersonBySessionId(String sessionId);
}
