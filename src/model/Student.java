package model;

import model.enums.Role;

public class Student extends User {

    public Student(String matricNo, String password, String name) {
        super(matricNo, password, name, Role.STUDENT);
    }

    @Override
    public boolean canAccessAdminPanel() {
        return false; // Students cannot access admin panel
    }

    @Override
    public boolean canBookSpecialFacilities() {
        return false; // Students cannot book special facilities
    }

    @Override
    public int getMaxBookingHours() {
        return 2; // Students can book up to 2 hours
    }
}