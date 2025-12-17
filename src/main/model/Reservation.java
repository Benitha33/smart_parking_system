package com.example.smartparking.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a reservation made by a user for a parking slot.
 * The model is intentionally simple to focus on OOP design and extensibility.
 */
public class Reservation {
    private final String id;
    private final User user;
    private final ParkingSlot slot;
    private final LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private ReservationStatus status;

    public Reservation(String id, User user, ParkingSlot slot) {
        this.id = id;
        this.user = user;
        this.slot = slot;
        this.createdAt = LocalDateTime.now();
        this.status = ReservationStatus.RESERVED;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public ParkingSlot getSlot() {
        return slot;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", slot=" + slot +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
