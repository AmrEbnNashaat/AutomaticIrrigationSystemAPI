package com.amr.irrigation.repositories;

import com.amr.irrigation.models.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TimeslotRepository extends JpaRepository<TimeSlot, Integer> {
}
