package model;

import model.enums.BookingStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
    private static int bookingCounter = 1;
    private String bookingID;
    private String facilityId;
    private String userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;

    public Booking(String facilityId, String userId, LocalDateTime startTime, LocalDateTime endTime) {
        this.bookingID = "B" + String.format("%03d", bookingCounter++);
        this.facilityId = facilityId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = BookingStatus.ACTIVE;
    }

    // Getters and Setters
    public String getBookingID() {
        return bookingID;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    // Legacy getters for backward compatibility
    public String getRoomID() {
        return getFacilityId();
    }

    public void setRoomID(String roomID) {
        setFacilityId(roomID);
    }

    public String getMatricNo() {
        return getUserId();
    }

    public void setMatricNo(String matricNo) {
        setUserId(matricNo);
    }

    public String getStatusString() {
        return status.toString();
    }

    public void setStatus(String status) {
        try {
            this.status = BookingStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            this.status = BookingStatus.ACTIVE; // Default fallback
        }
    }

    public String getFormattedStartTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return startTime.format(formatter);
    }

    public String getFormattedEndTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return endTime.format(formatter);
    }

    @Override
    public String toString() {
        return String.format("Booking %s | Facility: %s | %s to %s | Status: %s",
            bookingID, facilityId, getFormattedStartTime(), getFormattedEndTime(), status);
    }
}
