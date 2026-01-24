# 📖 IIUM Library Booking System - API Documentation

Complete API reference for the IIUM Library Booking System.

## 📦 Package Structure

```
model/
├── User.java                 # Abstract user base class
├── Admin.java                # Admin user subclass
├── Staff.java                # Staff user subclass
├── Student.java              # Student user subclass
├── Postgraduate.java         # Postgraduate user subclass
├── Facility.java             # Facility data model
├── Booking.java              # Booking record model
├── Equipment.java            # Equipment tracking
├── SessionManager.java       # Session management
├── enums/                    # System enumerations
│   ├── Role.java            # User roles
│   ├── BookingStatus.java   # Booking states
│   ├── FacilityStatus.java  # Facility availability
│   └── FacilityType.java    # Facility categories
└── services/                 # Business logic services
    ├── AuthService.java      # Authentication & authorization
    ├── BookingService.java   # Booking operations
    ├── FacilityService.java  # Facility management
    └── BookingPolicy.java    # Booking rules & validation

view/
├── MainApplication.java      # JavaFX application launcher
├── MainLayout.java           # Main UI layout with navigation
├── components/               # Reusable UI components
└── pages/                    # Application pages
    ├── LoginPage.java        # Authentication screen
    ├── DashboardPage.java    # User dashboard
    ├── FacilitiesPage.java   # Facility browsing
    ├── FacilityDetailPage.java # Facility details
    ├── BookingPage.java      # Booking management
    ├── AdminPanelPage.java   # Administrative functions
    └── MyBookingsPage.java   # User's booking history

tools/
├── AuthTest.java            # Authentication testing
├── AutoLoginTest.java       # Automated login verification
└── RunModelTest.java        # Model validation
```

## 👤 User Model

### Class: `model.User`

Represents a system user with authentication and booking capabilities.

#### Constructor
```java
public User(String matricNo, String password, String name, Role role)
```

#### Methods

##### Getters
```java
public String getMatricNo()           // Returns matriculation number
public String getName()               // Returns user name
public Role getRole()                 // Returns user role
public List<Booking> getMyBookings()  // Returns user's bookings
```

##### Setters
```java
public void setRole(Role role)        // Updates user role
```

##### Static Methods
```java
public static List<Room> getAllRooms() // Returns all facilities (legacy)
```

##### Instance Methods
```java
public boolean makeBooking(Room room, LocalDateTime startTime, LocalDateTime endTime)
// Creates a booking for the user (legacy method)

public boolean cancelBooking(Booking booking)
// Cancels a user's booking (legacy method)
```

## 🏢 Facility Model

### Class: `model.Facility`

Represents a bookable facility in the IIUM library system.

#### Constructor
```java
public Facility(String id, String name, int capacity, String location,
                FacilityStatus status, String equipment, String accessRules,
                FacilityType type, String description)
```

#### Methods

##### Getters
```java
public String getId()                    // Returns facility ID
public String getName()                  // Returns facility name
public int getCapacity()                 // Returns capacity
public String getLocation()              // Returns location
public FacilityStatus getStatus()        // Returns current status
public String getEquipment()             // Returns equipment list
public String getAccessRules()           // Returns access rules
public FacilityType getType()            // Returns facility type
public String getDescription()           // Returns description
```

##### Setters
```java
public void setStatus(FacilityStatus status)  // Updates facility status
public void setEquipment(String equipment)    // Updates equipment
public void setAccessRules(String rules)      // Updates access rules
```

##### Business Methods
```java
public boolean isAvailable()             // Checks if facility is available
public boolean canBeBookedBy(User user)  // Checks user permissions
public String getStatusDisplay()         // Returns display-friendly status
```

## 📅 Booking Model

### Class: `model.Booking`

Represents a booking record in the system.

#### Constructor
```java
public Booking(String facilityId, String userId, LocalDateTime startTime, LocalDateTime endTime)
```

#### Methods

##### Getters
```java
public String getId()                    // Returns booking ID
public String getFacilityId()            // Returns facility ID
public String getUserId()                // Returns user ID
public LocalDateTime getStartTime()      // Returns start time
public LocalDateTime getEndTime()        // Returns end time
public BookingStatus getStatus()         // Returns booking status
public LocalDateTime getCreatedAt()      // Returns creation timestamp
```

##### Setters
```java
public void setStatus(BookingStatus status)  // Updates booking status
```

##### Business Methods
```java
public boolean isActive()                // Checks if booking is active
public boolean isUpcoming()              // Checks if booking is in future
public boolean canBeCancelled()          // Checks if booking can be cancelled
public long getDurationMinutes()         // Returns booking duration
```

## 🔐 Authentication Service

### Class: `model.services.AuthService`

Handles user authentication and authorization.

#### Methods

##### Authentication
```java
public static User login(String matricNo, String password)
// Authenticates user and returns User object if successful
// Returns null if authentication fails
```

##### Role Management
```java
private static Role determineRole(String matricNo)
// Determines user role based on matric number pattern:
// - Starts with "1": ADMIN
// - Starts with "2": STAFF
// - Others: STUDENT
```

##### Permission Checks
```java
public static boolean isAdmin(User user)
// Returns true if user has admin role

public static boolean isStaff(User user)
// Returns true if user has staff or admin role

public static boolean canAccessAdminFeatures(User user)
// Returns true if user can access administrative features
```

## 📋 Booking Service

### Class: `model.services.BookingService`

Manages all booking operations and business logic.

#### Methods

##### Booking Operations
```java
public static Booking createBooking(User user, Facility facility,
                                   LocalDateTime startTime, LocalDateTime endTime)
// Creates a new booking with validation
// Returns Booking object if successful, null if validation fails
```

```java
public static boolean cancelBooking(User user, Booking booking)
// Cancels an active booking
// Returns true if cancellation successful
```

##### Query Methods
```java
public static List<Booking> getAllBookings()
// Returns all bookings in the system

public static List<Booking> getBookingsForUser(String userId)
// Returns all bookings for a specific user

public static List<Booking> getBookingsForFacility(String facilityId)
// Returns all bookings for a specific facility

public static List<Booking> getActiveBookings()
// Returns all active (non-cancelled) bookings
```

##### Conflict Detection
```java
private static boolean hasBookingConflict(String facilityId,
                                         LocalDateTime startTime, LocalDateTime endTime)
// Checks if proposed booking conflicts with existing bookings
// Returns true if conflict exists
```

## 🏢 Facility Service

### Class: `model.services.FacilityService`

Manages facility data and operations.

#### Methods

##### Facility Retrieval
```java
public static List<Facility> getAllFacilities()
// Returns all facilities in the system

public static Facility findFacilityById(String id)
// Returns facility with specified ID, null if not found

public static List<Facility> searchFacilities(String query)
// Returns facilities matching search query

public static List<Facility> filterFacilities(FacilityType type, FacilityStatus status)
// Returns facilities matching type and status filters
```

##### Facility Management
```java
public static boolean addFacility(Facility facility)
// Adds a new facility to the system

public static boolean updateFacility(Facility facility)
// Updates existing facility information

public static boolean removeFacility(String facilityId)
// Removes facility from system (admin only)
```

## 📏 Booking Policy

### Class: `model.services.BookingPolicy`

Defines and enforces booking rules and validation.

#### Methods

##### Validation
```java
public static boolean canBook(User user, Facility facility,
                             LocalDateTime startTime, LocalDateTime endTime)
// Validates if user can make the booking
// Checks permissions, time constraints, and business rules
```

##### Time Validation
```java
public static boolean isValidBookingTime(LocalDateTime startTime, LocalDateTime endTime)
// Validates booking time constraints
// - Maximum 2 hours per booking
// - Within operating hours
// - Not in the past
```

##### Permission Validation
```java
public static boolean hasBookingPermission(User user, Facility facility)
// Checks if user has permission to book the facility
// Based on user role and facility access rules
```

## 🔧 View Components

### Class: `view.MainApplication`

Main JavaFX application class.

#### Methods
```java
public void start(Stage primaryStage)
// Application entry point - shows login screen

private void showLoginPage()
// Displays the login screen

private void onLoginSuccess(User user)
// Handles successful login - shows main layout

private void showMainLayout()
// Displays main application interface
```

### Class: `view.pages.LoginPage`

Login screen component.

#### Constructor
```java
public LoginPage(Consumer<User> onLoginSuccess)
// Creates login page with success callback
```

#### Methods
```java
private void handleLogin()
// Processes login attempt

private void showError(String message)
// Displays error message to user

public void clearFields()
// Clears login form fields

public void quickLoginAs(String matric, String password)
// Programmatic login for testing (used by quick buttons)
```

### Class: `view.MainLayout`

Main application layout with navigation.

#### Constructor
```java
public MainLayout(User currentUser)
// Creates main layout for authenticated user
```

#### Methods
```java
private void setupNavigation()
// Sets up sidebar navigation

private void showPage(String pageName)
// Switches to specified page

private void logout()
// Logs out current user and returns to login
```

## 🧪 Testing Tools

### Class: `tools.AuthTest`

Tests authentication service functionality.

#### Methods
```java
public static void main(String[] args)
// Runs authentication tests
// Tests various matric number patterns and role assignment
```

### Class: `tools.AutoLoginTest`

Automated login testing with JavaFX.

#### Methods
```java
public static void main(String[] args)
// Launches JavaFX application with automated login testing
// Tests login screen functionality
```

### Class: `tools.RunModelTest`

Validates model classes and data integrity.

#### Methods
```java
public static void main(String[] args)
// Tests model classes and relationships
// Validates facility and booking data
```

## 📊 Enums

### `model.enums.Role`
```java
public enum Role {
    STUDENT,    // Regular students
    STAFF,      // Library staff
    ADMIN       // System administrators
}
```

### `model.enums.BookingStatus`
```java
public enum BookingStatus {
    ACTIVE,     // Active booking
    CANCELLED,  // Cancelled booking
    COMPLETED   // Completed booking
}
```

### `model.enums.FacilityStatus`
```java
public enum FacilityStatus {
    AVAILABLE,      // Available for booking
    BOOKED,         // Currently booked
    MAINTENANCE,    // Under maintenance
    CLOSED         // Permanently closed
}
```

### `model.enums.FacilityType`
```java
public enum FacilityType {
    STUDY_ROOM,         // Individual study rooms
    DISCUSSION_ROOM,    // Group discussion rooms
    COMPUTER_LAB,       // Computer laboratories
    AUDITORIUM,         // Large meeting spaces
    EXHIBITION_AREA,    // Exhibition spaces
    SPECIAL_FACILITY    // Special purpose facilities
}
```

## 🔄 Data Flow

### Authentication Flow
1. `LoginPage.handleLogin()` → `AuthService.login()`
2. Role determination → `User` object creation
3. `MainApplication.onLoginSuccess()` → `MainLayout` display

### Booking Flow
1. User selects facility → `BookingPage`
2. `BookingService.createBooking()` validation
3. `BookingPolicy.canBook()` permission check
4. Conflict detection → Booking creation
5. Facility status update → User notification

### Facility Management Flow
1. `FacilityService.getAllFacilities()` → Display
2. Search/Filter → `FacilityService.searchFacilities()`
3. Booking request → `BookingService.createBooking()`

## ⚠️ Important Notes

- **Legacy Methods**: Some methods in `User.java` are marked `@Deprecated`
- **Service Layer**: All business logic is centralized in service classes
- **Validation**: All operations include comprehensive validation
- **Thread Safety**: Services are not thread-safe for simplicity
- **Data Storage**: All data is stored in memory (no persistence)

## 🔍 Usage Examples

### Creating a Booking
```java
User user = AuthService.login("1234567", "password");
Facility facility = FacilityService.findFacilityById("DR-01");
LocalDateTime start = LocalDateTime.now().plusDays(1);
LocalDateTime end = start.plusHours(2);

Booking booking = BookingService.createBooking(user, facility, start, end);
if (booking != null) {
    System.out.println("Booking created: " + booking.getId());
}
```

### Checking Permissions
```java
User user = AuthService.login("0123456", "password");
if (AuthService.isAdmin(user)) {
    // Show admin features
}
```

### Facility Search
```java
List<Facility> results = FacilityService.searchFacilities("discussion");
for (Facility facility : results) {
    System.out.println(facility.getName() + " - " + facility.getStatus());
}
```

---
**For user guides, see [USER_MANUAL.md](USER_MANUAL.md)**