package com.example.smartparking.controller;

import com.example.smartparking.model.ParkingSlot;
import com.example.smartparking.model.Reservation;
import com.example.smartparking.model.User;
import com.example.smartparking.service.ParkingService;

import java.util.List;

/**
 * Simple console controller demonstrating core flows required for the exam:
 * - Search available slots
 * - Reserve a slot
 * - Occupy and release a slot
 *
 * The class is intentionally small and focused so it can be explained easily.
 */
public class Main {
    public static void main(String[] args) {
        ParkingService service = new ParkingService();

        // 1) List available slots
        System.out.println("Available slots (initial):");
        printSlots(service.listAvailableSlots());

        // 2) Driver searches and reserves a slot
        User driver = new User("u1", "Alice", "alice@example.com");
        List<ParkingSlot> available = service.listAvailableSlots();
        if (available.isEmpty()) {
            System.out.println("No slots available");
            return;
        }
        ParkingSlot chosen = available.get(0);
        System.out.println("Driver " + driver.getName() + " reserves slot " + chosen.getLocation());
        Reservation reservation = service.reserveSlot(driver, chosen.getId());
        System.out.println("Reservation created: " + reservation.getId());

        System.out.println("Available slots (after reserve):");
        printSlots(service.listAvailableSlots());

        // 3) Driver enters and occupies the slot
        System.out.println("Driver entering, occupying slot for reservation " + reservation.getId());
        service.occupySlot(reservation.getId());
        System.out.println("Slot occupied. Current reservations:");
        service.listReservations().forEach(r -> System.out.println(r));

        // 4) Driver exits and releases the slot
        System.out.println("Driver exiting, releasing slot for reservation " + reservation.getId());
        service.releaseSlot(reservation.getId());
        System.out.println("Slot released. Reservation complete:");
        service.findReservationById(reservation.getId()).ifPresent(System.out::println);

        System.out.println("Available slots (final):");
        printSlots(service.listAvailableSlots());
    }

    private static void printSlots(List<ParkingSlot> slots) {
        if (slots.isEmpty()) {
            System.out.println("  (none)");
            return;
        }
        slots.forEach(s -> System.out.println("  " + s.getId() + " - " + s.getLocation()));
    }
}
