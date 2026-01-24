package model;

import model.enums.Role;

public class Postgraduate extends User {

    public Postgraduate(String matricNo, String password, String name) {
        super(matricNo, password, name, Role.POSTGRADUATE);
    }

    @Override
    public boolean canAccessAdminPanel() {
        return false; // Postgraduates cannot access admin panel
    }

    @Override
    public boolean canBookSpecialFacilities() {
        return true; // Postgraduates can book special facilities like carrel rooms
    }

    @Override
    public int getMaxBookingHours() {
        return 3; // Postgraduates can book up to 3 hours (between staff and students)
    }
}