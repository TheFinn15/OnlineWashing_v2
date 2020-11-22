package com.newOnlinewWashing.security.repo;

import com.newOnlinewWashing.security.model.Person;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonRepo extends JpaRepository<Person, Long> {

    @EntityGraph(attributePaths = "authorities")
    Optional<Person> findOneWithAuthoritiesByLogin(String username);

    @EntityGraph(attributePaths = "authorities")
    Optional<Person> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Person findByLogin(String username);

    @Modifying
    @Query("UPDATE Person u SET u.phone = :phone WHERE u.id = :id")
    void updatePhone(@Param("id") Long id, @Param("phone") String phone);

}