package com.newOnlinewWashing.security.rest;

import com.newOnlinewWashing.security.model.Person;
import com.newOnlinewWashing.security.repo.PersonRepo;
import com.newOnlinewWashing.security.service.PersonService;
import com.newOnlinewWashing.security.model.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/api")
public class PersonRestController {
    @Autowired
    private PersonRepo userRepository;

    private final PersonService userService;


    public PersonRestController(PersonService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<Person> getActualUser() {
        return ResponseEntity.ok(userService.getUserWithAuthorities().get());
    }

    @PostMapping("/register")
    public ResponseEntity<Person> saveUser(@RequestBody @Valid Person user){
        HttpHeaders headers = new HttpHeaders();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if(user == null){
            System.out.println(user.getPwd());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        user.setActivated(true);
        user.setAuthorities(Collections.singleton(new Authority("ROLE_USER")));
        user.setPwd(encoder.encode(user.getPwd()));
        this.userService.save(user);

        return new ResponseEntity<>(user, headers, HttpStatus.CREATED);
    }

    @PutMapping("/user")
    public ResponseEntity<Person> updateUser(@RequestBody Person user){
        HttpHeaders headers = new HttpHeaders();

        Person authUser = userService.getUserWithAuthorities().get();

        Person userToChange = userRepository.findByLogin(authUser.getLogin());

        if(user.getPhone() != null) {
            userToChange.setPhone(user.getPhone());
        }
        if(user.getsName() != null) {
            userToChange.setsName(user.getsName());
        }
        if(user.getfName() != null) {
            userToChange.setfName(user.getfName());
        }
        if(user.getEmail() != null) {
            userToChange.setEmail(user.getEmail());
        }
        if(user.getAvatar() != null){
            userToChange.setAvatar(user.getAvatar());
        }

        if(user == null){
            System.out.println(user.getsName());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        this.userService.save(userToChange);

        return new ResponseEntity<>(user, headers, HttpStatus.OK);
    }
}
