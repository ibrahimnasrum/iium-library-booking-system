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
    // Minimum booking duration in minutes
    private static final int MIN_BOOKING_MINUTES = 30;
    // Minimum advance booking time in minutes
    private static final int MIN_ADVANCE_MINUTES = 30;
    // Maximum advance booking time in days
    private static final int MAX_ADVANCE_DAYS = 14;
    // Maximum bookings per user per day
    private static final int MAX_BOOKINGS_PER_USER_PER_DAY = 3;

    /**
     * Check if a user can book a facility
     */
    public static boolean canBook(User user, Facility facility, LocalDateTime startTime, LocalDateTime endTime) {
        if (user == null || facility == null || startTime == null || endTime == null) {
            return false;
        }

        // Check if facility is available
        if (!facility.isAvailable()) {
            return false;
        }

        // Check privilege requirements
        if (!hasRequiredPrivilege(user, facility)) {
            return false;
        }

        // Check booking duration
        if (!isValidDuration(startTime, endTime)) {
            return false;
        }

        // Check minimum advance booking time (30 minutes)
        if (!hasMinimumAdvanceTime(startTime)) {
            return false;
        }

        // Check maximum advance booking time (14 days)
        if (!isWithinAdvanceLimit(startTime)) {
            return false;
        }

        // Check user hasn't exceeded daily booking limit
        if (!isWithinUserDailyLimit(user, startTime)) {
            return false;
        }

        // Check for overlapping bookings for this user
        if (hasUserBookingConflict(user, startTime, endTime)) {
            return false;
        }

        // Check booking time is in the future (allow same day if time is still ahead)
        if (startTime.isBefore(LocalDateTime.now())) {
            return false;
        }

        // Check business hours (assuming 8 AM to 10 PM)
        if (!isWithinBusinessHours(startTime, endTime)) {
            return false;
        }

        return true;
    }

    /**
     * Check if user has required privilege for the facility
     */
    public static boolean hasRequiredPrivilege(User user, Facility facility) {
        ReservationPrivilege required = facility.getPrivilege();

        switch (required) {
            case OPEN:
                return true; // Anyone can book

            case STUDENT_ONLY:
                return user.getRole() == Role.STUDENT;

            case STAFF_ONLY:
                return user.getRole() == Role.STAFF || user.getRole() == Role.ADMIN;

            case POSTGRADUATE_ONLY:
                return user.getRole() == Role.POSTGRADUATE;

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
    public static boolean isValidDuration(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            return false;
        }

        long hours = Duration.between(startTime, endTime).toHours();
        long minutes = Duration.between(startTime, endTime).toMinutes();

        return minutes >= MIN_BOOKING_MINUTES && hours <= MAX_BOOKING_HOURS;
    }

    /**
     * Check if booking has minimum advance time (30 minutes)
     */
    public static boolean hasMinimumAdvanceTime(LocalDateTime startTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minimumStartTime = now.plusMinutes(MIN_ADVANCE_MINUTES);

        return startTime.isAfter(minimumStartTime) || startTime.isEqual(minimumStartTime);
    }

    /**
     * Check if booking is within maximum advance limit (14 days)
     */
    public static boolean isWithinAdvanceLimit(LocalDateTime startTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime maxAdvanceTime = now.plusDays(MAX_ADVANCE_DAYS);

        return startTime.isBefore(maxAdvanceTime) || startTime.toLocalDate().equals(maxAdvanceTime.toLocalDate());
    }

    /**
     * Check if user is within daily booking limit
     */
    public static boolean isWithinUserDailyLimit(User user, LocalDateTime startTime) {
        if (user == null) return false;

        // Count user's bookings for the same day
        long bookingsToday = user.getMyBookings().stream()
            .filter(booking -> booking.getStatus() == model.enums.BookingStatus.ACTIVE)
            .filter(booking -> booking.getStartTime().toLocalDate().equals(startTime.toLocalDate()))
            .count();

        return bookingsToday < MAX_BOOKINGS_PER_USER_PER_DAY;
    }

    /**
     * Check if user has overlapping bookings
     */
    public static boolean hasUserBookingConflict(User user, LocalDateTime startTime, LocalDateTime endTime) {
        if (user == null) return false;

        return user.getMyBookings().stream()
            .filter(booking -> booking.getStatus() == model.enums.BookingStatus.ACTIVE)
            .anyMatch(booking ->
                // Check for time overlap
                (startTime.isBefore(booking.getEndTime()) && endTime.isAfter(booking.getStartTime()))
            );
    }

    /**
     * Check if a user can potentially book a facility (ignoring time constraints)
     * Used for UI filtering to show only bookable facilities
     */
    public static boolean canUserBookFacility(User user, Facility facility) {
        if (user == null || facility == null) {
            return false;
        }

        // Check if facility is available
        if (!facility.isAvailable()) {
            return false;
        }

        // Check privilege requirements
        return hasRequiredPrivilege(user, facility);
    }

    /**
     * Check if booking time is within business hours
     */
    public static boolean isWithinBusinessHours(LocalDateTime startTime, LocalDateTime endTime) {
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
     * Get minimum booking duration in minutes
     */
    public static int getMinBookingMinutes() {
        return MIN_BOOKING_MINUTES;
    }

    /**
     * Get minimum advance booking time in minutes
     */
    public static int getMinAdvanceMinutes() {
        return MIN_ADVANCE_MINUTES;
    }

    /**
     * Get maximum advance booking time in days
     */
    public static int getMaxAdvanceDays() {
        return MAX_ADVANCE_DAYS;
    }

    /**
     * Get maximum bookings per user per day
     */
    public static int getMaxBookingsPerUserPerDay() {
        return MAX_BOOKINGS_PER_USER_PER_DAY;
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