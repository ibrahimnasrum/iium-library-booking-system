# 🏛️ IIUM Library Booking System

A comprehensive JavaFX-based library facility booking system designed specifically for IIUM (International Islamic University Malaysia) students, staff, and postgraduate users.

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Architecture](#-architecture)
- [Technologies](#-technologies)
- [Installation](#-installation)
- [Usage](#-usage)
- [API Documentation](#-api-documentation)
- [Database Schema](#-database-schema)
- [Contributing](#-contributing)
- [Team](#-team)
- [License](#-license)

## 🎯 Overview

The IIUM Library Booking System is a modern, user-friendly desktop application that allows IIUM community members to book various library facilities including carrel rooms, discussion rooms, and study areas. The system implements role-based access control following IIUM's matriculation number conventions and provides real-time facility status updates.

### Key Characteristics

- **Role-Based Access**: Different privileges for Admin (0*), Staff (1*), Regular Students (2*), and Postgraduate Students (3*)
- **Real-Time Updates**: Facility statuses update immediately after bookings
- **IIUM-Specific**: Tailored to IIUM's matriculation system and facility requirements
- **Modern UI**: Clean, intuitive interface with color-coded status indicators

## ✨ Features

### 🔐 Authentication & Authorization
- **IIUM Matric Number Authentication**: Automatic role assignment based on matric number patterns
- **Role-Based Access Control**: Different booking privileges for different user types
- **Session Management**: Persistent user sessions with automatic cleanup

### 🏢 Facility Management
- **26 Real IIUM Facilities**: Pre-loaded with actual IIUM library rooms and equipment
- **Facility Categories**:
  - Carrel Rooms (Postgraduate Only)
  - Discussion Rooms (Student Access)
  - Study Areas (General Access)
  - Staff Rooms (Staff Only)
- **Real-Time Status Updates**: AVAILABLE, BOOKED, MAINTENANCE, etc.
- **Equipment Tracking**: PCs, projectors, whiteboards, etc.

### 📅 Booking System
- **Flexible Time Slots**: Book facilities for various durations
- **Advance Booking**: 30-minute advance booking rule
- **Conflict Prevention**: Automatic detection of booking conflicts
- **Booking History**: Complete booking management and cancellation
- **Time Validation**: Business hours and facility-specific rules

### 🎨 User Interface
- **Modern JavaFX Design**: Professional, responsive interface
- **Color-Coded Status**: Green (Available), Red (Booked), Orange (Maintenance)
- **Advanced Search & Filtering**: By location, status, type, and equipment
- **Mobile-Responsive Cards**: Facility cards with detailed information
- **Navigation Sidebar**: Easy access to all features

### 🔄 Real-Time Features
- **Live Status Updates**: Facility statuses refresh automatically
- **Manual Refresh**: Refresh button for immediate status updates
- **Automatic Cleanup**: Expired bookings are automatically removed
- **Synchronization**: All UI components stay synchronized

## 🏗️ Architecture

### System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    VIEW LAYER                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  MainApplication.java                              │    │
│  │  ├── MainLayout.java (Navigation & Page Management) │    │
│  │  ├── DashboardPage.java (User Dashboard)           │    │
│  │  ├── FacilitiesPage.java (Facility Browser)        │    │
│  │  ├── FacilityDetailPage.java (Booking Interface)   │    │
│  │  ├── MyBookingsPage.java (Booking Management)      │    │
│  │  └── AdminPanelPage.java (Admin Controls)          │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────┐
│                 SERVICE LAYER                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  AuthService.java (Authentication & Role Management)│    │
│  │  BookingService.java (Booking Logic & Conflict Mgmt)│    │
│  │  BookingPolicy.java (Business Rules & Validation)   │    │
│  │  FacilityService.java (Facility Data Management)    │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────┐
│                  MODEL LAYER                               │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  User.java (User Profile & Bookings)               │    │
│  │  Facility.java (Facility Details & Status)         │    │
│  │  Booking.java (Booking Records)                    │    │
│  │  Equipment.java (Facility Equipment)               │    │
│  │  Room.java (Legacy Room Model)                     │    │
│  │  └── enums/ (Status & Type Enumerations)           │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### Design Patterns Used

- **MVC Pattern**: Clear separation of Model, View, and Controller logic
- **Service Layer Pattern**: Business logic encapsulated in service classes
- **Observer Pattern**: UI components observe model changes
- **Factory Pattern**: Object creation through service methods
- **Strategy Pattern**: Different booking policies for different user types

## 🛠️ Technologies

### Core Technologies
- **Java 21**: Modern Java with latest language features
- **JavaFX 21**: Rich desktop UI framework
- **CSS**: Custom styling for modern appearance

### Development Tools
- **JDK 21**: Java Development Kit
- **JavaFX SDK 21.0.9**: UI framework SDK
- **Gradle/Maven**: Dependency management (optional)
- **Git**: Version control

### Key Libraries
- **javafx-controls**: Core UI controls
- **javafx-fxml**: XML-based UI definition (not used in this project)
- **java.time**: Modern date/time API
- **java.util**: Collections and utilities

## 📦 Installation

### Prerequisites

1. **Java 21 JDK**
   ```bash
   # Download from Oracle or OpenJDK
   # Set JAVA_HOME environment variable
   ```

2. **JavaFX SDK 21.0.9**
   ```bash
   # Download from GluonHQ
   # Extract to project directory or system path
   ```

### Quick Setup (Windows)

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/iium-library-booking-system.git
   cd iium-library-booking-system
   ```

2. **Run the setup script:**
   ```bash
   setup-images.bat  # Downloads required assets
   ```

3. **Start the application:**
   ```bash
   run.bat
   ```

   The application will open with a **login screen** where you can enter your IIUM matric number.

### User Authentication

**Login with your IIUM Matric Number:**
- **Admin**: `0XXXXXX` (Full system access)
- **Staff**: `1XXXXXX` (Staff facilities + general access)
- **Student**: `2XXXXXX` (General student facilities)
- **Postgraduate**: `3XXXXXX` (Postgraduate facilities + general access)

**Quick Login Options (for testing):**
- Click **"Admin (0123456)"** for administrative access
- Click **"Student (2123456)"** for student access
- Click **"Postgrad (3123456)"** for postgraduate access

### Manual Setup

1. **Compile the project:**
   ```bash
   javac --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -d bin src\Main.java src\model\enums\*.java src\model\*.java src\model\services\*.java src\view\components\*.java src\view\pages\*.java src\view\*.java
   ```

2. **Run the application:**
   ```bash
   java --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -cp bin view.MainApplication
   ```

### Project Structure

```
iium-library-booking-system/
├── src/
│   ├── Main.java                    # Legacy entry point
│   ├── view/
│   │   ├── MainApplication.java     # Modern entry point
│   │   ├── MainLayout.java          # Main application layout
│   │   ├── components/
│   │   │   ├── FacilityCard.java    # Facility display cards
│   │   │   └── NavigationSidebar.java # Navigation component
│   │   └── pages/
│   │       ├── DashboardPage.java   # User dashboard
│   │       ├── FacilitiesPage.java  # Facility browser
│   │       ├── FacilityDetailPage.java # Booking interface
│   │       ├── MyBookingsPage.java  # Booking management
│   │       └── AdminPanelPage.java  # Admin controls
│   ├── model/
│   │   ├── User.java               # User model
│   │   ├── Facility.java           # Facility model
│   │   ├── Booking.java            # Booking model
│   │   ├── Equipment.java          # Equipment model
│   │   ├── Room.java               # Legacy room model
│   │   ├── enums/
│   │   │   ├── Role.java           # User roles
│   │   │   ├── FacilityStatus.java # Facility status
│   │   │   ├── FacilityType.java   # Facility types
│   │   │   ├── ReservationPrivilege.java # Access privileges
│   │   │   └── BookingStatus.java  # Booking status
│   │   └── services/
│   │       ├── AuthService.java    # Authentication service
│   │       ├── BookingService.java # Booking management
│   │       ├── BookingPolicy.java  # Business rules
│   │       └── FacilityService.java # Facility management
│   └── styles/
│       └── theme.css               # Application styling
├── bin/                            # Compiled classes
├── javafx-sdk-21.0.9/             # JavaFX runtime
├── run.bat                        # Windows launcher
├── setup-images.bat               # Asset downloader
└── README.md                      # This documentation
```

## 🎮 Usage

### Getting Started

1. **Launch the Application**
   - Run `run.bat` or use manual compilation
   - Application opens with a **login screen** for authentication

2. **Login with IIUM Matric Number**
   ```
   Admin:        0XXXXXX (Full access)
   Staff:        1XXXXXX (Staff facilities + general)
   Student:      2XXXXXX (Student facilities only)
   Postgraduate: 3XXXXXX (Postgraduate facilities + general)
   ```

3. **Quick Login Options** (for testing):
   - Click **"Admin (0123456)"** for administrative access
   - Click **"Student (2123456)"** for student access
   - Click **"Postgrad (3123456)"** for postgraduate access

### Booking a Facility

1. **Navigate to Facilities**
   - Click "Browse Facilities" from dashboard
   - Use search and filters to find suitable rooms

2. **Select a Facility**
   - Click on any facility card to view details
   - Check equipment, capacity, and availability

3. **Make a Booking**
   - Select date and time slots
   - System validates privileges and conflicts
   - Confirm booking

4. **Manage Bookings**
   - View all bookings in "My Bookings"
   - Cancel bookings if needed

### Admin Features

- **Facility Status Management**: Update facility statuses
- **User Management**: View all users and bookings
- **System Monitoring**: Real-time system status

## 📚 API Documentation

### Core Classes

#### User Model
```java
public class User {
    private String matricNo;
    private Role role;
    private List<Booking> myBookings;

    // Methods
    public boolean canBook(Facility facility);
    public void addBooking(Booking booking);
    public void cancelBooking(Booking booking);
}
```

#### Facility Model
```java
public class Facility {
    private String id;
    private String name;
    private FacilityType type;
    private FacilityStatus status;
    private ReservationPrivilege privilege;
    private List<Equipment> equipment;

    // Methods
    public boolean isAvailable();
    public boolean isAccessibleBy(User user);
}
```

#### Booking Model
```java
public class Booking {
    private String bookingID;
    private String userId;
    private String facilityId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;

    // Methods
    public boolean isActive();
    public boolean conflictsWith(Booking other);
}
```

### Service Classes

#### AuthService
```java
public class AuthService {
    public static User login(String matricNo, String password);
    public static Role determineRole(String matricNo);
    public static boolean hasAdminPrivileges(User user);
}
```

#### BookingService
```java
public class BookingService {
    public static Booking createBooking(User user, Facility facility,
                                      LocalDateTime start, LocalDateTime end);
    public static boolean cancelBooking(User user, Booking booking);
    public static boolean hasBookingConflict(String facilityId,
                                           LocalDateTime start, LocalDateTime end);
    public static void cleanupExpiredBookings();
}
```

#### BookingPolicy
```java
public class BookingPolicy {
    public static boolean canBook(User user, Facility facility,
                                LocalDateTime startTime, LocalDateTime endTime);
    public static String validateBooking(User user, Facility facility,
                                       LocalDateTime startTime, LocalDateTime endTime);
}
```

### Enums

#### Role
```java
public enum Role {
    ADMIN,      // Full system access
    STAFF,      // Staff facilities + general
    STUDENT     // Student facilities only
}
```

#### FacilityStatus
```java
public enum FacilityStatus {
    AVAILABLE,           // Ready for booking
    BOOKED,             // Currently reserved
    MAINTENANCE,        // Under maintenance
    TEMPORARILY_CLOSED, // Closed temporarily
    RESERVED           // Reserved for special use
}
```

#### ReservationPrivilege
```java
public enum ReservationPrivilege {
    OPEN,               // Anyone can book
    STUDENT_ONLY,       // Students only
    STAFF_ONLY,         // Staff only
    POSTGRADUATE_ONLY,  // Postgraduate students only
    LIBRARY_USE_ONLY,   // Library staff only
    SPECIAL_NEEDS_ONLY, // Special needs access
    BOOK_VENDORS_ONLY   // Book vendors only
}
```

## 💾 Database Schema

The system uses in-memory data storage (no external database required). Data structures:

### Users Collection
```json
{
  "matricNo": "3123456",
  "role": "STUDENT",
  "myBookings": ["booking-001", "booking-002"]
}
```

### Facilities Collection
```json
{
  "id": "CR-L1-01",
  "name": "Carrel Room L1-01",
  "type": "CARREL_ROOM",
  "status": "AVAILABLE",
  "privilege": "POSTGRADUATE_ONLY",
  "location": "Level 1",
  "capacity": 1,
  "equipment": ["PC", "Chair", "Table"]
}
```

### Bookings Collection
```json
{
  "bookingID": "BK-20250121-001",
  "userId": "3123456",
  "facilityId": "CR-L1-01",
  "startTime": "2026-01-21T09:00:00",
  "endTime": "2026-01-21T10:00:00",
  "status": "ACTIVE"
}
```

## 🤝 Contributing

### Development Setup

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Make your changes**
4. **Test thoroughly**
5. **Submit a pull request**

### Code Standards

- **Java Naming Conventions**: Follow standard Java naming
- **Documentation**: Add JavaDoc comments for public methods
- **Error Handling**: Use appropriate exception handling
- **Testing**: Test all user roles and edge cases

### Adding New Features

1. **Facility Types**: Add to `FacilityType` enum and update UI
2. **User Roles**: Modify `AuthService.determineRole()` and `BookingPolicy`
3. **Business Rules**: Update `BookingPolicy` class
4. **UI Components**: Follow existing component patterns

## 👥 Team

**Group 5 - IIUM Coursework Project**

### Team Members
- **Ibrahim Bin Nasrum (2116467)**
  - **Component**: Booking Management (Make/Cancel/View)
  - **Responsibilities**: Booking logic, conflict detection, booking history

- **Muhammad Izwan Bin Muhammad Isham (2428113)**
  - **Component**: Login & Main Navigation
  - **Responsibilities**: Authentication, session management, UI navigation

- **Mohammad Amir Imtiyaz Bin Mohd Annuar (2212545)**
  - **Component**: Room List & Availability
  - **Responsibilities**: Facility management, search/filter, status updates

### Project Timeline
- **Start Date**: September 2024
- **Completion Date**: January 2025
- **Technologies**: Java 21, JavaFX 21, CSS
- **Development Environment**: Windows 11, VS Code

## 📄 License

This project is developed as part of IIUM coursework assignment.
All rights reserved to the development team.

### Academic Usage
- For educational purposes only
- Not intended for commercial use
- May be used as reference for similar projects

## 🐛 Known Issues & Future Enhancements

### Current Limitations
- In-memory data storage (no persistence)
- No concurrent user support
- Limited admin features
- No email notifications

### Planned Features
- Database integration (MySQL/PostgreSQL)
- Multi-user concurrent access
- Email/SMS notifications
- Mobile app companion
- Advanced analytics dashboard
- REST API for integrations

## 📞 Support

For questions or issues related to this project:

1. **Check the documentation** above
2. **Review the code comments** in source files
3. **Test with different user roles** to understand behavior
4. **Contact team members** for specific component questions

---

**Project**: IIUM Library Booking System  
**Version**: 1.0.0  
**Date**: January 2025  
**Team**: Group 5 - IIUM Coursework Project
