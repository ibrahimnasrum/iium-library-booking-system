package model.services;

import model.Booking;
import model.Facility;
import model.User;
import model.enums.BookingStatus;
import model.enums.FacilityStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class BookingService {

    private static List<Booking> allBookings = new ArrayList<>();

    static {
        // Clean up expired bookings and reset facility statuses on startup
        cleanupExpiredBookings();
        System.out.println("BookingService initialized - cleaned up expired bookings");
    }

    /**
     * Clean up expired bookings and reset facility statuses
     */
    private static void cleanupExpiredBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> expiredBookings = new ArrayList<>();
        int activeBookingsBefore = getActiveBookingsCount();

        for (Booking booking : allBookings) {
            if (booking.getStatus() == BookingStatus.ACTIVE && booking.getEndTime().isBefore(now)) {
                // Booking has expired
                expiredBookings.add(booking);
                booking.setStatus(BookingStatus.COMPLETED);

                // Reset facility status to available
                Facility facility = FacilityService.findFacilityById(booking.getFacilityId());
                if (facility != null) {
                    facility.setStatus(FacilityStatus.AVAILABLE);
                    System.out.println("Reset facility " + facility.getId() + " to AVAILABLE (expired booking)");
                }
            }
        }

        // Also reset any facilities that are BOOKED but have no active bookings
        List<Facility> allFacilities = FacilityService.getAllFacilities();
        for (Facility facility : allFacilities) {
            if (facility.getStatus() == FacilityStatus.BOOKED) {
                List<Booking> activeBookings = getActiveBookingsForFacility(facility.getId());
                if (activeBookings.isEmpty()) {
                    facility.setStatus(FacilityStatus.AVAILABLE);
                    System.out.println("Reset facility " + facility.getId() + " to AVAILABLE (no active bookings)");
                }
            }
        }

        int activeBookingsAfter = getActiveBookingsCount();
        System.out.println("Cleanup complete - Active bookings: " + activeBookingsBefore + " -> " + activeBookingsAfter);
    }

    /**
     * Initialize and cleanup expired bookings (called after facilities are loaded)
     */
    public static void initializeAndCleanup() {
        cleanupExpiredBookings();
    }

    /**
     * Create a new booking
     */
    public static Booking createBooking(User user, Facility facility, LocalDateTime startTime, LocalDateTime endTime) {
        System.out.println("BookingService.createBooking called for facility " + facility.getId() +
                         " by user " + user.getMatricNo());

        // Validate booking policy
        if (!BookingPolicy.canBook(user, facility, startTime, endTime)) {
            System.out.println("BookingPolicy.canBook returned false");
            return null;
        }
        System.out.println("BookingPolicy.canBook passed");

        // Check for conflicts
        if (hasBookingConflict(facility.getId(), startTime, endTime)) {
            System.out.println("hasBookingConflict returned true");
            return null;
        }
        System.out.println("No booking conflicts detected");

        // Create booking
        Booking booking = new Booking(facility.getId(), user.getMatricNo(), startTime, endTime);
        allBookings.add(booking);
        System.out.println("Booking created: " + booking.getBookingID());

        // Update facility status
        facility.setStatus(model.enums.FacilityStatus.BOOKED);
        System.out.println("Facility status set to BOOKED");

        // Add to user's bookings
        user.getMyBookings().add(booking);
        System.out.println("Booking added to user's bookings");

        return booking;
    }

    /**
     * Cancel a booking
     */
    public static boolean cancelBooking(User user, Booking booking) {
        if (booking == null || booking.getStatus() != BookingStatus.ACTIVE) {
            return false;
        }

        // Check if user owns this booking
        if (!booking.getUserId().equals(user.getMatricNo())) {
            return false;
        }

        // Update booking status
        booking.setStatus(BookingStatus.CANCELLED);

        // Update facility status back to available
        Facility facility = FacilityService.findFacilityById(booking.getFacilityId());
        if (facility != null) {
            facility.setStatus(model.enums.FacilityStatus.AVAILABLE);
        }

        return true;
    }

    /**
     * Get all bookings
     */
    public static List<Booking> getAllBookings() {
        return new ArrayList<>(allBookings);
    }

    /**
     * Get bookings for a specific user
     */
    public static List<Booking> getBookingsForUser(String userId) {
        return allBookings.stream()
                .filter(b -> b.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    /**
     * Get bookings for a specific facility
     */
    public static List<Booking> getBookingsForFacility(String facilityId) {
        return allBookings.stream()
                .filter(b -> b.getFacilityId().equals(facilityId))
                .collect(Collectors.toList());
    }

    /**
     * Get active bookings
     */
    public static List<Booking> getActiveBookings() {
        return allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    /**
     * Check for booking conflicts
     */
    public static boolean hasBookingConflict(String facilityId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> conflictingBookings = allBookings.stream()
                .filter(b -> b.getFacilityId().equals(facilityId))
                .filter(b -> b.getStatus() == BookingStatus.ACTIVE)
                .filter(b -> timeSlotsOverlap(b.getStartTime(), b.getEndTime(), startTime, endTime))
                .collect(Collectors.toList());

        if (!conflictingBookings.isEmpty()) {
            System.out.println("Booking conflict detected for facility " + facilityId +
                             " at " + startTime + " - " + endTime +
                             ". Conflicting bookings: " + conflictingBookings.size());
            for (Booking b : conflictingBookings) {
                System.out.println("  - Booking: " + b.getBookingID() + " from " +
                                 b.getStartTime() + " to " + b.getEndTime());
            }
        }

        return !conflictingBookings.isEmpty();
    }

    /**
     * Check if two time slots overlap
     */
    private static boolean timeSlotsOverlap(LocalDateTime start1, LocalDateTime end1,
                                          LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    /**
     * Clear all bookings and reset all facilities to available (for testing)
     */
    public static void clearAllBookingsAndResetFacilities() {
        allBookings.clear();

        // Reset all facilities to available
        List<Facility> allFacilities = FacilityService.getAllFacilities();
        for (Facility facility : allFacilities) {
            if (facility.getStatus() == FacilityStatus.BOOKED) {
                facility.setStatus(FacilityStatus.AVAILABLE);
            }
        }
    }

    /**
     * Get active bookings count for debugging
     */
    public static int getActiveBookingsCount() {
        return (int) allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.ACTIVE)
                .count();
    }

    /**
     * Get active bookings for a facility
     */
    public static List<Booking> getActiveBookingsForFacility(String facilityId) {
        return allBookings.stream()
                .filter(b -> b.getFacilityId().equals(facilityId))
                .filter(b -> b.getStatus() == BookingStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    /**
     * Get bookings by status
     */
    public static List<Booking> getBookingsByStatus(BookingStatus status) {
        return allBookings.stream()
                .filter(b -> b.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Find booking by ID
     */
    public static Booking findBookingById(String bookingId) {
        return allBookings.stream()
                .filter(b -> b.getBookingID().equals(bookingId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Update booking status (Admin only)
     */
    public static boolean updateBookingStatus(String bookingId, BookingStatus status) {
        Booking booking = findBookingById(bookingId);
        if (booking != null) {
            booking.setStatus(status);
            return true;
        }
        return false;
    }

    /**
     * Get booking statistics
     */
    public static BookingStats getBookingStats() {
        long total = allBookings.size();
        long active = allBookings.stream().filter(b -> b.getStatus() == BookingStatus.ACTIVE).count();
        long cancelled = allBookings.stream().filter(b -> b.getStatus() == BookingStatus.CANCELLED).count();
        long completed = allBookings.stream().filter(b -> b.getStatus() == BookingStatus.COMPLETED).count();

        return new BookingStats(total, active, cancelled, completed);
    }

    /**
     * Booking statistics class
     */
    public static class BookingStats {
        public final long total;
        public final long active;
        public final long cancelled;
        public final long completed;

        public BookingStats(long total, long active, long cancelled, long completed) {
            this.total = total;
            this.active = active;
            this.cancelled = cancelled;
            this.completed = completed;
        }
    }
}