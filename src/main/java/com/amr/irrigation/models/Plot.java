package com.amr.irrigation.models;

import jakarta.persistence.*;

@Entity
public class Plot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plot_id_sequence")
    @SequenceGenerator(name = "plot_id_sequence", sequenceName = "plot_id_sequence", allocationSize = 1)
    private Integer id;
    private double area;
    private Integer timeSlotId = 0; // Initialize with 0
    private boolean isBeingWatered;
    private double currentWaterLevel;
    private boolean isWaterLevelLow = false;
    public Plot() {
    }
    public Plot(double area, int timeSlotId, boolean isBeingWatered, double currentWaterLevel) {
        this.area = area;
        this.timeSlotId = 0;
        this.isBeingWatered = false;
        this.currentWaterLevel = this.getCurrentWaterLevel();
    }

    public Plot(Integer id, double area, Integer timeSlotId, boolean isBeingWatered, double currentWaterLevel, boolean isWaterLevelLow) {
        this.id = id;
        this.area = area;
        this.timeSlotId = 0;
        this.isBeingWatered = isBeingWatered;
        this.currentWaterLevel = currentWaterLevel;
        this.isWaterLevelLow = isWaterLevelLow;
    }
    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public Integer getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Integer timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public boolean isBeingWatered() {
        return isBeingWatered;
    }

    public void setBeingWatered(boolean beingWatered) {
        isBeingWatered = beingWatered;
    }

    public double getCurrentWaterLevel() {
        return currentWaterLevel;
    }

    public void setCurrentWaterLevel(double currentWaterLevel) {
        this.currentWaterLevel = currentWaterLevel;
    }

    public boolean isWaterLevelLow() {
        return isWaterLevelLow;
    }

    public void setWaterLevelLow(boolean waterLevelLow) {
        isWaterLevelLow = waterLevelLow;
    }
}
