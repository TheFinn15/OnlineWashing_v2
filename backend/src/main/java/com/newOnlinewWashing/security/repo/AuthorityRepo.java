package com.newOnlinewWashing.security.repo;

import com.newOnlinewWashing.security.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepo extends JpaRepository<Authority, String> {
}
