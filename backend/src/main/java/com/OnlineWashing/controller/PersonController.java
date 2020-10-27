package com.OnlineWashing.controller;


import com.OnlineWashing.exception.person.PersonNotFoundException;
import com.OnlineWashing.model.Person;
import com.OnlineWashing.model.Wallet;
import com.OnlineWashing.repo.PersonRepo;
import com.OnlineWashing.repo.WalletRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/persons")
public class PersonController {
    private final PersonRepo personRepo;
    private final WalletRepo walletRepo;

    public PersonController(PersonRepo personRepo, WalletRepo walletRepo) {
        this.personRepo = personRepo;
        this.walletRepo = walletRepo;
    }

    @GetMapping
    public List<Person> personList() {
        return personRepo.findAll();
    }

    @GetMapping("{id}")
    public Person getOne(@PathVariable("id") Person person){
        if (person == null) throw new PersonNotFoundException();
        return person;
    }

    @PostMapping
    public Object create(@RequestBody Person person) {
        try {
            Wallet newWallet = new Wallet();
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
            newWallet.setLastUpdateRow(dateTime.format(formatter));
            newWallet.setBalance(0L);
            walletRepo.save(newWallet);
            person.setLastUpdateRow(dateTime.format(formatter));
            person.setWallet(newWallet);
            return personRepo.save(person);
        } catch (Exception ex) {
            System.out.println(ex);
            return new ResponseEntity<Object>("Проверьте все важные поля либо попробуйте позже.", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{id}")
    public Person update(@PathVariable Long id, @RequestBody Person newPerson) {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
        return personRepo.findById(id)
                .map(person -> {
                    if (newPerson.getMachine() != null) { person.setMachine(newPerson.getMachine()); }
                    if (newPerson.getfName() != null) { person.setfName(newPerson.getfName()); }
                    if (newPerson.getsName() != null) { person.setsName(newPerson.getsName()); }
                    if (newPerson.getLogin() != null) { person.setLogin(newPerson.getLogin()); }
                    if (newPerson.getPwd() != null) { person.setPwd(newPerson.getPwd()); }
                    if (newPerson.getEmail() != null) { person.setEmail(newPerson.getEmail()); }
                    if (newPerson.getPhone() != null) { person.setPhone(newPerson.getPhone()); }
                    if (newPerson.getAvatar() != null) { person.setAvatar(newPerson.getAvatar()); }
                    if (newPerson.getWallet() != null) { person.setWallet(newPerson.getWallet()); }
                    if (newPerson.getSessionId() != null) {person.setSessionId(newPerson.getSessionId());}
                    person.setLastUpdateRow(dateTime.format(formatter));
                    return personRepo.save(person);
                })
                .orElseGet(() -> {
                    newPerson.setId(id);
                    return personRepo.save(newPerson);
                });
    }

    @DeleteMapping("{id}")
    public ObjectNode delete(@PathVariable("id") Person person) {
        Wallet wallet = person.getWallet();
        ObjectNode obj = new ObjectMapper().createObjectNode();
        obj.put("status", 200);
        obj.put("info", "User "+person.getfName()+" "+person.getsName()+" was deleted");
        personRepo.delete(person);
        walletRepo.delete(wallet);
        return obj;
    }
}