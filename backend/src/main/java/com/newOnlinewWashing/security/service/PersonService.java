package com.newOnlinewWashing.security.service;

import com.newOnlinewWashing.security.SecurityUtils;
import com.newOnlinewWashing.security.model.Person;
import com.newOnlinewWashing.security.repo.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PersonService {

    @Autowired
    PersonRepo userRepo;


    private final PersonRepo userRepository;

    public PersonService(PersonRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Person> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    public void save(Person user) {
//      log.info("IN UserServiceImpl save {}", user);
        userRepo.save(user);
    }

}