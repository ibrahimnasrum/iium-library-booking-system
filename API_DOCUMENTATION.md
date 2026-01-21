# 📚 IIUM Library Booking System - API Documentation

## Overview

This document provides detailed API documentation for the IIUM Library Booking System, including all public classes, methods, and their usage.

> **Note (recent updates):** The application now starts with a **Login** screen (no auto-login). Closed and Maintenance facility statuses are preserved and displayed. A project UML diagram is available at `uml/full_project_diagram.puml` (rendered image: `uml/full_project_diagram.png`). See `CHANGELOG.md` for the full list of recent changes.

## Table of Contents

- [Model Classes](#model-classes)
- [Service Classes](#service-classes)
- [View Classes](#view-classes)
- [Enums](#enums)
- [Utility Classes](#utility-classes)

## Model Classes

### User

Represents a system user with booking capabilities.

```java
public class User {
    private String matricNo;
    private Role role;
    private List<Booking> myBookings;

    // Constructors
    public User(String matricNo, Role role)

    // Getters
    public String getMatricNo()
    public Role getRole()
    public List<Booking> getMyBookings()

    // Business Methods
    public boolean canBook(Facility facility)
    public void addBooking(Booking booking)
    public void cancelBooking(Booking booking)
    public boolean hasActiveBooking(String facilityId, LocalDateTime start, LocalDateTime end)
}
```

**Usage Example:**
```java
User postgraduate = new User("3123456", Role.STUDENT);
// Check if user can book a postgraduate facility
boolean canBook = postgraduate.canBook(carrelRoom);
```

### Facility

Represents a bookable facility in the IIUM library system.

```java
public class Facility {
    private String id;
    private String name;
    private FacilityType type;
    private FacilityStatus status;
    private ReservationPrivilege privilege;
    private String location;
    private int capacity;
    private List<Equipment> equipment;
    private String detailedInfo;

    // Constructors
    public Facility(String id, String name, FacilityType type, ReservationPrivilege privilege)

    // Getters
    public String getId()
    public String getName()
    public FacilityType getType()
    public FacilityStatus getStatus()
    public ReservationPrivilege getPrivilege()
    public String getLocation()
    public int getCapacity()
    public List<Equipment> getEquipment()
    public String getDetailedInfo()

    // Setters
    public void setStatus(FacilityStatus status)

    // Business Methods
    public boolean isAvailable()
    public boolean isAccessibleBy(User user)
    public String getStatusDisplayText()
}
```

**Usage Example:**
```java
Facility carrelRoom = new Facility("CR-L1-01", "Carrel Room L1-01",
                                  FacilityType.CARREL_ROOM,
                                  ReservationPrivilege.POSTGRADUATE_ONLY);
carrelRoom.setStatus(FacilityStatus.AVAILABLE);
```

### Booking

Represents a booking reservation.

```java
public class Booking {
    private String bookingID;
    private String userId;
    private String facilityId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;

    // Constructors
    public Booking(String facilityId, String userId, LocalDateTime startTime, LocalDateTime endTime)

    // Getters
    public String getBookingID()
    public String getUserId()
    public String getFacilityId()
    public LocalDateTime getStartTime()
    public LocalDateTime getEndTime()
    public BookingStatus getStatus()

    // Setters
    public void setStatus(BookingStatus status)

    // Business Methods
    public boolean isActive()
    public boolean isExpired()
    public boolean conflictsWith(Booking other)
    public Duration getDuration()
}
```

**Usage Example:**
```java
LocalDateTime start = LocalDateTime.of(2026, 1, 21, 9, 0);
LocalDateTime end = LocalDateTime.of(2026, 1, 21, 10, 0);
Booking booking = new Booking("CR-L1-01", "3123456", start, end);
```

### Equipment

Represents equipment available in a facility.

```java
public class Equipment {
    private String name;
    private String description;

    // Constructors
    public Equipment(String name, String description)
    public Equipment(String name)

    // Getters
    public String getName()
    public String getDescription()

    // Override Methods
    public String toString()
    public boolean equals(Object obj)
    public int hashCode()
}
```

## Service Classes

### AuthService

Handles user authentication and role management.

```java
public class AuthService {

    // Authentication Methods
    public static User login(String matricNo, String password)
    public static boolean validateMatricNumber(String matricNo)

    // Role Management
    public static Role determineRole(String matricNo)
    public static boolean hasAdminPrivileges(User user)
    public static boolean hasStaffPrivileges(User user)

    // Utility Methods
    private static boolean isValidMatricFormat(String matricNo)
}
```

**Usage Example:**
```java
// Login user
User user = AuthService.login("3123456", "password");

// Check role
Role role = AuthService.determineRole("3123456"); // Returns STUDENT

// Check privileges
boolean isAdmin = AuthService.hasAdminPrivileges(user);
```

### BookingService

Central service for booking operations.

```java
public class BookingService {

    // Booking Operations
    public static Booking createBooking(User user, Facility facility,
                                      LocalDateTime startTime, LocalDateTime endTime)
    public static boolean cancelBooking(User user, Booking booking)
    public static List<Booking> getUserBookings(String userId)
    public static List<Booking> getAllBookings()

    // Conflict Detection
    public static boolean hasBookingConflict(String facilityId,
                                           LocalDateTime startTime, LocalDateTime endTime)
    public static List<Booking> getConflictingBookings(String facilityId,
                                                     LocalDateTime startTime, LocalDateTime endTime)

    // Maintenance
    public static void cleanupExpiredBookings()
    public static void clearAllBookingsAndResetFacilities()
    public static void initializeAndCleanup()

    // Statistics
    public static int getActiveBookingCount()
    public static List<Booking> getBookingsForFacility(String facilityId)
}
```

**Usage Example:**
```java
// Create booking
Booking booking = BookingService.createBooking(user, facility, startTime, endTime);

// Check conflicts
boolean hasConflict = BookingService.hasBookingConflict("CR-L1-01", startTime, endTime);

// Get user bookings
List<Booking> myBookings = BookingService.getUserBookings("3123456");
```

### BookingPolicy

Implements business rules for booking validation.

```java
public class BookingPolicy {

    // Main Validation
    public static boolean canBook(User user, Facility facility,
                                LocalDateTime startTime, LocalDateTime endTime)
    public static String validateBooking(User user, Facility facility,
                                       LocalDateTime startTime, LocalDateTime endTime)

    // Privilege Checks
    public static boolean hasRequiredPrivilege(User user, ReservationPrivilege requiredPrivilege)
    public static boolean canAccessFacility(User user, Facility facility)

    // Time Validation
    public static boolean isValidBookingTime(LocalDateTime startTime, LocalDateTime endTime)
    public static boolean isWithinBusinessHours(LocalDateTime time)
    public static boolean isAdvanceBooking(LocalDateTime bookingTime)

    // Business Rules
    private static boolean validateFacilityAvailability(Facility facility)
    private static boolean validateUserPrivileges(User user, ReservationPrivilege privilege)
    private static boolean validateTimeConstraints(LocalDateTime startTime, LocalDateTime endTime)
    private static boolean validateNoConflicts(Facility facility, LocalDateTime startTime, LocalDateTime endTime)
}
```

**Usage Example:**
```java
// Validate booking
boolean canBook = BookingPolicy.canBook(user, facility, startTime, endTime);

// Get validation error message
String error = BookingPolicy.validateBooking(user, facility, startTime, endTime);
```

### FacilityService

Manages facility data and operations.

```java
public class FacilityService {

    // Data Access
    public static List<Facility> getAllFacilities()
    public static Facility getFacilityById(String id)
    public static List<Facility> getFacilitiesByType(FacilityType type)
    public static List<Facility> getFacilitiesByStatus(FacilityStatus status)
    public static List<Facility> getFacilitiesByPrivilege(ReservationPrivilege privilege)

    // Search & Filter
    public static List<Facility> searchFacilities(String query)
    public static List<Facility> filterFacilities(List<Facility> facilities,
                                                String location, FacilityStatus status)

    // Status Management
    public static void updateFacilityStatus(String facilityId, FacilityStatus newStatus)
    public static void resetAllFacilitiesToAvailable()

    // Statistics
    public static int getAvailableFacilityCount()
    public static int getBookedFacilityCount()
    public static Map<FacilityType, Integer> getFacilityCountByType()
}
```

**Usage Example:**
```java
// Get all facilities
List<Facility> facilities = FacilityService.getAllFacilities();

// Search facilities
List<Facility> results = FacilityService.searchFacilities("carrel");

// Filter by status
List<Facility> available = FacilityService.getFacilitiesByStatus(FacilityStatus.AVAILABLE);
```

## View Classes

### MainApplication

Main JavaFX application class.

```java
public class MainApplication extends Application {

    // Lifecycle Methods
    public void start(Stage primaryStage)
    private void showMainLayout()
    private void showLoginFailed()

    // Initialization
    private void initializeData()
    private void setupAutoLogin()

    // UI Management
    private void setupStage(Stage stage)
    private void loadStyles()
}
```

### MainLayout

Main application layout with navigation.

```java
public class MainLayout extends VBox {

    // Navigation
    public void showPage(String pageId)
    private void navigateToFacilityDetail(Facility facility)

    // Page Management
    private void initializePages()
    private void setupLayout()

    // Facility Management
    public void refreshFacilitiesPage()
    public void refreshAllFacilityDisplays()

    // User Management
    private void showUserSwitchingDialog()
    private void switchUser(String matricNo)
}
```

### FacilitiesPage

Facility browsing and search interface.

```java
public class FacilitiesPage extends VBox {

    // Data Management
    private void loadData()
    private void updateFacilitiesDisplay()

    // UI Components
    private VBox createHeader()
    private VBox createSearchSection()
    private VBox createFacilitiesSection()

    // Search & Filter
    private void filterFacilities()
    private void searchFacilities(String query)

    // Status Updates
    public void refreshFacilityStatuses()
    private void updateFacilityCard(Facility facility)
}
```

### FacilityDetailPage

Individual facility booking interface.

```java
public class FacilityDetailPage extends ScrollPane {

    // Data Display
    private void updateFacilityInfo(Facility facility)
    private void updateEquipmentList()
    private void updateBookingSection()

    // Booking Operations
    private void handleBooking(DatePicker startDate, DatePicker endDate,
                              ComboBox<String> startTime, ComboBox<String> endTime)
    private String validateBooking(LocalDateTime startTime, LocalDateTime endTime)

    // UI Components
    private VBox createFacilityInfoSection()
    private VBox createBookingSection()
    private VBox createEquipmentSection()
}
```

## Enums

### Role

User role enumeration.

```java
public enum Role {
    ADMIN("Administrator"),     // Full system access
    STAFF("Staff Member"),      // Staff facilities + general access
    STUDENT("Student");         // Student facilities only

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
```

### FacilityStatus

Facility availability status.

```java
public enum FacilityStatus {
    AVAILABLE("Available"),           // Ready for booking
    BOOKED("Booked"),                 // Currently reserved
    MAINTENANCE("Under Maintenance"), // Under maintenance
    TEMPORARILY_CLOSED("Closed"),     // Closed temporarily
    RESERVED("Reserved");             // Reserved for special use

    private final String displayText;

    FacilityStatus(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}
```

### FacilityType

Type of facility.

```java
public enum FacilityType {
    CARREL_ROOM("Carrel Room"),
    DISCUSSION_ROOM("Discussion Room"),
    STUDY_AREA("Study Area"),
    STAFF_ROOM("Staff Room"),
    COMPUTER_ROOM("Computer Room"),
    MULTIMEDIA_ROOM("Multimedia Room");

    private final String displayName;

    FacilityType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
```

### ReservationPrivilege

Access privilege levels.

```java
public enum ReservationPrivilege {
    OPEN("Open to All"),
    STUDENT_ONLY("Students Only"),
    STAFF_ONLY("Staff Only"),
    POSTGRADUATE_ONLY("Postgraduate Only"),
    LIBRARY_USE_ONLY("Library Staff Only"),
    SPECIAL_NEEDS_ONLY("Special Needs Only"),
    BOOK_VENDORS_ONLY("Book Vendors Only");

    private final String displayText;

    ReservationPrivilege(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}
```

### BookingStatus

Booking status enumeration.

```java
public enum BookingStatus {
    ACTIVE("Active"),         // Currently active booking
    CANCELLED("Cancelled"),   // Cancelled by user
    EXPIRED("Expired"),       // Automatically expired
    COMPLETED("Completed");   // Successfully completed

    private final String displayText;

    BookingStatus(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}
```

## Utility Classes

### SessionManager (Legacy)

Legacy session management class.

```java
public class SessionManager {
    private static User currentUser;
    private static List<Facility> facilities;
    private static List<Booking> bookings;

    // Session Management
    public static void setCurrentUser(User user)
    public static User getCurrentUser()

    // Data Management
    public static void initializeData()
    public static List<Facility> getFacilities()
    public static List<Booking> getBookings()

    // Booking Operations
    public static boolean makeBooking(Facility facility, LocalDateTime start, LocalDateTime end)
    public static boolean cancelBooking(Booking booking)
}
```

---

*This API documentation is automatically generated from the source code and reflects the current implementation as of January 2025.*