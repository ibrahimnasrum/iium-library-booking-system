package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
    private static int bookingCounter = 1;
    private String bookingID;
    private String roomID;
    private String matricNo;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status; // "Active" or "Cancelled"

    public Booking(String roomID, String matricNo, LocalDateTime startTime, LocalDateTime endTime) {
        this.bookingID = "B" + String.format("%03d", bookingCounter++);
        this.roomID = roomID;
        this.matricNo = matricNo;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = "Active";
    }

    // Getters and Setters
    public String getBookingID() {
        return bookingID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getMatricNo() {
        return matricNo;
    }

    public void setMatricNo(String matricNo) {
        this.matricNo = matricNo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        return String.format("Booking %s | Room: %s | %s to %s | Status: %s", 
            bookingID, roomID, getFormattedStartTime(), getFormattedEndTime(), status);
    }
}
