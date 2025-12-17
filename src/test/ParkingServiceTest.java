package com.example.smartparking.service;

import com.example.smartparking.model.ParkingSlot;
import com.example.smartparking.model.Reservation;
import com.example.smartparking.model.ReservationStatus;
import com.example.smartparking.model.User;

import java.util.List;

/**
 * Simple test runner using plain Java assertions (no JUnit dependency).
 * Run via: java -cp out/test-classes;out/classes com.example.smartparking.service.ParkingServiceTest
 */
public class ParkingServiceTest {
    private ParkingService service;
    private int testsPassed = 0;
    private int testsFailed = 0;

    public static void main(String[] args) {
        ParkingServiceTest test = new ParkingServiceTest();
        test.runAllTests();
        System.out.println("\n=== Test Results ===");
        System.out.println("Passed: " + test.testsPassed);
        System.out.println("Failed: " + test.testsFailed);
        if (test.testsFailed > 0) {
            System.exit(1);
        }
    }

    private void runAllTests() {
        System.out.println("Running ParkingServiceTest...\n");
        testListAvailableSlots_initialNotEmpty();
        testReserveAndOccupyAndReleaseFlow();
        testReserveUnavailableSlotThrows();
    }

    private void setUp() {
        service = new ParkingService();
    }

    private void testListAvailableSlots_initialNotEmpty() {
        setUp();
        try {
            List<ParkingSlot> available = service.listAvailableSlots();
            assertTrue(!available.isEmpty(), "Initial available slots should not be empty");
            System.out.println("[PASS] testListAvailableSlots_initialNotEmpty");
            testsPassed++;
        } catch (AssertionError e) {
            System.out.println("[FAIL] testListAvailableSlots_initialNotEmpty: " + e.getMessage());
            testsFailed++;
        }
    }

    private void testReserveAndOccupyAndReleaseFlow() {
        setUp();
        try {
            User user = new User("u-test", "Test User", "t@example.com");
            List<ParkingSlot> available = service.listAvailableSlots();
            ParkingSlot slot = available.get(0);

            // Reserve
            Reservation r = service.reserveSlot(user, slot.getId());
            assertTrue(r != null, "Reservation should not be null");
            assertEquals(ReservationStatus.RESERVED, r.getStatus(), "Reservation status should be RESERVED");

            // Occupy
            Reservation active = service.occupySlot(r.getId());
            assertEquals(ReservationStatus.ACTIVE, active.getStatus(), "Reservation status should be ACTIVE");
            assertTrue(slot.isOccupied(), "Slot should be occupied");

            // Release
            Reservation completed = service.releaseSlot(r.getId());
            assertEquals(ReservationStatus.COMPLETED, completed.getStatus(), "Reservation status should be COMPLETED");
            assertTrue(!slot.isOccupied(), "Slot should not be occupied after release");
            System.out.println("[PASS] testReserveAndOccupyAndReleaseFlow");
            testsPassed++;
        } catch (AssertionError e) {
            System.out.println("[FAIL] testReserveAndOccupyAndReleaseFlow: " + e.getMessage());
            testsFailed++;
        }
    }

    private void testReserveUnavailableSlotThrows() {
        setUp();
        try {
            User user = new User("u2", "Bob", "b@example.com");
            List<ParkingSlot> available = service.listAvailableSlots();
            ParkingSlot slot = available.get(0);
            service.reserveSlot(user, slot.getId());

            // Second reservation attempt should fail
            boolean exceptionThrown = false;
            try {
                service.reserveSlot(new User("u3", "Carol", "c@example.com"), slot.getId());
            } catch (IllegalStateException e) {
                exceptionThrown = true;
            }
            assertTrue(exceptionThrown, "Should throw IllegalStateException when reserving unavailable slot");
            System.out.println("[PASS] testReserveUnavailableSlotThrows");
            testsPassed++;
        } catch (AssertionError e) {
            System.out.println("[FAIL] testReserveUnavailableSlotThrows: " + e.getMessage());
            testsFailed++;
        }
    }

    // Simple assertion helpers
    private void assertTrue(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " (expected: " + expected + ", got: " + actual + ")");
        }
    }
}

