package com.ctrlaltpat.MySkates.controller;

import com.ctrlaltpat.MySkates.model.RollerSkates;
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
    public List<RollerSkates> getAllRollerSkatess() {
        return rollerSkatesService.getAllRollerSkates();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RollerSkates> getRollerSkatesById(@PathVariable Long id) {
        Optional<RollerSkates> rollerSkates = rollerSkatesService.getRollerSkatesById(id);
        return rollerSkates.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public RollerSkates createRollerSkates(@RequestBody RollerSkates rollerSkates) {
        return rollerSkatesService.createRollerSkates(rollerSkates);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RollerSkates> updateRollerSkates(@PathVariable Long id,
            @RequestBody RollerSkates rollerSkatesDetails) {
        RollerSkates updatedRollerSkates = rollerSkatesService.updateRollerSkates(id, rollerSkatesDetails);
        return ResponseEntity.ok(updatedRollerSkates);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRollerSkates(@PathVariable Long id) {
        rollerSkatesService.deleteRollerSkates(id);
        return ResponseEntity.noContent().build();
    }
}
