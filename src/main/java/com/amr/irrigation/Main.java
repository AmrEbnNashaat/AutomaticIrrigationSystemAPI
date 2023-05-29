package com.amr.irrigation;

import com.amr.irrigation.models.Plot;
import com.amr.irrigation.models.TimeSlot;
import com.amr.irrigation.repositories.PlotRepository;
import com.amr.irrigation.repositories.TimeslotRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@EnableScheduling
@RestController
public class Main {
    private final PlotRepository plotRepository;
    private final TimeslotRepository timeslotRepository;
    private int consecutiveNoTimeSlots = 0;

    public Main(PlotRepository plotRepository, TimeslotRepository timeslotRepository) {
        this.plotRepository = plotRepository;
        this.timeslotRepository = timeslotRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    @Scheduled(fixedDelay = 1000)
    public void decreasePlotWaterSupply() {
        List<Plot> plots = plotRepository.findAll();
        for(Plot plot : plots) {
            if(plot.getTimeSlotId() == 0 && plot.getCurrentWaterLevel() > 0) {
                plot.setCurrentWaterLevel(plot.getCurrentWaterLevel() - 1);
                plotRepository.save(plot);
            }
        }
    }
    @Scheduled(fixedDelay = 1000) // Run every second
    public void waterPlots() {
        System.out.println("In Func");
        List<Plot> plots = plotRepository.findAll();
        for (Plot plot : plots) {
            Integer timeSlotId = plot.getTimeSlotId();
            if(!timeSlotId.equals(0)) {
                //If this plot is assigned to another timeslot
                TimeSlot timeSlot = timeslotRepository.findById(timeSlotId).orElse(null);
                if(timeSlot != null) {
                    //duration of timeSlot
                    int duration = timeSlot.getDuration();
                    //waterAmount in timeSlot
                    int timeSlotWaterAmount = timeSlot.getWaterAmount();
                    //Plot's current water level
                    double plotWaterLevel = plot.getCurrentWaterLevel();

                    if(duration > 0 && timeSlotWaterAmount > 0 && plotWaterLevel < 100) {
                        //we start decrementing
                        duration--;
                        timeSlotWaterAmount--;
                        plotWaterLevel++;

                        timeSlot.setDuration(duration);
                        //we decrement timeSlot's waterAmount and inc the plot's
                        timeSlot.setWaterAmount(timeSlotWaterAmount);
                        plot.setCurrentWaterLevel(plotWaterLevel);
                        //We set the flags
                        timeSlot.setAssigned(true);
                        plot.setBeingWatered(true);

                        //we then save

                        System.out.println("Current Duration for TimeSlot ID -> "+ timeSlot.getId() + " = " + duration);
                        System.out.println("Current water level for TimeSlot ID -> "+ timeSlot.getId() + " = " + timeSlotWaterAmount);
                        System.out.println("Current water level for Plot ID -> "+ plot.getId() + " = " + plotWaterLevel);
                        System.out.println("================");

                    } else {
                        //We stop watering.
                        plot.setTimeSlotId(0);
                        plot.setBeingWatered(false);
                        plot.setCurrentWaterLevel(plotWaterLevel);

                        timeSlot.setPlotId(0);
                        timeSlot.setAssigned(false);
                        timeSlot.setWaterAmount(timeSlotWaterAmount);

                    }
                    //We then save the new values
                    timeslotRepository.save(timeSlot);
                    plotRepository.save(plot);
                }
            }
        }
    }

    @Scheduled(fixedDelay = 1000) // Run every second
    public void assignTimeSlots() {
        boolean startCancelling;
        long serverStartTime = System.currentTimeMillis();
        List<Plot> plots = plotRepository.findAll();
        Plot plotWithLowestWaterLevel = null;
        double lowestWaterLevel = Double.MAX_VALUE;

        for (Plot plot : plots) {
            if (!plot.isBeingWatered() && plot.getTimeSlotId() == 0) {
                double waterLevel = plot.getCurrentWaterLevel();

                if(waterLevel < lowestWaterLevel) {
                    lowestWaterLevel = waterLevel;
                    plotWithLowestWaterLevel = plot;
                }
            }
        }

        if (plotWithLowestWaterLevel != null) {
            System.out.println("Assigning TimeSlots to Plots..");
            List<TimeSlot> timeSlots = timeslotRepository.findAll();
            TimeSlot freeTimeSlot = null;
            int maxWaterLevel = 0;

            for (TimeSlot timeSlot : timeSlots) {
                //if TimeSlot is not busy
                if(!timeSlot.isAssigned() && timeSlot.getPlotId() == 0 && timeSlot.getWaterAmount() > maxWaterLevel) {
                    freeTimeSlot = timeSlot;
                    maxWaterLevel = timeSlot.getWaterAmount();
                }
            }

            if (freeTimeSlot != null) {
                plotWithLowestWaterLevel.setTimeSlotId(freeTimeSlot.getId());
                freeTimeSlot.setPlotId(plotWithLowestWaterLevel.getId());
                plotRepository.save(plotWithLowestWaterLevel);
                timeslotRepository.save(freeTimeSlot);
                System.out.println("Assigned TimeSlot ID " + freeTimeSlot.getId() + " to Plot ID " + plotWithLowestWaterLevel.getId());
                consecutiveNoTimeSlots = 0;
            } else {
                consecutiveNoTimeSlots++;
                if (consecutiveNoTimeSlots >= 15) {

                    System.out.println("SYSTEM FAILED: We have been retrying the sensor, but found no time slots to assign to plots. Please add more TimeSlots!");
                    //System.exit(0);
                }
            }
        } else {
            System.out.println("No plots exist!");
            //No plotsl eft
        }
    }
}
