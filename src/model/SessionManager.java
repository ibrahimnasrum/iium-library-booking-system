package model;

import java.util.ArrayList;
import java.util.List;

/**
 * SessionManager - Manages the current logged-in user and shared application state
 */
public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private List<Room> allRooms;
    
    private SessionManager() {
        // Initialize with sample rooms
        allRooms = new ArrayList<>();
        allRooms.add(new Room("R001", "Study Room", 4, "Level 1", "Available"));
        allRooms.add(new Room("R002", "Discussion Room", 6, "Level 2", "Available"));
        allRooms.add(new Room("R003", "Meeting Room", 8, "Level 3", "Available"));
        allRooms.add(new Room("R004", "Private Room", 2, "Level 1", "Available"));
        allRooms.add(new Room("R005", "Conference Room", 12, "Level 4", "Available"));
    }
    
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public List<Room> getAllRooms() {
        return allRooms;
    }
    
    public void logout() {
        currentUser = null;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
