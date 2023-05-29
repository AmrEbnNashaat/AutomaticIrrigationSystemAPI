package com.amr.irrigation.controllers;


import com.amr.irrigation.models.Plot;
import com.amr.irrigation.models.TimeSlot;
import com.amr.irrigation.repositories.PlotRepository;
import com.amr.irrigation.repositories.TimeslotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/timeslot")
public class TimeslotController {
    private final TimeslotRepository timeslotRepository;
    private final PlotRepository plotRepository;
    public TimeslotController(TimeslotRepository timeslotRepository, PlotRepository plotRepository) {
        this.timeslotRepository = timeslotRepository;
        this.plotRepository = plotRepository;
    }

    //GET ALL TIMESLOTS
    @GetMapping
    public List<TimeSlot> getTimeSlots() {
        return timeslotRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createTimeSlot(@RequestBody TimeSlot timeSlot) {
        if (timeSlot.getPlotId() != null && timeSlot.getPlotId() != 0) {
            return ResponseEntity.badRequest().body("Invalid plot ID. Plot ID should be 0.");
        }
        if (timeSlot.getPlotId() == null) {
            timeSlot.setPlotId(0);
        }

        if (timeSlot.isAssigned()) {
            timeSlot.setAssigned(false);
            //return ResponseEntity.badRequest().body("Invalid assignment flag. Assignment flag should be false.");
        }

        if (timeSlot.getWaterAmount() > 100) {
            return ResponseEntity.badRequest().body("Invalid water amount. Water amount should be at most 100.");
        }

        if (timeSlot.getDuration() > 100) {
            return ResponseEntity.badRequest().body("Invalid duration. Duration should be at most 100.");
        }

        if (timeSlot.getWaterAmount() > timeSlot.getDuration()) {
            return ResponseEntity.badRequest().body("Invalid water amount. Water amount cannot be greater than duration.");
        }

        TimeSlot createdTimeSlot = timeslotRepository.save(timeSlot);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTimeSlot);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTimeSlot(@PathVariable Integer id, @RequestBody TimeSlot updatedTimeSlot) {
        Optional<TimeSlot> optionalTimeSlot = timeslotRepository.findById(id);

        if (optionalTimeSlot.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        TimeSlot existingTimeSlot = optionalTimeSlot.get();

        // Check if isAssigned flag is being changed
        if (updatedTimeSlot.isAssigned() != existingTimeSlot.isAssigned()) {
            if (updatedTimeSlot.getPlotId() == null || updatedTimeSlot.getPlotId() == 0) {
                return ResponseEntity.badRequest().body("Invalid plot ID. Plot ID is required when changing the assignment flag.");
            }
            // Check if the plot exists
            Optional<Plot> optionalPlot = plotRepository.findById(updatedTimeSlot.getPlotId());
            if (optionalPlot.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid plot ID. Plot with the provided ID does not exist.");
            }
        }

        // Check if waterAmount is greater than duration
        if (updatedTimeSlot.getWaterAmount() > updatedTimeSlot.getDuration()) {
            return ResponseEntity.badRequest().body("Invalid water amount. Water amount cannot be greater than duration.");
        }


        existingTimeSlot.setDuration(updatedTimeSlot.getDuration());
        existingTimeSlot.setWaterAmount(updatedTimeSlot.getWaterAmount());
        existingTimeSlot.setPlotId(0);
        existingTimeSlot.setAssigned(updatedTimeSlot.isAssigned());

        TimeSlot updatedTimeSlotEntity = timeslotRepository.save(existingTimeSlot);
        return ResponseEntity.ok(updatedTimeSlotEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTimeSlot(@PathVariable Integer id) {
        Optional<TimeSlot> optionalTimeSlot = timeslotRepository.findById(id);

        if (optionalTimeSlot.isEmpty()) {
            return ResponseEntity.badRequest().body("TimeSlot with ID " + id + " not found!");

        }

        TimeSlot timeSlot = optionalTimeSlot.get();
        timeslotRepository.delete(timeSlot);

        return ResponseEntity.badRequest().body("TimeSlot with ID " + id + " is DELETED!");
    }

}
