package com.OnlineWashing.controller;

import com.OnlineWashing.exception.person.PersonNotFoundException;
import com.OnlineWashing.exception.stock.StockNotFoundException;
import com.OnlineWashing.model.Person;
import com.OnlineWashing.model.Stock;
import com.OnlineWashing.repo.StockRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/stocks")
@CrossOrigin
public class StockController {
    private final StockRepo stockRepo;

    public StockController(StockRepo stockRepo) {
        this.stockRepo = stockRepo;
    }

    @GetMapping
    public List<Stock> stockList() {return stockRepo.findAll();}

    @GetMapping("{id}")
    public Stock getOne(@PathVariable("id") Stock stock){
        if (stock == null) throw new StockNotFoundException();
        return stock;
    }

    @PostMapping
    public Object create(@RequestBody Stock stock) {
        try {
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
            stock.setLastUpdateRow(dateTime.format(formatter));
            return stockRepo.save(stock);
        } catch (Exception ex) {
            return new ResponseEntity<Object>("Проверьте все важные поля либо попробуйте позже.", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{id}")
    public Stock update(@PathVariable Long id, @RequestBody Stock newStock) {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
        return stockRepo.findById(id)
                .map(stock -> {
                    if (newStock.getName() != null) { stock.setName(newStock.getName()); }
                    if (newStock.getSponsor() != null) { stock.setSponsor(newStock.getSponsor()); }
                    if (newStock.getDiscount() != 0) { stock.setDiscount(newStock.getDiscount()); }
                    if (newStock.getLastTerm() != null) { stock.setLastTerm(newStock.getLastTerm()); }
                    stock.setLastUpdateRow(dateTime.format(formatter));
                    return stockRepo.save(stock);
                })
                .orElseGet(() -> {
                    newStock.setId(id);
                    return stockRepo.save(newStock);
                });
    }

    @DeleteMapping("{id}")
    public ObjectNode delete(@PathVariable("id") Stock stock) {
        ObjectNode obj = new ObjectMapper().createObjectNode();
        obj.put("status", 200);
        obj.put("info", "Stock "+stock.getName()+" "+stock.getSponsor()+" was deleted");
        stockRepo.delete(stock);
        return obj;
    }
}
