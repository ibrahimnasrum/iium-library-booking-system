package model.services;

import model.User;
import model.enums.Role;
import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private static Map<String, User> users = new HashMap<>();

    static {
        // Pre-create some test users with different roles
        createTestUsers();
    }

    private static void createTestUsers() {
        // Admin users (matric starting with 0)
        users.put("0123456", new User("0123456", "admin123", "Admin User", Role.ADMIN));
        users.put("0987654", new User("0987654", "admin456", "Library Admin", Role.ADMIN));

        // Staff users (matric starting with 1)
        users.put("1123456", new User("1123456", "staff123", "Staff User", Role.STAFF));
        users.put("1234567", new User("1234567", "staff456", "Librarian", Role.STAFF));

        // Student users (matric starting with 2)
        users.put("2123456", new User("2123456", "student123", "Student User", Role.STUDENT));
        users.put("2234567", new User("2234567", "student456", "Undergraduate", Role.STUDENT));

        // Postgraduate users (matric starting with 3)
        users.put("3123456", new User("3123456", "postgrad123", "Postgrad User", Role.POSTGRADUATE));
        users.put("3234567", new User("3234567", "postgrad456", "PhD Student", Role.POSTGRADUATE));
    }

    /**
     * Authenticates a user based on matric number and password
     * @param matricNo The matriculation number
     * @param password The password
     * @return User object if authentication successful, null otherwise
     */
    public static User login(String matricNo, String password) {
        User user = users.get(matricNo);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        // For demo purposes, if user doesn't exist, create one based on matric pattern
        if (matricNo != null && !matricNo.isEmpty() && password != null && !password.isEmpty()) {
            Role role = determineRole(matricNo);
            User newUser = new User(matricNo, password, "User " + matricNo, role);
            users.put(matricNo, newUser);
            return newUser;
        }

        return null;
    }

    /**
     * Determines user role based on matric number pattern
     * @param matricNo The matriculation number
     * @return The appropriate role
     */
    private static Role determineRole(String matricNo) {
        if (matricNo.startsWith("0")) {
            return Role.ADMIN; // Admin matric numbers start with 0
        } else if (matricNo.startsWith("1")) {
            return Role.STAFF; // Staff matric numbers start with 1
        } else if (matricNo.startsWith("2")) {
            return Role.STUDENT; // Student matric numbers start with 2
        } else if (matricNo.startsWith("3")) {
            return Role.POSTGRADUATE; // Postgraduate matric numbers start with 3
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