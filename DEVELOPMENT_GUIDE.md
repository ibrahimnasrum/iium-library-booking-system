# 🛠️ IIUM Library Booking System - Development Guide

Complete development guide for the IIUM Library Booking System.

## 🏗️ Architecture Overview

### MVC Pattern Implementation
```
Model (Data & Business Logic)
├── model/                    # Data models
├── model/services/          # Business logic
└── model/enums/             # System enumerations

View (User Interface)
├── view/MainApplication.java # Application launcher
├── view/MainLayout.java      # Main UI container
├── view/pages/               # Page components
└── view/components/          # Reusable UI elements

Controller (Implicit in View)
├── Event handlers in UI components
├── Service method calls
└── Data binding
```

### Service Layer Architecture
- **AuthService**: Handles authentication and authorization
- **BookingService**: Manages booking operations
- **FacilityService**: Facility data management
- **BookingPolicy**: Business rules and validation

## 🚀 Getting Started

### Development Environment Setup

#### Prerequisites
- **Java 21 JDK** - Core runtime
- **JavaFX 21.0.9** - UI framework
- **Git** - Version control
- **VS Code** - Recommended IDE

#### Project Structure Setup
```bash
# Clone repository
git clone <repository-url>
cd iium-library-booking-system

# Verify JavaFX bundle
ls javafx-sdk-21.0.9/lib/
```

### Building the Project

#### Manual Compilation
```powershell
# Compile all source files
javac --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -d bin src\Main.java src\model\*.java src\model\services\*.java src\view\*.java src\view\pages\*.java

# Run application
java --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -cp bin Main
```

#### Using run.bat
```cmd
# Simple execution
run.bat
```

## 📝 Coding Standards

### Java Naming Conventions
```java
// Classes
public class BookingService        // PascalCase
public class AuthService          // PascalCase

// Methods
public void createBooking()       // camelCase
public boolean cancelBooking()    // camelCase
private void validateInput()      // camelCase

// Variables
private String userName;          // camelCase
private List<Booking> bookings;   // camelCase
private static final int MAX_BOOKING_HOURS = 2; // UPPER_SNAKE_CASE

// Constants
public static final String DEFAULT_PASSWORD = "password";
```

### Code Organization
```java
// Package structure
model/                    // Data models
model/services/          // Business logic
model/enums/             // Enumerations
view/                    // UI components
view/pages/              // Page classes
view/components/         // Reusable components
tools/                   // Development tools
```

### Documentation Standards
```java
/**
 * Creates a new booking with validation
 * @param user The user making the booking
 * @param facility The facility to book
 * @param startTime Booking start time
 * @param endTime Booking end time
 * @return Booking object if successful, null if validation fails
 */
public static Booking createBooking(User user, Facility facility,
                                   LocalDateTime startTime, LocalDateTime endTime) {
    // Implementation
}
```

## 🔧 Adding New Features

### Adding a New Facility Type

#### 1. Update Enum
```java
// File: src/model/enums/FacilityType.java
public enum FacilityType {
    STUDY_ROOM,
    DISCUSSION_ROOM,
    COMPUTER_LAB,
    AUDITORIUM,
    EXHIBITION_AREA,
    SPECIAL_FACILITY,
    CONFERENCE_ROOM    // New type
}
```

#### 2. Create Facility Data
```java
// File: src/model/Facility.java (static initializer)
static {
    // Add conference rooms
    facilities.add(new Facility("CR-01", "Conference Room A", 20,
        "Level 3", FacilityStatus.AVAILABLE, "Projector, Video Conference",
        "Staff Only", FacilityType.CONFERENCE_ROOM,
        "Executive conference room with VC facilities"));
}
```

#### 3. Update UI Components
```java
// File: src/view/pages/FacilitiesPage.java
private void setupFilters() {
    // Add conference room filter
    CheckBox conferenceFilter = new CheckBox("Conference Rooms");
    conferenceFilter.setOnAction(e -> filterFacilities());
    // Add to filter container
}
```

### Adding a New User Role

#### 1. Update Role Enum
```java
// File: src/model/enums/Role.java
public enum Role {
    STUDENT,
    STAFF,
    ADMIN,
    GUEST         // New role
}
```

#### 2. Update Authentication
```java
// File: src/model/services/AuthService.java
private static Role determineRole(String matricNo) {
    if (matricNo.startsWith("0")) {
        return Role.ADMIN;
    } else if (matricNo.startsWith("1")) {
        return Role.STAFF;
    } else if (matricNo.startsWith("2")) {
        return Role.STUDENT;
    } else {
        return Role.GUEST;    // Default for visitors
    }
}
```

#### 3. Update Permissions
```java
// File: src/model/services/AuthService.java
public static boolean canAccessLibrary(User user) {
    return user != null && user.getRole() != Role.GUEST;
}
```

### Adding Booking Validation Rules

#### 1. Update BookingPolicy
```java
// File: src/model/services/BookingPolicy.java
public static boolean canBook(User user, Facility facility,
                             LocalDateTime startTime, LocalDateTime endTime) {
    // Existing validations...

    // New: Check advance booking limit
    if (!isWithinAdvanceBookingLimit(user, startTime)) {
        return false;
    }

    return true;
}

private static boolean isWithinAdvanceBookingLimit(User user, LocalDateTime startTime) {
    long daysInAdvance = ChronoUnit.DAYS.between(LocalDate.now(), startTime.toLocalDate());
    int maxAdvanceDays = user.getRole() == Role.ADMIN ? 90 : 30;
    return daysInAdvance <= maxAdvanceDays;
}
```

## 🧪 Testing Procedures

### Unit Testing Services
```java
// File: tools/AuthTest.java
public class AuthTest {
    public static void main(String[] args) {
        // Test authentication
        User admin = AuthService.login("0123456", "password");
        assert admin != null && admin.getRole() == Role.ADMIN;

        User student = AuthService.login("2123456", "password");
        assert student != null && student.getRole() == Role.STUDENT;

        System.out.println("All authentication tests passed!");
    }
}
```

### UI Testing
```java
// File: tools/AutoLoginTest.java
public class AutoLoginTest extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Automated UI testing
        LoginPage loginPage = new LoginPage(user -> {
            System.out.println("Login successful: " + user.getMatricNo());
            Platform.exit();
        });

        // Simulate quick login
        loginPage.quickLoginAs("0123456", "password");

        Scene scene = new Scene(loginPage, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
```

### Integration Testing
```java
// Test complete booking flow
User user = AuthService.login("0123456", "password");
Facility facility = FacilityService.getAllFacilities().get(0);
LocalDateTime start = LocalDateTime.now().plusDays(1);
LocalDateTime end = start.plusHours(1);

Booking booking = BookingService.createBooking(user, facility, start, end);
assert booking != null;
assert facility.getStatus() == FacilityStatus.BOOKED;
assert user.getMyBookings().contains(booking);
```

## 🐛 Debugging Guide

### Common Issues

#### JavaFX Module Errors
```powershell
# Ensure correct module path
javac --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml

# Check JavaFX jar files exist
dir javafx-sdk-21.0.9\lib\
```

#### Authentication Failures
```java
// Debug authentication
User user = AuthService.login(matric, password);
if (user == null) {
    System.out.println("Login failed for: " + matric);
    // Check matric format and password
}
```

#### Booking Conflicts
```java
// Debug booking creation
boolean hasConflict = BookingService.hasBookingConflict(facilityId, start, end);
if (hasConflict) {
    System.out.println("Booking conflict detected");
    // Check existing bookings for facility
}
```

### Logging Best Practices
```java
// Add logging to service methods
public static Booking createBooking(User user, Facility facility,
                                   LocalDateTime startTime, LocalDateTime endTime) {
    System.out.println("Creating booking for user: " + user.getMatricNo() +
                      " facility: " + facility.getId());

    // Validation logic...

    if (booking == null) {
        System.out.println("Booking creation failed - validation error");
        return null;
    }

    System.out.println("Booking created successfully: " + booking.getId());
    return booking;
}
```

## 🚀 Deployment

### Building for Distribution

#### Create Executable JAR
```powershell
# Compile with all dependencies
javac --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -d bin src\*.java src\model\*.java src\model\services\*.java src\view\*.java src\view\pages\*.java

# Create manifest
echo "Main-Class: Main" > manifest.txt
echo "Class-Path: ." >> manifest.txt

# Create JAR
jar cfm iium-booking.jar manifest.txt -C bin .
```

#### Windows Executable
```cmd
# Use Launch4j or similar tool to create .exe
# Configure main class: Main
# Add JVM options: --module-path javafx-sdk-21.0.9\lib --add-modules javafx.controls,javafx.fxml
```

### Environment Configuration

#### Production Settings
```java
// File: config.properties
database.url=jdbc:mysql://localhost:3306/iium_booking
database.user=booking_app
database.password=secure_password
max.booking.hours=2
advance.booking.days=30
```

#### Loading Configuration
```java
// File: src/model/services/ConfigService.java
public class ConfigService {
    private static Properties config = new Properties();

    static {
        try (InputStream input = new FileInputStream("config.properties")) {
            config.load(input);
        } catch (IOException e) {
            // Use defaults
        }
    }

    public static String get(String key) {
        return config.getProperty(key);
    }
}
```

## 📊 Performance Optimization

### Memory Management
```java
// Use weak references for cached data
private static WeakHashMap<String, Facility> facilityCache = new WeakHashMap<>();

// Implement pagination for large lists
public static List<Facility> getFacilities(int page, int pageSize) {
    int startIndex = (page - 1) * pageSize;
    int endIndex = Math.min(startIndex + pageSize, allFacilities.size());
    return allFacilities.subList(startIndex, endIndex);
}
```

### Database Optimization (Future)
```java
// Prepared statements for repeated queries
private static final String BOOKING_QUERY =
    "SELECT * FROM bookings WHERE facility_id = ? AND status = 'ACTIVE'";

public static List<Booking> getActiveBookingsForFacility(String facilityId) {
    try (PreparedStatement stmt = connection.prepareStatement(BOOKING_QUERY)) {
        stmt.setString(1, facilityId);
        ResultSet rs = stmt.executeQuery();
        // Process results...
    }
}
```

## 🔒 Security Considerations

### Input Validation
```java
public static User login(String matricNo, String password) {
    // Validate input
    if (matricNo == null || matricNo.length() != 7 ||
        !matricNo.matches("\\d{7}")) {
        return null;
    }

    if (password == null || password.length() < 4) {
        return null;
    }

    // Proceed with authentication...
}
```

### Access Control
```java
public static boolean canAccessAdminPanel(User user) {
    return user != null &&
           (user.getRole() == Role.ADMIN || user.getRole() == Role.STAFF) &&
           user.isAccountActive(); // Additional checks
}
```

## 🔄 Maintenance Procedures

### Regular Tasks
- **Code Reviews**: Review all changes before merging
- **Testing**: Run full test suite before releases
- **Documentation**: Update docs for API changes
- **Backups**: Backup configuration and data

### Version Control
```bash
# Feature branch workflow
git checkout -b feature/new-facility-type
# Make changes...
git commit -m "Add conference room facility type"
git push origin feature/new-facility-type
# Create pull request
```

### Monitoring
```java
// Add health checks
public static SystemHealth getSystemHealth() {
    return new SystemHealth(
        getActiveBookings().size(),
        getAllFacilities().size(),
        checkDatabaseConnection(),
        getMemoryUsage()
    );
}
```

## 📈 Scaling Considerations

### Current Limitations
- **In-Memory Storage**: Data lost on restart
- **Single Threaded**: No concurrent access handling
- **No Caching**: Repeated database queries

### Future Improvements
```java
// Database integration
public class DatabaseService {
    private static Connection connection;

    public static void initialize() {
        // Setup connection pool
        // Create tables if needed
    }

    public static List<Booking> getBookingsForUser(String userId) {
        // Database query implementation
    }
}

// Caching layer
public class CacheService {
    private static LoadingCache<String, Facility> facilityCache =
        Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(key -> loadFacilityFromDatabase(key));
}
```

## 🤝 Contributing Guidelines

### Pull Request Process
1. **Fork** the repository
2. **Create** a feature branch
3. **Implement** changes with tests
4. **Update** documentation
5. **Submit** pull request with description

### Code Review Checklist
- [ ] Unit tests added/updated
- [ ] Documentation updated
- [ ] Code follows style guidelines
- [ ] Security considerations addressed
- [ ] Performance impact assessed

### Issue Reporting
- **Bug Reports**: Include steps to reproduce, expected vs actual behavior
- **Feature Requests**: Describe use case and benefits
- **Security Issues**: Report privately to maintainers

---
**For API reference, see [API_DOCUMENTATION.md](API_DOCUMENTATION.md)**