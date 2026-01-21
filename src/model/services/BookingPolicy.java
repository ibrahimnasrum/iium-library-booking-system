package model.services;

import model.Booking;
import model.Facility;
import model.Room;
import model.User;
import model.enums.ReservationPrivilege;
import model.enums.Role;
import java.time.LocalDateTime;
import java.time.Duration;

public class BookingPolicy {

    // Maximum booking duration in hours
    private static final int MAX_BOOKING_HOURS = 3;

    /**
     * Check if a user can book a facility
     */
    public static boolean canBook(User user, Facility facility, LocalDateTime startTime, LocalDateTime endTime) {
        System.out.println("BookingPolicy.canBook called for user " + user.getMatricNo() + " (" + user.getRole() + ") " +
                         "facility " + facility.getId() + " at " + startTime + " - " + endTime);

        if (user == null || facility == null || startTime == null || endTime == null) {
            System.out.println("BookingPolicy: Null parameter check failed");
            return false;
        }

        // Check if facility is available
        if (!facility.isAvailable()) {
            System.out.println("BookingPolicy: Facility not available - status: " + facility.getStatus());
            return false;
        }
        System.out.println("BookingPolicy: ✓ Facility is available");

        // Check privilege requirements
        if (!hasRequiredPrivilege(user, facility)) {
            System.out.println("BookingPolicy: Privilege check failed - user: " + user.getRole() +
                             ", required: " + facility.getPrivilege());
            return false;
        }
        System.out.println("BookingPolicy: ✓ Privilege check passed");

        // Check booking duration
        if (!isValidDuration(startTime, endTime)) {
            System.out.println("BookingPolicy: Duration check failed");
            return false;
        }
        System.out.println("BookingPolicy: ✓ Duration check passed");

        // Check booking time is at least 30 minutes in the future
        LocalDateTime minimumBookingTime = LocalDateTime.now().plusMinutes(30);
        if (startTime.isBefore(minimumBookingTime)) {
            System.out.println("BookingPolicy: Time check failed - start time " + startTime +
                             " is before minimum " + minimumBookingTime);
            return false;
        }
        System.out.println("BookingPolicy: ✓ Time check passed");

        // Check business hours (assuming 8 AM to 10 PM)
        if (!isWithinBusinessHours(startTime, endTime)) {
            System.out.println("BookingPolicy: Business hours check failed");
            return false;
        }
        System.out.println("BookingPolicy: ✓ Business hours check passed");

        System.out.println("BookingPolicy: All checks passed");
        return true;
    }

    /**
     * Check if user has required privilege for the facility
     */
    private static boolean hasRequiredPrivilege(User user, Facility facility) {
        ReservationPrivilege required = facility.getPrivilege();

        switch (required) {
            case OPEN:
                return true; // Anyone can book

            case STUDENT_ONLY:
                return user.getRole() == Role.STUDENT;

            case STAFF_ONLY:
                return user.getRole() == Role.STAFF || user.getRole() == Role.ADMIN;

            case POSTGRADUATE_ONLY:
                // Postgraduate students have matric numbers starting with "3" (IIUM system)
                return user.getMatricNo().startsWith("3");

            case SPECIAL_NEEDS_ONLY:
                // Would need additional user profile information
                return false; // Not implemented for demo

            case BOOK_VENDORS_ONLY:
                // Would need vendor verification
                return false; // Not implemented for demo

            case LIBRARY_USE_ONLY:
                return user.getRole() == Role.STAFF || user.getRole() == Role.ADMIN;

            default:
                return false;
        }
    }

    /**
     * Check if booking duration is valid
     */
    private static boolean isValidDuration(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            return false;
        }

        long hours = Duration.between(startTime, endTime).toHours();
        return hours > 0 && hours <= MAX_BOOKING_HOURS;
    }

    /**
     * Check if booking time is within business hours
     */
    private static boolean isWithinBusinessHours(LocalDateTime startTime, LocalDateTime endTime) {
        int startHour = startTime.getHour();
        int endHour = endTime.getHour();

        // Business hours: 8 AM to 10 PM (22:00)
        final int OPEN_HOUR = 8;
        final int CLOSE_HOUR = 22;

        return startHour >= OPEN_HOUR && startHour <= CLOSE_HOUR &&
               endHour >= OPEN_HOUR && endHour <= CLOSE_HOUR;
    }

    /**
     * Get maximum booking duration in hours
     */
    public static int getMaxBookingHours() {
        return MAX_BOOKING_HOURS;
    }

    /**
     * Get business hours as string
     */
    public static String getBusinessHours() {
        return "8:00 AM - 10:00 PM";
    }

    /**
     * Validate minimum users for discussion rooms
     */
    public static boolean meetsMinimumUserRequirement(Facility facility, int numberOfUsers) {
        if (!(facility instanceof Room)) {
            return true; // Not a room, no minimum requirement
        }

        Room room = (Room) facility;

        // Discussion rooms require minimum 3 users
        if (room.getType() == model.enums.FacilityType.DISCUSSION_ROOM) {
            return numberOfUsers >= 3;
        }

        return true;
    }

    /**
     * Check if booking can be extended
     */
    public static boolean canExtendBooking(Booking existingBooking, LocalDateTime newEndTime) {
        if (existingBooking == null || newEndTime == null) {
            return false;
        }

        // Check if new end time exceeds maximum duration from start
        long totalHours = Duration.between(existingBooking.getStartTime(), newEndTime).toHours();
        if (totalHours > MAX_BOOKING_HOURS) {
            return false;
        }

        // Check if extension is within business hours
        return isWithinBusinessHours(existingBooking.getStartTime(), newEndTime);
    }

    /**
     * Get cancellation policy
     */
    public static String getCancellationPolicy() {
        return "Bookings can be cancelled up to 1 hour before start time. " +
               "Late cancellations may incur penalties.";
    }

    /**
     * Check if booking can be cancelled
     */
    public static boolean canCancelBooking(Booking booking) {
        if (booking == null || booking.getStatus() != model.enums.BookingStatus.ACTIVE) {
            return false;
        }

        // Can cancel if more than 1 hour before start time
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourBefore = booking.getStartTime().minusHours(1);

        return now.isBefore(oneHourBefore);
    }
}