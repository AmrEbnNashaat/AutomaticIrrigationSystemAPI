package com.amr.irrigation.controllers;


import com.amr.irrigation.models.Plot;
import com.amr.irrigation.models.TimeSlot;
import com.amr.irrigation.repositories.PlotRepository;
import com.amr.irrigation.repositories.TimeslotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@EnableAsync
@RestController
@RequestMapping("api/v1/plot")
public class PlotController {
    private final PlotRepository plotRepository;
    private final TimeslotRepository timeslotRepository;
    public PlotController(PlotRepository plotRepository, TimeslotRepository timeslotRepository) {
        this.plotRepository = plotRepository;
        this.timeslotRepository = timeslotRepository;
    }


    //GET ALL PLOTS
    @GetMapping
    public List<Plot> getPlots() {
        return plotRepository.findAll();
    }
    //area, List<TimeSlot> neededTimeSlots


    @PostMapping
    public ResponseEntity<?> createPlot(@RequestBody Plot plot) {
        if (plot.getTimeSlotId() != 0) {
            return ResponseEntity.badRequest().body("Invalid timeSlotId. timeSlotId should be 0.");
        }

        if (plot.isBeingWatered()) {
            return ResponseEntity.badRequest().body("Invalid isBeingWatered flag. isBeingWatered flag should be false.");
        }
        plot.setTimeSlotId(0);
        plot.setBeingWatered(false);
        plot.setWaterLevelLow(false);
        Plot createdPlot = plotRepository.save(plot);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlot);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlot(@PathVariable Integer id, @RequestBody Plot updatedPlot) {
        Optional<Plot> optionalPlot = plotRepository.findById(id);
        if (optionalPlot.isPresent()) {
            Plot existingPlot = optionalPlot.get();

            if (updatedPlot.getTimeSlotId() != null && updatedPlot.getTimeSlotId() > 0) {
                Optional<TimeSlot> optionalTimeSlot = timeslotRepository.findById(updatedPlot.getTimeSlotId());
                if (optionalTimeSlot.isPresent()) {
                    TimeSlot timeSlot = optionalTimeSlot.get();
                    if (timeSlot.isAssigned() || timeSlot.getWaterAmount() > 0) {
                        return ResponseEntity.badRequest().body("Invalid timeSlotId. The specified time slot is already assigned or contains water.");
                    }
                } else {
                    return ResponseEntity.badRequest().body("Invalid timeSlotId. The specified time slot does not exist.");
                }
            }

            if (updatedPlot.isBeingWatered() && (updatedPlot.getTimeSlotId() == null || updatedPlot.getTimeSlotId() <= 0)) {
                return ResponseEntity.badRequest().body("Invalid beingWatered flag. The beingWatered flag cannot be true without assigning a valid timeSlotId.");
            }

            if (updatedPlot.getArea() < 0) {
                return ResponseEntity.badRequest().body("Invalid area. Area cannot be negative.");
            }

            if (updatedPlot.getCurrentWaterLevel() > 100) {
                return ResponseEntity.badRequest().body("Invalid currentWaterLevel. Current water level cannot exceed 100.");
            }

            if (updatedPlot.isWaterLevelLow() && updatedPlot.getCurrentWaterLevel() > 20) {
                return ResponseEntity.badRequest().body("Invalid isWaterLevelLow flag. The water level is above 20, so isWaterLevelLow cannot be true.");
            }

            existingPlot.setArea(updatedPlot.getArea());
            existingPlot.setTimeSlotId(updatedPlot.getTimeSlotId());
            existingPlot.setBeingWatered(updatedPlot.isBeingWatered());
            existingPlot.setCurrentWaterLevel(updatedPlot.getCurrentWaterLevel());
            existingPlot.setWaterLevelLow(updatedPlot.isWaterLevelLow());

            Plot savedPlot = plotRepository.save(existingPlot);
            return ResponseEntity.ok(savedPlot);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlot(@PathVariable Integer id) {
        Optional<Plot> optionalPlot = plotRepository.findById(id);
        if (optionalPlot.isPresent()) {
            Plot plot = optionalPlot.get();
            plotRepository.delete(plot);
            return ResponseEntity.ok("Plot " + id + " deleted SUCCESSFULLY!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/{plotId}/assign")
    public ResponseEntity<String> assignTimeSlotToPlot(
            @PathVariable Integer plotId, @RequestParam Integer timeSlotId) {
        Optional<Plot> optionalPlot = plotRepository.findById(plotId);
        Optional<TimeSlot> optionalTimeSlot = timeslotRepository.findById(timeSlotId);

        if (optionalPlot.isPresent() && optionalTimeSlot.isPresent()) {
            Plot plot = optionalPlot.get();
            TimeSlot timeSlot = optionalTimeSlot.get();

            if (timeSlot.isAssigned()) {
                return ResponseEntity.badRequest().body("TimeSlot is already assigned.");
            }

            if (plot.getTimeSlotId() != 0 && !plot.getTimeSlotId().equals(timeSlotId)) {
                return ResponseEntity.badRequest().body("The Plot is already assigned to a different TimeSlot.");
            }

            if (timeSlot.getPlotId() != 0 && !timeSlot.getPlotId().equals(plotId)) {
                return ResponseEntity.badRequest().body("The TimeSlot is already assigned to a different Plot.");
            }

            if (plot.getTimeSlotId() == 0) {
                plot.setTimeSlotId(timeSlotId);
                timeSlot.setPlotId(plotId);
                timeslotRepository.save(timeSlot);
                plotRepository.save(plot);
                return ResponseEntity.ok("TimeSlot assigned to the Plot successfully.");
            } else {
                return ResponseEntity.badRequest().body("The Plot is already assigned to the specified TimeSlot.");
            }
        } else {
            return ResponseEntity.badRequest().body("Invalid Plot ID or TimeSlot ID.");
        }
    }






}






