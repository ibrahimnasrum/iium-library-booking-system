package model.services;

import model.Booking;
import model.Facility;
import model.User;
import model.enums.BookingStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class BookingService {

    private static List<Booking> allBookings = new ArrayList<>();

    /**
     * Create a new booking
     */
    public static Booking createBooking(User user, Facility facility, LocalDateTime startTime, LocalDateTime endTime) {
        // Validate booking policy
        if (!BookingPolicy.canBook(user, facility, startTime, endTime)) {
            return null;
        }

        // Check for conflicts
        if (hasBookingConflict(facility.getId(), startTime, endTime)) {
            return null;
        }

        // Create booking
        Booking booking = new Booking(facility.getId(), user.getMatricNo(), startTime, endTime);
        allBookings.add(booking);

        // Update facility status
        facility.setStatus(model.enums.FacilityStatus.BOOKED);

        // Add to user's bookings
        user.getMyBookings().add(booking);

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
        return allBookings.stream()
                .filter(b -> b.getFacilityId().equals(facilityId))
                .filter(b -> b.getStatus() == BookingStatus.ACTIVE)
                .anyMatch(b -> timeSlotsOverlap(b.getStartTime(), b.getEndTime(), startTime, endTime));
    }

    /**
     * Check if two time slots overlap
     */
    private static boolean timeSlotsOverlap(LocalDateTime start1, LocalDateTime end1,
                                          LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    /**
     * Get bookings by date range
     */
    public static List<Booking> getBookingsByDateRange(LocalDateTime start, LocalDateTime end) {
        return allBookings.stream()
                .filter(b -> !b.getStartTime().isBefore(start) && !b.getStartTime().isAfter(end))
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