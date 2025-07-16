package com.ctrlaltpat.MySkates.service;

import com.ctrlaltpat.MySkates.model.RollerSkates;
import com.ctrlaltpat.MySkates.repository.RollerSkatesRepository;
import com.ctrlaltpat.MySkates.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RollerSkatesService {
    
    @Autowired
    private RollerSkatesRepository rollerSkatesRepository;

    public List<RollerSkates> getAllRollerSkates() {
        return rollerSkatesRepository.findAll();
    }

    public Optional<RollerSkates> getRollerSkatesById(Long id) {
        return rollerSkatesRepository.findById(id);
    }

    public RollerSkates createRollerSkates(RollerSkates rollerSkates) {
        return rollerSkatesRepository.save(rollerSkates);
    }

    public RollerSkates updateRollerSkates(Long id, RollerSkates rollerSkatesDetails) {
        RollerSkates rollerSkates = rollerSkatesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Roller Skates not found"));

        rollerSkates.setName(rollerSkatesDetails.getName());
        rollerSkates.setBrand(rollerSkatesDetails.getBrand());
        rollerSkates.setPlates(rollerSkatesDetails.getPlates());
        rollerSkates.setWheels(rollerSkatesDetails.getWheels());

        return rollerSkatesRepository.save(rollerSkates);
    }

    public void deleteRollerSkates(Long id) {
        RollerSkates rollerSkates = rollerSkatesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Roller Skates not found"));

        rollerSkatesRepository.delete(rollerSkates);
    }
}
