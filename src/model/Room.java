package model;

public class Room {
    private String roomID;
    private String type;
    private int capacity;
    private String location;
    private String availabilityStatus; // "Available" or "Booked"

    public Room(String roomID, String type, int capacity, String location, String availabilityStatus) {
        this.roomID = roomID;
        this.type = type;
        this.capacity = capacity;
        this.location = location;
        this.availabilityStatus = availabilityStatus;
    }

    // Getters and Setters
    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    @Override
    public String toString() {
        return roomID + " - " + type + " (Capacity: " + capacity + ", Location: " + location + ")";
    }
}
