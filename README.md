# 🏛️ IIUM Library Booking System

A fully integrated JavaFX-based library room booking system for IIUM (International Islamic University Malaysia) with professional authentication and comprehensive facility management.

## 🔐 Authentication System
- **IIUM Matric Number Authentication**: Secure login with matric number validation
- **Role-Based Access Control**: Automatic role assignment based on matric number patterns
- **Professional Login Screen**: Modern UI with error handling and user feedback
- **Quick Login Buttons**: Easy testing with Admin, Student, and Postgraduate roles

## ✨ Core Features
- 📋 **Facility Management** - View all 26 IIUM library facilities with real-time status
- 🔍 **Advanced Search & Filtering** - Search by facility ID, type, location, or equipment
- 📅 **Booking Engine** - Create, view, and cancel bookings with conflict detection
- ⏰ **Real-time Updates** - Instant facility status synchronization
- 📊 **Booking History** - Track all your bookings with detailed information
- 🎨 **Modern JavaFX UI** - Clean, intuitive interface with color-coded status
- 👑 **Admin Panel** - System management, monitoring, and user oversight

## 🚀 Quick Start

### Prerequisites
- Java 21 JDK
- JavaFX SDK 21.0.9 (bundled in project)

### Running the System

Use the provided `run.bat` script for Windows:

```cmd
run.bat
```

Or compile and run manually:

1. **Compile:**
```powershell
javac --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -d bin src\Main.java src\model\*.java src\model\services\*.java src\view\*.java src\view\pages\*.java
```

2. **Run:**
```powershell
java --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -cp bin Main
```

## 🎯 How to Use the Login System

### Launch
Run `run.bat` or compile manually as shown above.

### Login Screen
- Enter your IIUM matric number (e.g., 3123456 for postgraduate)
- Enter password (default: "password" for testing)

### Quick Test Logins
Use these matric numbers for testing different roles:
- **Admin (0123456)**: Full system access and management
- **Student (2123456)**: Student facilities only
- **Postgrad (3123456)**: Postgraduate + general facilities

### User Roles
- **0 = Admin**: Complete system access, user management, facility oversight
- **1 = Staff**: Limited administrative access
- **2 = Student**: Access to student-specific facilities
- **3 = Postgraduate**: Access to postgraduate and general facilities

## 🏗️ System Architecture

```
Main.java → MainApplication.java
    ↓              ↓
LoginPage (Authentication Layer)
    ↓              ↓
MainLayout (Main UI Container)
    ↓              ↓
DashboardPage, FacilitiesPage, BookingPage, AdminPanelPage
    ↓              ↓
AuthService, BookingService, FacilityService (Business Logic)
    ↓              ↓
User, Facility, Booking, Equipment (Data Models)
```

## 📂 Project Structure

```
iium-library-booking-system/
├── src/
│   ├── Main.java                    # Application entry point
│   ├── model/                       # Data models and services
│   │   ├── User.java               # User management
│   │   ├── Facility.java           # Facility data with IIUM details
│   │   ├── Booking.java            # Booking record management
│   │   ├── Equipment.java          # Equipment tracking
│   │   ├── SessionManager.java     # Session management
│   │   ├── enums/                  # System enumerations
│   │   └── services/               # Business logic services
│   │       ├── AuthService.java    # Authentication & authorization
│   │       ├── BookingService.java # Booking operations
│   │       ├── FacilityService.java # Facility management
│   │       └── BookingPolicy.java  # Booking rules & validation
│   └── view/                        # User interface components
│       ├── MainApplication.java    # JavaFX application launcher
│       ├── MainLayout.java         # Main UI layout with navigation
│       ├── components/             # Reusable UI components
│       └── pages/                  # Application pages
│           ├── LoginPage.java      # Authentication screen
│           ├── DashboardPage.java  # User dashboard
│           ├── FacilitiesPage.java # Facility browsing
│           ├── FacilityDetailPage.java # Facility details
│           ├── BookingPage.java    # Booking management
│           ├── AdminPanelPage.java # Administrative functions
│           └── MyBookingsPage.java # User's booking history
├── tools/                          # Development and testing tools
│   ├── AuthTest.java              # Authentication testing
│   ├── AutoLoginTest.java         # Automated login testing
│   └── RunModelTest.java          # Model validation
├── javafx-sdk-21.0.9/             # Bundled JavaFX SDK
├── run.bat                        # Windows execution script
├── setup-images.bat               # Image setup script
└── documentation/                 # Project documentation
    ├── README.md                  # This file
    ├── QUICK_START.md             # Fast setup guide
    ├── USER_MANUAL.md             # End-user guide
    ├── API_DOCUMENTATION.md       # API reference
    └── DEVELOPMENT_GUIDE.md       # Developer documentation
```

## 🎯 System Flow

1. **Authentication** - User logs in with IIUM matric number
2. **Role Assignment** - System determines access level from matric prefix
3. **Dashboard** - User sees personalized dashboard based on role
4. **Facility Browsing** - Search and filter available facilities
5. **Booking Management** - Create bookings with conflict checking
6. **Real-time Updates** - Status changes reflect immediately across UI

## 👥 Team Members & Contributions

**Group 5 - IIUM Coursework Project**
- **Muhammad Izwan Bin Muhammad Isham (2428113)**: Login & Main Navigation, Authentication System
- **Mohammad Amir Imtiyaz Bin Mohd Annuar (2212545)**: Room/Facility List & Availability, Search & Filtering
- **Ibrahim Bin Nasrum (2116467)**: Booking Management (Make/Cancel/View), Booking Engine

## 📊 Key Technical Features

- **Authentication System**: IIUM matric-based role assignment
- **Booking Engine**: Real-time conflict detection and validation
- **Facility Management**: 26 IIUM facilities with status tracking
- **UI Components**: Modern JavaFX interface with refresh capabilities
- **Admin Panel**: System management and monitoring
- **Search & Filter**: Advanced facility discovery
- **Real-time Updates**: Status synchronization across components

## 📝 Documentation

- **[QUICK_START.md](QUICK_START.md)** - 5-minute setup and test scenarios
- **[USER_MANUAL.md](USER_MANUAL.md)** - Step-by-step user guide
- **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** - Complete API reference
- **[DEVELOPMENT_GUIDE.md](DEVELOPMENT_GUIDE.md)** - Developer documentation

## 🧪 Testing

The system includes comprehensive testing tools:
- `tools/AuthTest.java` - Authentication service testing
- `tools/AutoLoginTest.java` - Automated login verification
- `tools/RunModelTest.java` - Model validation

## 📝 Notes

- Pre-loaded with 26 real IIUM library facilities
- All booking operations include real-time conflict detection
- Data is stored in memory (no external database required)
- Professional authentication layer ensures secure access
- Role-based permissions control feature access

---
**Project**: IIUM Library Booking System  
**Technology**: Java 21 + JavaFX 21.0.9  
**Date**: January 2025
