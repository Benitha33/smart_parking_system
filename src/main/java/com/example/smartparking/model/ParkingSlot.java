package com.example.smartparking.model;

import java.util.Objects;

/**
 * Represents a single parking slot. Fields keep the data simple so it can be
 * easily extended to include size, level, or pricing later.
 */
public class ParkingSlot {
    private final int id;
    private final String location; // e.g., "A1"
    private volatile boolean occupied;
    private volatile boolean reserved;

    public ParkingSlot(int id, String location) {
        this.id = id;
        this.location = location;
        this.occupied = false;
        this.reserved = false;
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    @Override
    public String toString() {
        return "ParkingSlot{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", occupied=" + occupied +
                ", reserved=" + reserved +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSlot that = (ParkingSlot) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
