package com.OnlineWashing.controller;

import com.OnlineWashing.exception.wallet.WalletNotFoundException;
import com.OnlineWashing.model.Wallet;
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
@RequestMapping("api/wallets")
@CrossOrigin
public class WalletController {
    private final WalletRepo walletRepo;

    public WalletController(WalletRepo walletRepo) {
        this.walletRepo = walletRepo;
    }
    @GetMapping
    public List<Wallet> walletList() {return walletRepo.findAll();}

    @GetMapping("{id}")
    public Wallet getOne(@PathVariable("id") Wallet wallet){
        if (wallet == null) throw new WalletNotFoundException();
        return wallet;
    }

    @PostMapping
    public Object create(@RequestBody Wallet wallet) {
        try {
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
            wallet.setLastUpdateRow(dateTime.format(formatter));
            return walletRepo.save(wallet);
        } catch (Exception ex) {
            return new ResponseEntity<Object>("Проверьте все важные поля либо попробуйте позже.", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{id}")
    public Wallet update(@PathVariable Long id, @RequestBody Wallet newWallet) {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
        return walletRepo.findById(id)
                .map(wallet -> {
                    if (newWallet.getBalance() != null) { wallet.setBalance(newWallet.getBalance()); }
                    if (newWallet.getHistoryTransactions() != null) { wallet.setHistoryTransactions(newWallet.getHistoryTransactions()); }
                    wallet.setLastUpdateRow(dateTime.format(formatter));
                    return walletRepo.save(wallet);
                })
                .orElseGet(() -> {
                    newWallet.setId(id);
                    return walletRepo.save(newWallet);
                });
    }

    @DeleteMapping("{id}")
    public ObjectNode delete(@PathVariable("id") Wallet wallet) {
        ObjectNode obj = new ObjectMapper().createObjectNode();
        obj.put("status", 200);
        obj.put("info", "Wallet "+wallet.getBalance()+" was deleted");
        walletRepo.delete(wallet);
        return obj;
    }
}
