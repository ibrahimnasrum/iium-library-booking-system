# 🏛️ IIUM Library Booking System

A fully integrated JavaFX-based library room booking system for IIUM (International Islamic University Malaysia).

## Team Members Contribution
- **Izwan**: Login & Main Navigation
- **Amir**: Room List & Availability
- **Ibrahim**: Booking Management (Make/Cancel/View)

## ✨ Features
- 📋 **Room Listing** - View all 26 IIUM library rooms with details
- 🔍 **Advanced Search & Filtering** - Search by room ID, type, location, or equipment
- 📅 **Booking Management** - Create, view, and cancel bookings with date/time selection
- ⏰ **Real-time Availability** - Instant room status updates
- 📊 **Booking History** - Track all your bookings
- 🎨 **Intuitive UI** - Clean, modern interface with color-coded status

## 🚀 Quick Start

### Prerequisites
- Java 21 JDK
- JavaFX SDK 21.0.9

### Running the System

Use the provided `run.bat` script for Windows:

```cmd
run.bat
```

Or compile and run manually:

1. **Compile:**
```powershell
javac --module-path "C:\Program Files\javafx\javafx-sdk-21.0.9\lib" --add-modules javafx.controls -d . src\Main.java src\model\*.java src\view\*.java
```

2. **Run:**
```powershell
java --module-path "C:\Program Files\javafx\javafx-sdk-21.0.9\lib" --add-modules javafx.controls Main
```

## 🏗️ System Architecture

```
Main.java → BookingView.java
    ↓              ↓
SessionManager (Manages user & data)
    ↓              ↓
Model Classes (User, Room, Booking)
```

## 📂 Project Structure

```
iium-library-booking-system/
├── src/
│   ├── Main.java              # Application entry point
│   ├── model/                 # Data models
│   │   ├── User.java         # User management & booking operations
│   │   ├── Room.java         # Room data with IIUM library details
│   │   ├── Booking.java      # Booking record management
│   │   └── SessionManager.java # Session management
│   └── view/                  # User interface
│       └── BookingView.java  # Main JavaFX GUI for booking management
├── run.bat                    # Windows run script
└── README.md                  # This documentation
```

## 🎯 How It Works

1. **Browse Rooms** - View all 26 IIUM library rooms with equipment and capacity details
2. **Search & Filter** - Find rooms by type, location, or availability
3. **Make Booking** - Select room, date, and time slot
4. **Manage Bookings** - View your bookings and cancel if needed

## 👥 Team

**Group 5 - IIUM Coursework Project**
- **Ibrahim Bin Nasrum (2116467)**: Booking Management (Make/Cancel/View)
- **Muhammad Izwan Bin Muhammad Isham (2428113)**: Login & Main Navigation
- **Mohammad Amir Imtiyaz Bin Mohd Annuar (2212545)**: Room List & Availability

## 📝 Notes

- This is Ibrahim's booking component for assignment demonstration
- Pre-loaded with 26 real IIUM library rooms
- All booking operations update room availability in real-time
- Data is stored in memory (no database required)

---
**Student**: Ibrahim Bin Nasrum (2116467)  
**Component**: Booking Management  
**Date**: January 2025
