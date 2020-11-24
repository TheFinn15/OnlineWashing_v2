package com.newOnlinewWashing.security.rest;

import com.newOnlinewWashing.models.Wallet;
import com.newOnlinewWashing.repo.WalletRepo;
import com.newOnlinewWashing.security.model.Person;
import com.newOnlinewWashing.security.repo.PersonRepo;
import com.newOnlinewWashing.security.service.PersonService;
import com.newOnlinewWashing.security.model.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@RestController
@RequestMapping("/api")
public class PersonRestController {
    @Autowired
    private PersonRepo userRepository;
    @Autowired
    private WalletRepo walletRepo;

    private final PersonService userService;


    public PersonRestController(PersonService userService) {
        this.userService = userService;
    }

    @GetMapping("/persons")
    public ResponseEntity<? extends Object> getActualUser() {
        try {
            return ResponseEntity.ok(userService.getUserWithAuthorities().get());
        } catch (Exception e) {
            return new ResponseEntity<>("Users not exists", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Person> saveUser(@RequestBody @Valid Person user){
        HttpHeaders headers = new HttpHeaders();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");

        Wallet wallet = new Wallet();
        wallet.setBalance(0L);
        wallet.setLastUpdateRow(dateTime.format(formatter));
        walletRepo.save(wallet);

        if(user == null){
            System.out.println(user.getPwd());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        user.setActivated(true);
        user.setAuthorities(Collections.singleton(new Authority("ROLE_USER")));
        user.setPwd(encoder.encode(user.getPwd()));
        user.setLastUpdateRow(dateTime.format(formatter));
        user.setWallet(wallet);

        userService.save(user);

        return new ResponseEntity<>(user, headers, HttpStatus.CREATED);
    }

    @PutMapping("/persons")
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

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
        user.setLastUpdateRow(dateTime.format(formatter));

        this.userService.save(userToChange);

        return new ResponseEntity<>(user, headers, HttpStatus.OK);
    }

    @DeleteMapping("/persons")
    public ResponseEntity<Person> delUser(@RequestBody Person user) {
        HttpHeaders headers = new HttpHeaders();

        Wallet wallet = user.getWallet();
        Person authUser = userService.getUserWithAuthorities().get();

        Person userToDel = userRepository.findByLogin(authUser.getLogin());

        userRepository.delete(userToDel);
        walletRepo.delete(wallet);

        return new ResponseEntity<>(user, headers, HttpStatus.OK);
    }
}
