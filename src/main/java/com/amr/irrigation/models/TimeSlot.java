package com.amr.irrigation.models;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timeslot_id_sequence")
    @SequenceGenerator(name = "timeslot_id_sequence", sequenceName = "timeslot_id_sequence", allocationSize = 1)
    private Integer id;
    private int duration;
    private int waterAmount;
    @Column(name = "plot_id")
    private Integer plotId;
    private boolean isAssigned;
    public TimeSlot() {
        this.isAssigned = false;
    }

    public TimeSlot(int duration, int waterAmount, Integer plotId) {
        this.duration = duration;
        this.waterAmount = waterAmount;
        this.plotId = plotId;
        this.isAssigned = (plotId != null);
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getWaterAmount() {
        return waterAmount;
    }

    public void setWaterAmount(int waterAmount) {
        this.waterAmount = waterAmount;
    }


    public Integer getPlotId() {
        return plotId;
    }

    public void setPlotId(Integer plotId) {
        this.plotId = plotId;
        this.isAssigned = (plotId != null);
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }
}
