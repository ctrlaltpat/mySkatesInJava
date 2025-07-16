package com.ctrlaltpat.MySkates.controller;

import com.ctrlaltpat.MySkates.model.RollerSkatesPair;
import com.ctrlaltpat.MySkates.service.RollerSkatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/skates")
public class RollerSkatesController {

    @Autowired
    private RollerSkatesService rollerSkatesService;

    @GetMapping
    public List<RollerSkatesPair> getAllRollerSkatess() {
        return rollerSkatesService.getAllRollerSkates();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RollerSkatesPair> getRollerSkatesById(@PathVariable Long id) {
        Optional<RollerSkatesPair> pair = rollerSkatesService.getRollerSkatesById(id);
        return pair.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public RollerSkatesPair createRollerSkates(@RequestBody RollerSkatesPair pair) {
        return rollerSkatesService.createRollerSkates(pair);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RollerSkatesPair> updateRollerSkates(@PathVariable Long id,
            @RequestBody RollerSkatesPair rollerSkatesDetails) {
        RollerSkatesPair updatedPair = rollerSkatesService.updateRollerSkates(id, rollerSkatesDetails);
        return ResponseEntity.ok(updatedPair);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRollerSkates(@PathVariable Long id) {
        rollerSkatesService.deleteRollerSkates(id);
        return ResponseEntity.noContent().build();
    }
}
