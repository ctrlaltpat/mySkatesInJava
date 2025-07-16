package com.ctrlaltpat.MySkates.service;

import com.ctrlaltpat.MySkates.model.RollerSkatesPair;
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

    public List<RollerSkatesPair> getAllRollerSkates() {
        return rollerSkatesRepository.findAll();
    }

    public Optional<RollerSkatesPair> getRollerSkatesById(Long id) {
        return rollerSkatesRepository.findById(id);
    }

    public RollerSkatesPair createRollerSkates(RollerSkatesPair rollerSkatesPair) {
        return rollerSkatesRepository.save(rollerSkatesPair);
    }

    public RollerSkatesPair updateRollerSkates(Long id, RollerSkatesPair rollerSkatesDetails) {
        RollerSkatesPair pair = rollerSkatesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Roller Skates not found"));

        pair.setName(rollerSkatesDetails.getName());
        pair.setBrand(rollerSkatesDetails.getBrand());
        pair.setPlates(rollerSkatesDetails.getPlates());
        pair.setWheels(rollerSkatesDetails.getWheels());

        return rollerSkatesRepository.save(pair);
    }

    public void deleteRollerSkates(Long id) {
        RollerSkatesPair pair = rollerSkatesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Roller Skates not found"));

        rollerSkatesRepository.delete(pair);
    }
}
