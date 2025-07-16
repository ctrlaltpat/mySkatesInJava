package com.ctrlaltpat.MySkates.repository;

import com.ctrlaltpat.MySkates.model.RollerSkatesPair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RollerSkatesRepository extends JpaRepository<RollerSkatesPair, Long> {
}