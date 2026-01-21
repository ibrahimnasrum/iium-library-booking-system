# 🛠️ IIUM Library Booking System - Development Guide

## Overview

This development guide provides comprehensive information for developers working on the IIUM Library Booking System. It covers architecture, coding standards, testing procedures, and contribution guidelines.

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Development Environment](#development-environment)
- [Coding Standards](#coding-standards)
- [Adding New Features](#adding-new-features)
- [Testing Procedures](#testing-procedures)
- [Debugging Guide](#debugging-guide)
- [Deployment](#deployment)
- [Maintenance](#maintenance)

## Architecture Overview

### System Layers

```
┌─────────────────────────────────────┐
│         PRESENTATION LAYER          │
│  MainApplication.java              │
│  MainLayout.java                   │
│  *Page.java classes                │
│  *Component.java classes           │
└─────────────────────────────────────┘
                │
                ▼
┌─────────────────────────────────────┐
│         SERVICE LAYER              │
│  AuthService.java                  │
│  BookingService.java               │
│  BookingPolicy.java                │
│  FacilityService.java              │
└─────────────────────────────────────┘
                │
                ▼
┌─────────────────────────────────────┐
│         MODEL LAYER                │
│  User.java                         │
│  Facility.java                     │
│  Booking.java                      │
│  Equipment.java                    │
│  enums/                           │
└─────────────────────────────────────┘
```

### Design Patterns Used

1. **MVC Pattern**: Clear separation of concerns
2. **Service Layer Pattern**: Business logic encapsulation
3. **Observer Pattern**: UI updates on model changes
4. **Factory Pattern**: Object creation through services
5. **Strategy Pattern**: Different validation strategies

### Key Principles

- **Single Responsibility**: Each class has one purpose
- **Dependency Injection**: Services injected where needed
- **Interface Segregation**: Clean service interfaces
- **Open/Closed**: Open for extension, closed for modification

## Development Environment

### Prerequisites

1. **Java 21 JDK**
   ```bash
   # Download from Oracle or Adoptium
   java --version  # Should show Java 21
   ```

2. **JavaFX 21 SDK**
   ```bash
   # Download from GluonHQ
   # Extract to project directory
   ```

3. **IDE Setup**
   - **VS Code** with Java extensions
   - **IntelliJ IDEA** (recommended)
   - **Eclipse** with JavaFX support

4. **Build Tools**
   - Command line compilation
   - Optional: Maven/Gradle wrapper

### Project Structure

```
src/
├── Main.java                 # Legacy entry point
├── view/
│   ├── MainApplication.java  # Modern JavaFX application
│   ├── MainLayout.java       # Main UI container
│   ├── components/           # Reusable UI components
│   └── pages/                # Page-specific views
├── model/
│   ├── User.java            # User domain model
│   ├── Facility.java        # Facility domain model
│   ├── Booking.java         # Booking domain model
│   ├── Equipment.java       # Equipment model
│   ├── services/            # Business logic services
│   └── enums/               # Type-safe enumerations
└── styles/
    └── theme.css            # Application styling
```

### Running in Development

```bash
# Quick compilation and run
javac --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -d bin src\Main.java src\model\enums\*.java src\model\*.java src\model\services\*.java src\view\components\*.java src\view\pages\*.java src\view\*.java

java --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -cp bin view.MainApplication
```

## Coding Standards

### Java Naming Conventions

```java
// Classes
public class FacilityService {
}

// Interfaces
public interface BookingRepository {
}

// Methods
public void createBooking(User user, Facility facility) {
    // method body
}

private boolean validateBookingTime(LocalDateTime time) {
    // method body
}

// Variables
private String facilityId;
private List<Booking> activeBookings;
private boolean isAvailable;

// Constants
private static final int MAX_BOOKING_HOURS = 3;
private static final String ADMIN_ROLE_PREFIX = "0";
```

### Code Formatting

- **Indentation**: 4 spaces (no tabs)
- **Line Length**: Maximum 120 characters
- **Braces**: K&R style (opening brace on same line)
- **Imports**: Organize by package, remove unused

### Documentation Standards

```java
/**
 * Service class for managing facility-related operations.
 * Provides methods for retrieving, updating, and managing facilities.
 *
 * @author IIUM Development Team
 * @version 1.0.0
 * @since 2025-01-01
 */
public class FacilityService {

    /**
     * Retrieves all facilities in the system.
     *
     * @return List of all facilities
     */
    public static List<Facility> getAllFacilities() {
        // implementation
    }

    /**
     * Updates the status of a specific facility.
     *
     * @param facilityId The ID of the facility to update
     * @param newStatus The new status to set
     * @throws IllegalArgumentException if facility not found
     */
    public static void updateFacilityStatus(String facilityId, FacilityStatus newStatus) {
        // implementation
    }
}
```

### Error Handling

```java
public class BookingService {

    public static Booking createBooking(User user, Facility facility,
                                      LocalDateTime startTime, LocalDateTime endTime) {

        // Validate inputs
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (facility == null) {
            throw new IllegalArgumentException("Facility cannot be null");
        }

        // Business logic with proper error handling
        try {
            // Validate booking
            String validationError = BookingPolicy.validateBooking(user, facility, startTime, endTime);
            if (validationError != null) {
                System.err.println("Booking validation failed: " + validationError);
                return null;
            }

            // Create booking
            Booking booking = new Booking(facility.getId(), user.getMatricNo(), startTime, endTime);
            allBookings.add(booking);

            // Update facility status
            facility.setStatus(FacilityStatus.BOOKED);

            return booking;

        } catch (Exception e) {
            System.err.println("Error creating booking: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
```

## Adding New Features

### Adding a New Facility Type

1. **Update Enum**
   ```java
   // In FacilityType.java
   PUBLIC_READING_ROOM("Public Reading Room"),
   ```

2. **Create Facilities**
   ```java
   // In data initialization
   facilities.add(new Facility("PRR-L1-01", "Public Reading Room L1-01",
                              FacilityType.PUBLIC_READING_ROOM,
                              ReservationPrivilege.OPEN));
   ```

3. **Update UI**
   ```java
   // In FacilitiesPage.java filter options
   filterTypeCombo.getItems().addAll("All Types", "Carrel Room", "Discussion Room",
                                   "Study Area", "Public Reading Room");
   ```

### Adding a New User Role

1. **Update Role Enum**
   ```java
   VISITOR("Visitor"),  // Limited access
   ```

2. **Update AuthService**
   ```java
   private static Role determineRole(String matricNo) {
       if (matricNo.startsWith("0")) return Role.ADMIN;
       if (matricNo.startsWith("1")) return Role.STAFF;
       if (matricNo.startsWith("2")) return Role.STUDENT;
       if (matricNo.startsWith("3")) return Role.POSTGRADUATE;
       if (matricNo.startsWith("9")) return Role.VISITOR;
       return Role.STUDENT; // default
   }
   ```

3. **Update BookingPolicy**
   ```java
   case VISITOR_ONLY:
       return user.getRole() == Role.VISITOR;
   ```

### Adding a New Service Method

1. **Define Interface**
   ```java
   // In BookingService.java
   /**
    * Gets booking statistics for a user.
    * @param userId The user ID
    * @return Statistics map
    */
   public static Map<String, Object> getUserBookingStatistics(String userId) {
       // implementation
   }
   ```

2. **Add to UI**
   ```java
   // In MyBookingsPage.java
   Map<String, Object> stats = BookingService.getUserBookingStatistics(currentUser.getMatricNo());
   ```

## Testing Procedures

### Unit Testing Framework

```java
// Example test structure
public class BookingServiceTest {

    @Test
    public void testCreateBooking_Success() {
        // Arrange
        User user = new User("3123456", Role.STUDENT);
        Facility facility = new Facility("CR-L1-01", "Test Room",
                                       FacilityType.CARREL_ROOM,
                                       ReservationPrivilege.POSTGRADUATE_ONLY);
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);

        // Act
        Booking result = BookingService.createBooking(user, facility, start, end);

        // Assert
        assertNotNull(result);
        assertEquals("3123456", result.getUserId());
        assertEquals(FacilityStatus.BOOKED, facility.getStatus());
    }

    @Test
    public void testCreateBooking_InsufficientPrivileges() {
        // Test privilege validation
    }

    @Test
    public void testCreateBooking_TimeConflict() {
        // Test conflict detection
    }
}
```

### Manual Testing Checklist

#### User Authentication
- [ ] Admin login (0XXXXXX)
- [ ] Staff login (1XXXXXX)
- [ ] Student login (2XXXXXX)
- [ ] Postgraduate login (3XXXXXX)
- [ ] Invalid matric numbers
- [ ] User switching functionality

#### Facility Browsing
- [ ] All facilities display
- [ ] Search functionality
- [ ] Filter by location
- [ ] Filter by status
- [ ] Filter by type
- [ ] Facility details view

#### Booking System
- [ ] Valid booking creation
- [ ] Privilege validation
- [ ] Time validation
- [ ] Conflict detection
- [ ] Status updates
- [ ] Booking cancellation

#### Admin Features
- [ ] Facility status updates
- [ ] User management
- [ ] System monitoring
- [ ] Bulk operations

### Performance Testing

```java
// Performance test example
public class PerformanceTest {

    @Test
    public void testFacilitySearchPerformance() {
        // Create 1000 facilities
        List<Facility> facilities = createTestFacilities(1000);

        long startTime = System.nanoTime();

        // Perform search
        List<Facility> results = FacilityService.searchFacilities("test");

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // milliseconds

        System.out.println("Search took: " + duration + "ms");
        assertTrue(duration < 100); // Should complete within 100ms
    }
}
```

## Debugging Guide

### Common Issues and Solutions

#### 1. Facility Status Not Updating

**Symptoms:** UI shows old status after booking
**Debug Steps:**
```java
// Add logging in FacilitiesPage.refreshFacilityStatuses()
System.out.println("Refreshing facility: " + updatedFacility.getId() +
                  " from " + card.getFacility().getStatus() +
                  " to " + updatedFacility.getStatus());

// Check facility card mapping
FacilityCard card = facilityCardMap.get(updatedFacility.getId());
if (card == null) {
    System.err.println("No card found for facility: " + updatedFacility.getId());
}
```

**Solutions:**
- Verify facility ID mapping in `facilityCardMap`
- Check if `Facility.equals()` is implemented
- Ensure UI thread updates

#### 2. Privilege Validation Failing

**Symptoms:** Users can't book allowed facilities
**Debug Steps:**
```java
// Add logging in BookingPolicy.canBook()
System.out.println("Checking privilege for user: " + user.getMatricNo() +
                  " role: " + user.getRole() +
                  " required: " + requiredPrivilege);

// Test privilege check directly
boolean hasPrivilege = BookingPolicy.hasRequiredPrivilege(user, facility.getPrivilege());
System.out.println("Privilege check result: " + hasPrivilege);
```

**Solutions:**
- Verify matric number format
- Check role determination logic
- Validate privilege enum values

#### 3. Memory Issues

**Symptoms:** Application slows down or crashes
**Debug Steps:**
```java
// Monitor memory usage
Runtime runtime = Runtime.getRuntime();
long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
System.out.println("Used memory: " + usedMemory + "MB");

// Check collection sizes
System.out.println("Active bookings: " + BookingService.getActiveBookingCount());
System.out.println("Total facilities: " + FacilityService.getAllFacilities().size());
```

**Solutions:**
- Implement cleanup routines
- Use weak references for listeners
- Clear unused collections

### Debug Logging

```java
// Enable detailed logging
public class DebugLogger {
    private static final boolean DEBUG_ENABLED = true;

    public static void log(String message) {
        if (DEBUG_ENABLED) {
            System.out.println("[DEBUG] " + LocalDateTime.now() + " - " + message);
        }
    }

    public static void logError(String message, Exception e) {
        System.err.println("[ERROR] " + LocalDateTime.now() + " - " + message);
        if (DEBUG_ENABLED && e != null) {
            e.printStackTrace();
        }
    }
}
```

### Profiling Tools

1. **Java VisualVM**
   - Monitor memory usage
   - CPU profiling
   - Thread analysis

2. **Java Mission Control**
   - Advanced profiling
   - Flight recorder

3. **Custom Profiling**
   ```java
   public class Profiler {
       private static Map<String, Long> startTimes = new HashMap<>();

       public static void start(String operation) {
           startTimes.put(operation, System.nanoTime());
       }

       public static void end(String operation) {
           Long startTime = startTimes.remove(operation);
           if (startTime != null) {
               long duration = System.nanoTime() - startTime;
               System.out.println(operation + " took: " + (duration / 1_000_000) + "ms");
           }
       }
   }
   ```

## Deployment

### Building for Distribution

1. **Compile Release Version**
   ```bash
   # Clean compile
   rm -rf bin/*
   javac -Xlint:all --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -d bin src\*.java src\**\*.java

   # Create JAR
   jar cfe iium-booking-system.jar view.MainApplication -C bin .
   ```

2. **Create Distribution Package**
   ```bash
   # Create distribution directory
   mkdir dist
   cp iium-booking-system.jar dist/
   cp -r javafx-sdk-21.0.9 dist/
   cp run.bat dist/

   # Create zip archive
   zip -r iium-booking-system-v1.0.0.zip dist/
   ```

3. **Test Distribution**
   ```bash
   cd dist
   java --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -jar iium-booking-system.jar
   ```

### Environment Configuration

```bash
# config.properties
app.version=1.0.0
app.name=IIUM Library Booking System
javafx.version=21.0.9
database.enabled=false
debug.enabled=true
```

### Automated Deployment

```bash
# build.sh
#!/bin/bash

echo "Building IIUM Library Booking System..."

# Clean
rm -rf bin dist

# Compile
echo "Compiling..."
javac --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -d bin src/Main.java src/model/enums/*.java src/model/*.java src/model/services/*.java src/view/components/*.java src/view/pages/*.java src/view/*.java

# Package
echo "Packaging..."
mkdir -p dist/lib
cp -r javafx-sdk-21.0.9 dist/lib/
jar cfe dist/iium-booking-system.jar view.MainApplication -C bin .

# Create run script
cat > dist/run.sh << 'EOF'
#!/bin/bash
java --module-path "lib/javafx-sdk-21.0.9/lib" --add-modules javafx.controls,javafx.fxml -jar iium-booking-system.jar
EOF

chmod +x dist/run.sh

echo "Build complete!"
```

## Maintenance

### Regular Tasks

#### Weekly Maintenance
- [ ] Review error logs
- [ ] Check facility statuses
- [ ] Clean up expired bookings
- [ ] Update facility information

#### Monthly Maintenance
- [ ] Performance monitoring
- [ ] User feedback review
- [ ] Feature usage analysis
- [ ] Security updates

### Code Maintenance

#### Refactoring Guidelines
1. **Extract Methods**: Break down large methods
2. **Remove Duplication**: Consolidate repeated code
3. **Improve Naming**: Use descriptive names
4. **Add Documentation**: Keep JavaDoc current

#### Version Control
```bash
# Branching strategy
git checkout -b feature/new-facility-type
# Make changes
git commit -m "Add new facility type: Public Reading Room"
git push origin feature/new-facility-type

# Release process
git checkout main
git merge feature/new-facility-type
git tag v1.1.0
git push origin main --tags
```

### Monitoring and Alerts

```java
public class SystemMonitor {

    public static void logSystemStatus() {
        System.out.println("=== System Status ===");
        System.out.println("Active bookings: " + BookingService.getActiveBookingCount());
        System.out.println("Available facilities: " + FacilityService.getAvailableFacilityCount());
        System.out.println("Memory usage: " + getMemoryUsage() + "MB");
        System.out.println("====================");
    }

    private static long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
    }
}
```

### Backup and Recovery

```java
public class DataBackup {

    public static void createBackup() {
        try {
            // Serialize current data
            Map<String, Object> backup = new HashMap<>();
            backup.put("facilities", FacilityService.getAllFacilities());
            backup.put("bookings", BookingService.getAllBookings());
            backup.put("timestamp", LocalDateTime.now());

            // Save to file
            String filename = "backup-" + LocalDate.now() + ".json";
            saveToJsonFile(backup, filename);

            System.out.println("Backup created: " + filename);

        } catch (Exception e) {
            System.err.println("Backup failed: " + e.getMessage());
        }
    }

    public static void restoreFromBackup(String filename) {
        // Implementation for data restoration
    }
}
```

---

## Contributing

1. **Fork the repository**
2. **Create a feature branch**
3. **Make your changes following coding standards**
4. **Add tests for new functionality**
5. **Update documentation**
6. **Submit a pull request**

## Support

For development questions or issues:
- Check this development guide
- Review existing code and comments
- Contact the development team
- Check GitHub issues

---

**Document Version:** 1.0.0
**Last Updated:** January 2025
**Authors:** IIUM Development Team