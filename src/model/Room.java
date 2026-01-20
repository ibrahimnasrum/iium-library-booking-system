package model;

import model.enums.FacilityType;
import model.enums.FacilityStatus;
import model.enums.ReservationPrivilege;

public class Room extends Facility {

    // Legacy constructor for backward compatibility
    public Room(String roomID, String type, int capacity, String location, String availabilityStatus) {
        super(roomID, type, mapFacilityType(type), location, capacity,
              ReservationPrivilege.OPEN, mapFacilityStatus(availabilityStatus),
              getDefaultImagePath(roomID), "");
    }

    // Legacy constructor with equipment and eligibility
    public Room(String roomID, String type, int capacity, String location, String availabilityStatus,
                String equipment, String eligibility, String notes) {
        super(roomID, type, mapFacilityType(type), location, capacity,
              mapReservationPrivilege(eligibility), mapFacilityStatus(availabilityStatus),
              getDefaultImagePath(roomID), notes);
        // Add equipment if provided
        if (equipment != null && !equipment.equals("N/A")) {
            parseAndAddEquipment(equipment);
        }
    }

    // New constructor using enums
    public Room(String id, String name, FacilityType type, String location, int capacity,
                ReservationPrivilege privilege, FacilityStatus status, String imagePath, String notes) {
        super(id, name, type, location, capacity, privilege, status, imagePath, notes);
    }

    // Helper method to map string type to FacilityType enum
    private static FacilityType mapFacilityType(String type) {
        if (type == null) return FacilityType.ROOM;

        String lowerType = type.toLowerCase();
        if (lowerType.contains("discussion")) return FacilityType.DISCUSSION_ROOM;
        if (lowerType.contains("carrel")) return FacilityType.CARREL_ROOM;
        if (lowerType.contains("research")) return FacilityType.RESEARCH_ROOM;
        if (lowerType.contains("computer")) return FacilityType.COMPUTER_LAB;
        if (lowerType.contains("auditorium")) return FacilityType.AUDITORIUM;
        if (lowerType.contains("viewing")) return FacilityType.VIEWING_ROOM;
        if (lowerType.contains("exhibition")) return FacilityType.EXHIBITION_AREA;
        if (lowerType.contains("multi purpose")) return FacilityType.MULTI_PURPOSE_ROOM;
        if (lowerType.contains("special needs")) return FacilityType.SPECIAL_NEEDS_ROOM;
        if (lowerType.contains("study")) return FacilityType.STUDY_AREA;

        return FacilityType.ROOM; // Default
    }

    // Helper method to map string status to FacilityStatus enum
    private static FacilityStatus mapFacilityStatus(String status) {
        if (status == null) return FacilityStatus.AVAILABLE;

        switch (status.toLowerCase()) {
            case "available": return FacilityStatus.AVAILABLE;
            case "booked": return FacilityStatus.BOOKED;
            case "maintenance": return FacilityStatus.MAINTENANCE;
            case "temporarily closed": return FacilityStatus.TEMPORARILY_CLOSED;
            case "reserved": return FacilityStatus.RESERVED;
            case "closed": return FacilityStatus.TEMPORARILY_CLOSED;
            default: return FacilityStatus.AVAILABLE;
        }
    }

    // Helper method to map string eligibility to ReservationPrivilege enum
    private static ReservationPrivilege mapReservationPrivilege(String eligibility) {
        if (eligibility == null) return ReservationPrivilege.OPEN;

        String lowerEligibility = eligibility.toLowerCase();
        if (lowerEligibility.contains("staff only")) return ReservationPrivilege.STAFF_ONLY;
        if (lowerEligibility.contains("student only")) return ReservationPrivilege.STUDENT_ONLY;
        if (lowerEligibility.contains("postgraduate")) return ReservationPrivilege.POSTGRADUATE_ONLY;
        if (lowerEligibility.contains("special needs")) return ReservationPrivilege.SPECIAL_NEEDS_ONLY;
        if (lowerEligibility.contains("book vendors")) return ReservationPrivilege.BOOK_VENDORS_ONLY;
        if (lowerEligibility.contains("library use")) return ReservationPrivilege.LIBRARY_USE_ONLY;
        if (lowerEligibility.contains("open")) return ReservationPrivilege.OPEN;

        return ReservationPrivilege.OPEN; // Default
    }

    // Helper method to get default image path based on room ID
    private static String getDefaultImagePath(String roomID) {
        if (roomID == null || roomID.isEmpty()) return "";

        // Check if image file exists for this room ID
        String imagePath = "resources/images/" + roomID + ".jpg";
        // For now, return the path - the application will handle missing images gracefully
        return imagePath;
    }

    // Helper method to parse equipment string and add equipment items
    private void parseAndAddEquipment(String equipmentStr) {
        if (equipmentStr == null || equipmentStr.equals("N/A")) return;

        // Simple parsing - can be enhanced for more complex equipment strings
        String[] items = equipmentStr.split(", ");
        for (String item : items) {
            item = item.trim();
            if (item.toLowerCase().contains("projector")) {
                addEquipment(new Equipment("Projector", 1));
            } else if (item.toLowerCase().contains("whiteboard")) {
                addEquipment(new Equipment("Whiteboard", 1));
            } else if (item.toLowerCase().contains("computer")) {
                addEquipment(new Equipment("Computer", 1));
            } else if (item.toLowerCase().contains("plasma tv")) {
                addEquipment(new Equipment("Plasma TV", 1));
            } else if (item.toLowerCase().contains("audio system")) {
                addEquipment(new Equipment("Audio System", 1));
            } else {
                // Add as generic equipment
                addEquipment(new Equipment(item, 1));
            }
        }
    }

    @Override
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append(getId()).append(" - ").append(getName());
        info.append("\nLocation: ").append(getLocation());
        info.append("\nCapacity: ").append(getCapacity());
        info.append("\nType: ").append(getType());
        info.append("\nStatus: ").append(getStatus());
        info.append("\nPrivilege: ").append(getPrivilege());

        if (!getEquipment().isEmpty()) {
            info.append("\nEquipment: ");
            for (Equipment eq : getEquipment()) {
                info.append(eq.toString()).append(", ");
            }
            info.setLength(info.length() - 2); // Remove last comma
        }

        if (getNotes() != null && !getNotes().isEmpty()) {
            info.append("\nNotes: ").append(getNotes());
        }

        return info.toString();
    }

    // Legacy getters for backward compatibility
    public String getRoomID() {
        return getId();
    }

    public void setRoomID(String roomID) {
        setId(roomID);
    }

    public String getAvailabilityStatus() {
        return getStatus().toString();
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        setStatus(mapFacilityStatus(availabilityStatus));
    }

    public String getEligibility() {
        return getPrivilege().toString();
    }

    public void setEligibility(String eligibility) {
        setPrivilege(mapReservationPrivilege(eligibility));
    }

    // Get equipment as string for legacy compatibility
    public String getEquipmentAsString() {
        if (getEquipment().isEmpty()) return "N/A";

        StringBuilder sb = new StringBuilder();
        for (Equipment eq : getEquipment()) {
            sb.append(eq.getName());
            if (eq.getQuantity() > 1) {
                sb.append(" (x").append(eq.getQuantity()).append(")");
            }
            sb.append(", ");
        }
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2); // Remove last comma and space
        }
        return sb.toString();
    }

    public void setEquipment(String equipment) {
        // Clear existing equipment
        this.equipment.clear();
        // Parse and add new equipment
        parseAndAddEquipment(equipment);
    }
}
