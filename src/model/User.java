package model;

import model.enums.Role;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String matricNo;
    private String password;
    private String name;
    private Role role;
    private List<Booking> myBookings;
    private static List<Room> allRooms = new ArrayList<>();

    public User(String matricNo, String password, String name, Role role) {
        this.matricNo = matricNo;
        this.password = password;
        this.name = name;
        this.role = role;
        this.myBookings = new ArrayList<>();
    }

    // Static initializer with complete IIUM library rooms
    static {
        // Student Lounge
        allRooms.add(new Room("SL-01", "IIUM Student Lounge", 50, "Level 2", "Available",
            "Projector", "Open", "Presentation Room"));
        
        // Carrel Rooms (Study Rooms) - Monthly loan
        allRooms.add(new Room("CR-L1-01", "Carrel Room (Monthly)", 1, "Level 1", "Available",
            "N/A", "Postgraduate Only", "Monthly loan, RM5/day late fine"));
        allRooms.add(new Room("CR-L2-01", "Carrel Room (Monthly)", 1, "Level 2", "Available",
            "N/A", "Postgraduate Only", "Monthly loan, RM5/day late fine"));
        allRooms.add(new Room("CR-L3-01", "Carrel Room (Monthly)", 1, "Level 3", "Available",
            "PC", "Postgraduate Only", "Monthly loan with PC, RM5/day late fine"));
        
        // Carrel Rooms - Three-hourly loan
        allRooms.add(new Room("CR-L1-02", "Carrel Room (3-Hour)", 1, "Level 1", "Available",
            "N/A", "Postgraduate Only", "3-hour loan, extendable if not reserved"));
        allRooms.add(new Room("CR-L2-02", "Carrel Room (3-Hour)", 1, "Level 2", "Available",
            "N/A", "Postgraduate Only", "3-hour loan, extendable if not reserved"));
        allRooms.add(new Room("CR-L3-02", "Carrel Room (3-Hour)", 1, "Level 3", "Available",
            "PC", "Postgraduate Only", "3-hour loan with PC, extendable if not reserved"));
        
        // Special Needs Carrel Room
        allRooms.add(new Room("CR-SN-01", "Carrel Room (Special Needs)", 1, "Level 1", "Available",
            "N/A", "Special Needs/Disabled", "Approach Circulation Counter for key"));
        
        // Research Room
        allRooms.add(new Room("RR-01", "Research Room", 1, "Level 2", "Available",
            "Desk & Chair", "Academic Staff Only", "Limited to research period, extendable"));
        
        // Discussion Rooms
        allRooms.add(new Room("DR-01", "Discussion Room (Small)", 6, "Level 2", "Available",
            "Whiteboard", "Min 3 users", "3-hour booking, extendable if not reserved"));
        allRooms.add(new Room("DR-02", "Discussion Room (Medium)", 8, "Level 2", "Available",
            "Whiteboard", "Min 3 users", "3-hour booking, extendable if not reserved"));
        allRooms.add(new Room("DR-03", "Discussion Room (Large)", 16, "Level 3", "Available",
            "Whiteboard, Projector", "Min 3 users", "3-hour booking, extendable if not reserved"));
        
        // Viewing Rooms (Temporarily Closed)
        allRooms.add(new Room("VR-A", "Viewing Room A", 29, "Level 3", "Closed",
            "Computer, Plasma TV, Whiteboard", "Staff Only", "Temporarily Closed"));
        allRooms.add(new Room("VR-B", "Viewing Room B", 6, "Level 3", "Closed",
            "Computer, Whiteboard", "Staff & Students", "Temporarily Closed - Discussion"));
        allRooms.add(new Room("VR-C", "Viewing Room C", 5, "Level 3", "Closed",
            "N/A", "Staff & Students", "Temporarily Closed - Discussion"));
        
        // Exhibition Areas
        allRooms.add(new Room("EX-01", "Exhibition Area (Large)", 0, "Level 2", "Available",
            "N/A", "Staff Only", "Size: 45×30 sq.ft"));
        allRooms.add(new Room("EX-02", "Exhibition Area (Small)", 0, "Level 2", "Available",
            "N/A", "Staff Only", "Size: 15×30 sq.ft"));
        allRooms.add(new Room("EX-03", "Exhibition Area (Entrance)", 0, "Level 2 (Main Entrance)", "Available",
            "N/A", "Book Vendors Only", "Size: 15×5 sq.ft"));
        
        // Computer Labs
        allRooms.add(new Room("CL-L2-01", "Computer Lab 1", 20, "Level 2", "Available",
            "Computer, Projector, Audio System, Whiteboard", "Library Use Only", "Reserved for library activities"));
        allRooms.add(new Room("CL-L2-02", "Computer Lab 2", 20, "Level 2", "Available",
            "Computer, Projector, Audio System, Whiteboard", "Library Use Only", "Reserved for library activities"));
        allRooms.add(new Room("CL-L3-01", "Computer Lab 3", 20, "Level 3", "Available",
            "Computer, Projector, Audio System, Whiteboard", "Library Use Only", "Reserved for library activities"));
        allRooms.add(new Room("CL-L3-02", "Computer Lab 4", 20, "Level 3", "Available",
            "Computer, Projector, Audio System, Whiteboard", "Library Use Only", "Reserved for library activities"));
        
        // Multi Purpose Room
        allRooms.add(new Room("MPR-01", "Multi Purpose Room", 40, "Level 2", "Available",
            "N/A", "Staff Only", "No equipment provided"));
        
        // MBSB AZKA-PPZ Corner
        allRooms.add(new Room("AZKA-01", "MBSB AZKA-PPZ Corner", 30, "Level 1", "Available",
            "Projector", "Open", "Available for all users"));
        
        // Library Auditorium
        allRooms.add(new Room("AUD-01", "Library Auditorium", 150, "Level 1", "Available",
            "Computer, Projector, Audio System", "Staff Only", "Main auditorium - advance booking required"));
    }

    // Login method (for reference) - now handled by AuthService
    @Deprecated
    public static User login(String matricNo, String password) {
        // Simple validation for demo
        if (matricNo != null && !matricNo.isEmpty() && password != null && !password.isEmpty()) {
            Role role = matricNo.startsWith("2") ? Role.STAFF :
                       matricNo.startsWith("1") ? Role.ADMIN : Role.STUDENT;
            return new User(matricNo, password, "User", role);
        }
        return null;
    }

    // Make a booking
    public boolean makeBooking(Room room, LocalDateTime startTime, LocalDateTime endTime) {
        if (room != null && room.getAvailabilityStatus().equals("Available")) {
            Booking booking = new Booking(room.getId(), this.matricNo, startTime, endTime);
            myBookings.add(booking);
            room.setAvailabilityStatus("Booked");
            return true;
        }
        return false;
    }

    // Cancel a booking
    public boolean cancelBooking(Booking booking) {
        if (booking != null && booking.getStatus() == model.enums.BookingStatus.ACTIVE) {
            booking.setStatus(model.enums.BookingStatus.CANCELLED);

            // Make the room available again
            for (Room room : allRooms) {
                if (room.getId().equals(booking.getFacilityId())) {
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Booking> getMyBookings() {
        return myBookings;
    }

    public static List<Room> getAllRooms() {
        return allRooms;
    }
}
