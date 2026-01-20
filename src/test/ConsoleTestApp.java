package test;

import model.*;
import model.services.*;
import java.util.List;
import java.util.Scanner;

public class ConsoleTestApp {

    public static void main(String[] args) {
        System.out.println("=== IIUM Library Booking System - Console Test ===\n");

        // Test authentication
        testAuthentication();

        // Test facility management
        testFacilityManagement();

        // Test booking system
        testBookingSystem();

        System.out.println("\n=== All Tests Completed ===");
    }

    private static void testAuthentication() {
        System.out.println("Testing Authentication Service...");

        // Test student login
        User student = AuthService.login("123456", "password");
        if (student != null) {
            System.out.println("✓ Student login successful: " + student.getName() + " (" + student.getRole() + ")");
        } else {
            System.out.println("✗ Student login failed");
        }

        // Test admin login
        User admin = AuthService.login("admin", "admin123");
        if (admin != null) {
            System.out.println("✓ Admin login successful: " + admin.getName() + " (" + admin.getRole() + ")");
        } else {
            System.out.println("✗ Admin login failed");
        }

        System.out.println();
    }

    private static void testFacilityManagement() {
        System.out.println("Testing Facility Management...");

        // Login as admin
        User admin = AuthService.login("admin", "admin123");
        if (admin == null) {
            System.out.println("✗ Cannot test facility management - admin login failed");
            return;
        }

        // Get all facilities
        List<Facility> facilities = FacilityService.getAllFacilities();
        System.out.println("✓ Found " + facilities.size() + " facilities");

        // Test creating a new facility
        Room newRoom = new Room("TEST001", "Test Study Room", "A test room for validation",
                               4, FacilityType.STUDY_ROOM, FacilityStatus.AVAILABLE,
                               "resources/images/test_room.jpg");
        newRoom.addEquipment("Whiteboard");
        newRoom.addEquipment("Projector");

        boolean created = FacilityService.createFacility(newRoom);
        if (created) {
            System.out.println("✓ Created new facility: " + newRoom.getName());
        } else {
            System.out.println("✗ Failed to create facility");
        }

        // Test updating facility
        newRoom.setDescription("Updated test room description");
        boolean updated = FacilityService.updateFacility(newRoom);
        if (updated) {
            System.out.println("✓ Updated facility description");
        } else {
            System.out.println("✗ Failed to update facility");
        }

        // Test facility search
        List<Facility> searchResults = FacilityService.searchFacilities("Study");
        System.out.println("✓ Search for 'Study' returned " + searchResults.size() + " results");

        System.out.println();
    }

    private static void testBookingSystem() {
        System.out.println("Testing Booking System...");

        // Login as student
        User student = AuthService.login("123456", "password");
        if (student == null) {
            System.out.println("✗ Cannot test booking - student login failed");
            return;
        }

        // Get available facilities
        List<Facility> availableFacilities = FacilityService.getFacilitiesByStatus(FacilityStatus.AVAILABLE);
        if (availableFacilities.isEmpty()) {
            System.out.println("✗ No available facilities for booking test");
            return;
        }

        Facility testFacility = availableFacilities.get(0);
        System.out.println("✓ Testing booking for facility: " + testFacility.getName());

        // Test booking creation
        Booking newBooking = new Booking(student.getMatricNo(), testFacility.getId(),
                                       java.time.LocalDateTime.now().plusHours(1),
                                       java.time.LocalDateTime.now().plusHours(2),
                                       "Test booking", BookingStatus.PENDING);

        boolean bookingCreated = BookingService.createBooking(newBooking);
        if (bookingCreated) {
            System.out.println("✓ Booking created successfully");
        } else {
            System.out.println("✗ Booking creation failed");
        }

        // Test booking retrieval
        List<Booking> userBookings = BookingService.getBookingsByUser(student.getMatricNo());
        System.out.println("✓ User has " + userBookings.size() + " bookings");

        // Test booking policy validation
        boolean canBook = BookingPolicy.canUserBookFacility(student, testFacility);
        System.out.println("✓ User can book facility: " + canBook);

        // Test booking cancellation (if booking was created)
        if (!userBookings.isEmpty()) {
            Booking lastBooking = userBookings.get(userBookings.size() - 1);
            boolean cancelled = BookingService.cancelBooking(lastBooking.getId());
            if (cancelled) {
                System.out.println("✓ Booking cancelled successfully");
            } else {
                System.out.println("✗ Booking cancellation failed");
            }
        }

        System.out.println();
    }
}