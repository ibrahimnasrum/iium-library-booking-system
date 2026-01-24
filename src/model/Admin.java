package model;

import model.enums.Role;

public class Admin extends User {

    public Admin(String matricNo, String password, String name) {
        super(matricNo, password, name, Role.ADMIN);
    }

    @Override
    public boolean canAccessAdminPanel() {
        return true;
    }

    @Override
    public boolean canBookSpecialFacilities() {
        return true;
    }

    @Override
    public int getMaxBookingHours() {
        return 8; // Admins can book longer periods
    }
}