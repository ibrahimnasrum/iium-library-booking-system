package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String matricNo;
    private String password;
    private String name;
    private List<Booking> myBookings;
    private static List<Room> allRooms = new ArrayList<>();

    // Static initializer to create sample rooms
    static {
        allRooms.add(new Room("R001", "Study Room", 4, "Level 1", "Available"));
        allRooms.add(new Room("R002", "Discussion Room", 6, "Level 2", "Available"));
        allRooms.add(new Room("R003", "Meeting Room", 8, "Level 3", "Available"));
        allRooms.add(new Room("R004", "Private Room", 2, "Level 1", "Available"));
        allRooms.add(new Room("R005", "Conference Room", 12, "Level 4", "Available"));
    }

    public User(String matricNo, String password, String name) {
        this.matricNo = matricNo;
        this.password = password;
        this.name = name;
        this.myBookings = new ArrayList<>();
    }

    // Login method (for reference)
    public static User login(String matricNo, String password) {
        // Simple validation for demo
        if (matricNo != null && !matricNo.isEmpty() && password != null && !password.isEmpty()) {
            return new User(matricNo, password, "Student");
        }
        return null;
    }

    // Make a booking
    public boolean makeBooking(Room room, LocalDateTime startTime, LocalDateTime endTime) {
        if (room != null && room.getAvailabilityStatus().equals("Available")) {
            Booking booking = new Booking(room.getRoomID(), this.matricNo, startTime, endTime);
            myBookings.add(booking);
            room.setAvailabilityStatus("Booked");
            return true;
        }
        return false;
    }

    // Cancel a booking
    public boolean cancelBooking(Booking booking) {
        if (booking != null && booking.getStatus().equals("Active")) {
            booking.setStatus("Cancelled");
            
            // Make the room available again
            for (Room room : allRooms) {
                if (room.getRoomID().equals(booking.getRoomID())) {
                    room.setAvailabilityStatus("Available");
                    break;
                }
            }
            return true;
        }
        return false;
    }

    // Getters
    public String getMatricNo() {
        return matricNo;
    }

    public String getName() {
        return name;
    }

    public List<Booking> getMyBookings() {
        return myBookings;
    }

    public static List<Room> getAllRooms() {
        return allRooms;
    }
}
