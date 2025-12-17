package com.example.smartparking.service;

import com.example.smartparking.model.ParkingSlot;
import com.example.smartparking.model.Reservation;
import com.example.smartparking.model.ReservationStatus;
import com.example.smartparking.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service layer that handles parking operations. Uses in-memory collections
 * and simple synchronization. The design keeps persistence and UI separate
 * so the project can be extended to a DB or web app later.
 */
public class ParkingService {
    private final Map<Integer, ParkingSlot> slots = new ConcurrentHashMap<>();
    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();

    public ParkingService() {
        // Seed with sample slots (A1..A5, B1..B5)
        int id = 1;
        for (char r : new char[]{'A', 'B'}) {
            for (int i = 1; i <= 5; i++) {
                slots.put(id, new ParkingSlot(id, String.format("%s%d", r, i)));
                id++;
            }
        }
    }

    public List<ParkingSlot> listAllSlots() {
        return new ArrayList<>(slots.values());
    }

    public List<ParkingSlot> listAvailableSlots() {
        return slots.values().stream()
                .filter(s -> !s.isOccupied() && !s.isReserved())
                .sorted(Comparator.comparingInt(ParkingSlot::getId))
                .collect(Collectors.toList());
    }

    /**
     * Reserve a specific slot for a user. Returns the created Reservation.
     */
    public synchronized Reservation reserveSlot(User user, int slotId) {
        ParkingSlot slot = slots.get(slotId);
        if (slot == null) {
            throw new IllegalArgumentException("Slot not found: " + slotId);
        }
        if (slot.isOccupied() || slot.isReserved()) {
            throw new IllegalStateException("Slot not available: " + slotId);
        }
        slot.setReserved(true);
        String reservationId = UUID.randomUUID().toString();
        Reservation r = new Reservation(reservationId, user, slot);
        r.setStatus(ReservationStatus.RESERVED);
        reservations.put(reservationId, r);
        return r;
    }

    /**
     * Mark a reserved slot as occupied (user enters). Returns updated reservation.
     */
    public synchronized Reservation occupySlot(String reservationId) {
        Reservation r = reservations.get(reservationId);
        if (r == null) {
            throw new IllegalArgumentException("Reservation not found: " + reservationId);
        }
        if (r.getStatus() != ReservationStatus.RESERVED) {
            throw new IllegalStateException("Reservation not in RESERVED state: " + r.getStatus());
        }
        ParkingSlot slot = r.getSlot();
        slot.setReserved(false);
        slot.setOccupied(true);
        r.setStatus(ReservationStatus.ACTIVE);
        r.setStartedAt(LocalDateTime.now());
        return r;
    }

    /**
     * Release an occupied slot and complete the reservation.
     */
    public synchronized Reservation releaseSlot(String reservationId) {
        Reservation r = reservations.get(reservationId);
        if (r == null) {
            throw new IllegalArgumentException("Reservation not found: " + reservationId);
        }
        if (r.getStatus() != ReservationStatus.ACTIVE) {
            throw new IllegalStateException("Reservation not active: " + r.getStatus());
        }
        ParkingSlot slot = r.getSlot();
        slot.setOccupied(false);
        r.setEndedAt(LocalDateTime.now());
        r.setStatus(ReservationStatus.COMPLETED);
        return r;
    }

    public Optional<Reservation> findReservationById(String id) {
        return Optional.ofNullable(reservations.get(id));
    }

    public List<Reservation> listReservations() {
        return new ArrayList<>(reservations.values());
    }

    // Admin utilities
    public synchronized void addSlot(int id, String location) {
        if (slots.containsKey(id)) throw new IllegalArgumentException("Slot id exists: " + id);
        slots.put(id, new ParkingSlot(id, location));
    }

    public synchronized void removeSlot(int id) {
        ParkingSlot s = slots.get(id);
        if (s == null) throw new IllegalArgumentException("Slot not found: " + id);
        if (s.isOccupied() || s.isReserved()) throw new IllegalStateException("Cannot remove occupied/reserved slot");
        slots.remove(id);
    }
}
