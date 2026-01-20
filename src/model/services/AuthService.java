package model.services;

import model.User;
import model.enums.Role;

public class AuthService {

    /**
     * Authenticates a user based on matric number and password
     * @param matricNo The matriculation number
     * @param password The password
     * @return User object if authentication successful, null otherwise
     */
    public static User login(String matricNo, String password) {
        // Simple validation for demo
        if (matricNo != null && !matricNo.isEmpty() && password != null && !password.isEmpty()) {
            // Determine role based on matric number pattern
            Role role = determineRole(matricNo);
            return new User(matricNo, password, "User", role);
        }
        return null;
    }

    /**
     * Determines user role based on matric number pattern
     * @param matricNo The matriculation number
     * @return The appropriate role
     */
    private static Role determineRole(String matricNo) {
        if (matricNo.startsWith("2")) {
            return Role.STAFF; // Staff/Admin matric numbers start with 2
        } else if (matricNo.startsWith("1")) {
            return Role.ADMIN; // Admin matric numbers start with 1
        } else {
            return Role.STUDENT; // Default to student
        }
    }

    /**
     * Checks if user has admin privileges
     * @param user The user to check
     * @return true if user is admin, false otherwise
     */
    public static boolean isAdmin(User user) {
        return user != null && user.getRole() == Role.ADMIN;
    }

    /**
     * Checks if user has staff privileges (admin or staff)
     * @param user The user to check
     * @return true if user is staff or admin, false otherwise
     */
    public static boolean isStaff(User user) {
        return user != null && (user.getRole() == Role.STAFF || user.getRole() == Role.ADMIN);
    }

    /**
     * Checks if user can access admin features
     * @param user The user to check
     * @return true if user can access admin features
     */
    public static boolean canAccessAdminFeatures(User user) {
        return isStaff(user);
    }
}