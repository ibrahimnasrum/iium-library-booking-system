package model;

import model.enums.FacilityStatus;
import model.enums.FacilityType;
import model.enums.ReservationPrivilege;
import java.util.List;
import java.util.ArrayList;

public abstract class Facility {
    protected String id;
    protected String name;
    protected FacilityType type;
    protected String location;
    protected int capacity;
    protected ReservationPrivilege privilege;
    protected FacilityStatus status;
    protected String imagePath;
    protected List<Equipment> equipment;
    protected String notes;

    public Facility(String id, String name, FacilityType type, String location,
                   int capacity, ReservationPrivilege privilege, FacilityStatus status,
                   String imagePath, String notes) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.capacity = capacity;
        this.privilege = privilege;
        this.status = status;
        this.imagePath = imagePath;
        this.equipment = new ArrayList<>();
        this.notes = notes;
    }

    // Abstract method for facility-specific details
    public abstract String getDetailedInfo();

    // Equipment management
    public void addEquipment(Equipment equipment) {
        this.equipment.add(equipment);
    }

    public void removeEquipment(Equipment equipment) {
        this.equipment.remove(equipment);
    }

    public List<Equipment> getEquipment() {
        return new ArrayList<>(equipment);
    }

    // Status management
    public boolean isAvailable() {
        return status == FacilityStatus.AVAILABLE;
    }

    public boolean isBookable() {
        return status == FacilityStatus.AVAILABLE || status == FacilityStatus.BOOKED;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FacilityType getType() {
        return type;
    }

    public void setType(FacilityType type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public ReservationPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(ReservationPrivilege privilege) {
        this.privilege = privilege;
    }

    public FacilityStatus getStatus() {
        return status;
    }

    public void setStatus(FacilityStatus status) {
        this.status = status;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}