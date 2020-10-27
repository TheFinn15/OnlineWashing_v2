package com.OnlineWashing.controller;

import com.OnlineWashing.exception.historyTrans.HistoryTransactionNotFoundException;
import com.OnlineWashing.model.HistoryTransaction;
import com.OnlineWashing.repo.HistoryTransactionRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/histories")
@CrossOrigin
public class HistoryTransactionController {
    private final HistoryTransactionRepo historyTransactionRepo;

    public HistoryTransactionController(HistoryTransactionRepo historyTransactionRepo) {
        this.historyTransactionRepo = historyTransactionRepo;
    }

    @GetMapping
    public List<HistoryTransaction> historyTransactionList() {return historyTransactionRepo.findAll();}

    @GetMapping("{id}")
    public HistoryTransaction getOne(@PathVariable("id") HistoryTransaction historyTransaction){
        if (historyTransaction == null) throw new HistoryTransactionNotFoundException();
        return historyTransaction;
    }

    @PostMapping
    public Object create(@RequestBody HistoryTransaction historyTransaction) {
        try {
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
            historyTransaction.setLastUpdateRow(dateTime.format(formatter));
            return historyTransactionRepo.save(historyTransaction);
        } catch (Exception ex) {
            return new ResponseEntity<Object>("Проверьте все важные поля либо попробуйте позже.", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{id}")
    public HistoryTransaction update(@PathVariable Long id, @RequestBody HistoryTransaction newHistoryTransaction) {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
        return historyTransactionRepo.findById(id)
                .map(historyTransaction -> {
                    if (newHistoryTransaction.getDate() != null) { historyTransaction.setDate(newHistoryTransaction.getDate()); }
                    if (newHistoryTransaction.getSum() != null) { historyTransaction.setSum(newHistoryTransaction.getSum()); }
                    historyTransaction.setLastUpdateRow(dateTime.format(formatter));
                    return historyTransactionRepo.save(historyTransaction);
                })
                .orElseGet(() -> {
                    newHistoryTransaction.setId(id);
                    return historyTransactionRepo.save(newHistoryTransaction);
                });
    }

    @DeleteMapping("{id}")
    public ObjectNode delete(@PathVariable("id") HistoryTransaction historyTransaction) {
        ObjectNode obj = new ObjectMapper().createObjectNode();
        obj.put("status", 200);
        obj.put("info", "HistoryTransaction "+historyTransaction.getSum()+" "+historyTransaction.getDate()+" was deleted");
        historyTransactionRepo.delete(historyTransaction);
        return obj;
    }
}
