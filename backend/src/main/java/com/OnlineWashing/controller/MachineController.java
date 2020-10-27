package com.OnlineWashing.controller;

import com.OnlineWashing.exception.machine.MachineNotFoundException;
import com.OnlineWashing.model.Draft;
import com.OnlineWashing.model.Machine;
import com.OnlineWashing.repo.MachineRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/machines")
@CrossOrigin
public class MachineController {
    private final MachineRepo machineRepo;

    public MachineController(MachineRepo machineRepo) {
        this.machineRepo = machineRepo;
    }

    @GetMapping("reports/bad-status-machines")
    public ArrayList<Machine> getReportBadMachines() {
        ArrayList<Machine> drafts = new ArrayList<>();
        for(int i=0;i<machineRepo.findAll().size();i++) {
            Machine machine = machineRepo.findAll().get(i);
            if (machine.getStatus().equals("red")) {
                drafts.add(machine);
            }
        }
        return drafts;
    }

    @GetMapping
    public List<Machine> machineList() {return machineRepo.findAll();}

    @GetMapping("{id}")
    public Machine getOne(@PathVariable("id") Machine machine){
        if (machine == null) throw new MachineNotFoundException();
        return machine;
    }

    @PostMapping
    public Object create(@RequestBody Machine machine) {
        try {
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
            if (machine.getStatus() == null) machine.setStatus("green");
            machine.setLastUpdateRow(dateTime.format(formatter));
            return machineRepo.save(machine);
        } catch (Exception ex) {
            return new ResponseEntity<Object>("Проверьте все важные поля либо попробуйте позже.", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{id}")
    public Machine update(@PathVariable Long id, @RequestBody Machine newMachine) {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
        return machineRepo.findById(id)
                .map(machine -> {
                    if (newMachine.getName() != null) { machine.setName(newMachine.getName()); }
                    if (newMachine.getStock() != null) { machine.setStock(newMachine.getStock()); }
                    if (newMachine.getPercentReady() != null) { machine.setPercentReady(newMachine.getPercentReady()); }
                    if (newMachine.getCapacity() != null) { machine.setCapacity(newMachine.getCapacity()); }
                    if (newMachine.getDescription() != null) { machine.setDescription(newMachine.getDescription()); }
                    if (newMachine.getStatus() != null) { machine.setStatus(newMachine.getStatus()); }
                    if (newMachine.getPrice() != null) { machine.setPrice(newMachine.getPrice()); }
                    if (newMachine.getImg() != null) { machine.setImg(newMachine.getImg()); }
                    machine.setLastUpdateRow(dateTime.format(formatter));
                    return machineRepo.save(machine);
                })
                .orElseGet(() -> {
                    newMachine.setId(id);
                    return machineRepo.save(newMachine);
                });
    }

    @DeleteMapping("{id}")
    public ObjectNode delete(@PathVariable("id") Machine machine) {
        ObjectNode obj = new ObjectMapper().createObjectNode();
        obj.put("status", 200);
        obj.put("info", "Machine "+machine.getName()+" "+machine.getCapacity()+" was deleted");
        machineRepo.delete(machine);
        return obj;
    }
}
