package model;

import model.enums.Role;

public class Staff extends User {

    public Staff(String matricNo, String password, String name) {
        super(matricNo, password, name, Role.STAFF);
    }

    @Override
    public boolean canAccessAdminPanel() {
        return false; // Staff cannot access admin panel
    }

    @Override
    public boolean canBookSpecialFacilities() {
        return true; // Staff can book special facilities
    }

    @Override
    public int getMaxBookingHours() {
        return 4; // Staff can book up to 4 hours
    }
}