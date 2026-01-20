package model.services;

import model.Facility;
import model.Room;
import model.Equipment;
import model.User;
import model.enums.FacilityStatus;
import model.enums.FacilityType;
import model.enums.ReservationPrivilege;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FacilityService {

    private static List<Facility> facilities = new ArrayList<>();

    static {
        // Initialize with existing rooms from User.allRooms
        facilities.addAll(User.getAllRooms());
    }

    /**
     * Get all facilities
     */
    public static List<Facility> getAllFacilities() {
        return new ArrayList<>(facilities);
    }

    /**
     * Get facilities by type
     */
    public static List<Facility> getFacilitiesByType(FacilityType type) {
        return facilities.stream()
                .filter(f -> f.getType() == type)
                .collect(Collectors.toList());
    }

    /**
     * Get facilities by status
     */
    public static List<Facility> getFacilitiesByStatus(FacilityStatus status) {
        return facilities.stream()
                .filter(f -> f.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Search facilities by name or ID
     */
    public static List<Facility> searchFacilities(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllFacilities();
        }

        String lowerQuery = query.toLowerCase();
        return facilities.stream()
                .filter(f -> f.getName().toLowerCase().contains(lowerQuery) ||
                           f.getId().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    /**
     * Get available facilities for booking
     */
    public static List<Facility> getAvailableFacilities() {
        return facilities.stream()
                .filter(Facility::isAvailable)
                .collect(Collectors.toList());
    }

    /**
     * Find facility by ID
     */
    public static Facility findFacilityById(String id) {
        return facilities.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Add new facility (Admin only)
     */
    public static boolean addFacility(Facility facility) {
        if (facility != null && findFacilityById(facility.getId()) == null) {
            facilities.add(facility);
            return true;
        }
        return false;
    }

    /**
     * Update facility (Admin only)
     */
    public static boolean updateFacility(String id, Facility updatedFacility) {
        Facility existing = findFacilityById(id);
        if (existing != null && updatedFacility != null) {
            int index = facilities.indexOf(existing);
            facilities.set(index, updatedFacility);
            return true;
        }
        return false;
    }

    /**
     * Remove facility (Admin only)
     */
    public static boolean removeFacility(String id) {
        Facility facility = findFacilityById(id);
        if (facility != null) {
            facilities.remove(facility);
            return true;
        }
        return false;
    }

    /**
     * Update facility status (Admin only)
     */
    public static boolean updateFacilityStatus(String id, FacilityStatus status) {
        Facility facility = findFacilityById(id);
        if (facility != null) {
            facility.setStatus(status);
            return true;
        }
        return false;
    }

    /**
     * Mark facility as temporarily closed
     */
    public static boolean markTemporarilyClosed(String id) {
        return updateFacilityStatus(id, FacilityStatus.TEMPORARILY_CLOSED);
    }

    /**
     * Get facilities by location
     */
    public static List<Facility> getFacilitiesByLocation(String location) {
        return facilities.stream()
                .filter(f -> f.getLocation().toLowerCase().contains(location.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Get facilities by privilege level
     */
    public static List<Facility> getFacilitiesByPrivilege(ReservationPrivilege privilege) {
        return facilities.stream()
                .filter(f -> f.getPrivilege() == privilege)
                .collect(Collectors.toList());
    }

    /**
     * Get facility statistics
     */
    public static FacilityStats getFacilityStats() {
        long total = facilities.size();
        long available = facilities.stream().filter(Facility::isAvailable).count();
        long booked = facilities.stream().filter(f -> f.getStatus() == FacilityStatus.BOOKED).count();
        long closed = facilities.stream().filter(f -> f.getStatus() == FacilityStatus.TEMPORARILY_CLOSED).count();

        return new FacilityStats(total, available, booked, closed);
    }

    /**
     * Facility statistics class
     */
    public static class FacilityStats {
        public final long total;
        public final long available;
        public final long booked;
        public final long closed;

        public FacilityStats(long total, long available, long booked, long closed) {
            this.total = total;
            this.available = available;
            this.booked = booked;
            this.closed = closed;
        }
    }
}