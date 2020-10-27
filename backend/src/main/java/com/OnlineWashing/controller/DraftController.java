package com.OnlineWashing.controller;

import com.OnlineWashing.exception.draft.DraftNotFoundException;
import com.OnlineWashing.model.Draft;
import com.OnlineWashing.model.HistoryTransaction;
import com.OnlineWashing.model.Person;
import com.OnlineWashing.repo.DraftRepo;
import com.OnlineWashing.repo.PersonRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("api/drafts")
@CrossOrigin
public class DraftController {
    private final DraftRepo draftRepo;

    public DraftController(DraftRepo draftRepo) {
        this.draftRepo = draftRepo;
    }

    @GetMapping("reports/drafts/monthly-checks")
    public ArrayList<Draft> getReportDraftsMonth(){
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
        String[] dateElems = dateTime.format(formatter).split("-");
        ArrayList<Draft> drafts = new ArrayList<>();
        for(int i=0;i<draftRepo.findAll().size();i++) {
            Draft draft = draftRepo.findAll().get(i);
            if (draft.getDate().split("-")[1].equals(dateElems[1])) {
                drafts.add(draft);
            }
        }
        return drafts;
    }

    @GetMapping("filter/date/{date:\\d\\d.\\d\\d.\\d\\d\\d\\d}")
    public ArrayList<Draft> getFromDate(@PathVariable String date) {
        String[] infoRoute = date.split("\\.");
        ArrayList<Draft> drafts = new ArrayList<>();
        for(int i=0;i<draftRepo.findAll().size();i++) {
            Draft draft = draftRepo.findAll().get(i);
            String[] draftDateInfo = draft.getDate().split("-");
            if (infoRoute[0].equals(draftDateInfo[0]) && infoRoute[1].equals(draftDateInfo[1]) && infoRoute[2].equals(draftDateInfo[2])) {
                drafts.add(draft);
            }
        }
        return drafts;
    }

    @GetMapping("filter/price/{nums:\\d+-\\d+}")
    public ArrayList<Draft> getFromPrice(@PathVariable String nums) {
        String[] info = nums.split("-");
        long price_from = Long.parseLong(info[0]);
        long price_to = Long.parseLong(info[1]);
        ArrayList<Draft> drafts = new ArrayList<>();
        for(int i=0;i<draftRepo.findAll().size();i++) {
            Draft draft = draftRepo.findAll().get(i);
            if (draft.getPrice() >= price_from && draft.getPrice() <= price_to) {
                drafts.add(draft);
            }
        }
        return drafts;
    }

    @GetMapping
    public List<Draft> draftList() {return draftRepo.findAll();}

    @GetMapping("{id}")
    public Draft getOne(@PathVariable("id") Draft draft){
        if (draft == null) throw new DraftNotFoundException();
        return draft;
    }

    @PostMapping
    public Object create(@RequestBody Draft draft) {
        try {
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
            if (draft.getDate() == null) draft.setDate(dateTime.format(formatter));
            if (!draft.getPaymentType().equals("credit")) draft.setCreditCard("оплачено без карты");
            draft.setLastUpdateRow(dateTime.format(formatter));
            return draftRepo.save(draft);
        } catch (Exception ex) {
            return new ResponseEntity<Object>("Проверьте все важные поля либо попробуйте позже.", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{id}")
    public Draft update(@PathVariable Long id, @RequestBody Draft newDraft) {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
        return draftRepo.findById(id)
                .map(draft -> {
                    if (newDraft.getPerson() != null) { draft.setPerson(newDraft.getPerson()); }
                    if (newDraft.getMachine() != null) { draft.setMachine(newDraft.getMachine()); }
                    if (newDraft.getPrice() != null) { draft.setPrice(newDraft.getPrice()); }
                    if (newDraft.getVolume() != null) { draft.setVolume(newDraft.getVolume()); }
                    if (newDraft.getAdditional() != null) { draft.setAdditional(newDraft.getAdditional()); }
                    if (newDraft.getPaymentType() != null) { draft.setPaymentType(newDraft.getPaymentType()); }
                    if (newDraft.getCreditCard() != null) { draft.setCreditCard(newDraft.getCreditCard()); }
                    draft.setLastUpdateRow(dateTime.format(formatter));
                    return draftRepo.save(draft);
                })
                .orElseGet(() -> {
                    newDraft.setId(id);
                    return draftRepo.save(newDraft);
                });
    }

    @DeleteMapping("{id}")
    public ObjectNode delete(@PathVariable("id") Draft draft) {
        ObjectNode obj = new ObjectMapper().createObjectNode();
        obj.put("status", 200);
        obj.put("info", "Draft " + draft.getPerson() + " | " + draft.getPrice() + " | " + draft.getDate() + " was deleted");
        draftRepo.delete(draft);
        return obj;
    }
}
