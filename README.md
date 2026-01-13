# 🏛️ IIUM Library Booking System

A fully integrated JavaFX-based library room booking system for IIUM (International Islamic University Malaysia).

## ✨ Features

- 🔐 **User Authentication** - Login with matric number and password
- 📋 **Room Listing** - View all available rooms with details
- 🔍 **Room Filtering** - Filter by availability status
- 📅 **Booking Management** - Create, view, and cancel bookings
- ⏰ **Real-time Availability** - Instant room status updates
- 📊 **Booking History** - Track all your bookings
- 🎨 **Intuitive UI** - Clean, modern interface with color-coded status

## 🚀 Quick Start

### Prerequisites
- Java 11 or higher
- JavaFX SDK 21.0.9

### Running the System

1. **Compile:**
```powershell
$JFX="C:\Program Files\javafx\javafx-sdk-21.0.9\lib"
javac --module-path "$JFX" --add-modules javafx.controls -d bin src\model\*.java src\GUI\*.java src\Main.java
```

2. **Run:**
```powershell
java --module-path "$JFX" --add-modules javafx.controls -cp bin Main
```

Or simply use the provided scripts: `run.bat` (Windows) or `run.sh` (Linux/Mac)

## 📖 Documentation

- **[SETUP_AND_RUN.md](SETUP_AND_RUN.md)** - Complete setup and installation guide
- **[USER_GUIDE.md](USER_GUIDE.md)** - How to use the system
- **[SYSTEM_FLOW.md](SYSTEM_FLOW.md)** - System architecture and data flow
- **[INTEGRATION_COMPLETE.md](INTEGRATION_COMPLETE.md)** - Integration summary

## 🏗️ System Architecture

```
Login → Room List View → Booking View
  ↓           ↓              ↓
SessionManager (Manages user & data)
  ↓           ↓              ↓
Model Classes (User, Room, Booking)
```

## 📂 Project Structure

```
iium-library-booking-system/
├── src/
│   ├── model/           # Data models
│   │   ├── User.java
│   │   ├── Room.java
│   │   ├── Booking.java
│   │   └── SessionManager.java
│   ├── GUI/             # User interface
│   │   ├── Login.java
│   │   ├── RoomListView.java
│   │   └── BookingView.java
│   └── Main.java        # Application entry point
├── bin/                 # Compiled classes
├── run.bat             # Windows run script
├── run.sh              # Linux/Mac run script
└── *.md                # Documentation
```

## 🎯 How It Works

1. **Login** - Enter your credentials
2. **Browse Rooms** - View available rooms
3. **Select Room** - Choose your preferred room
4. **Make Booking** - Select date/time and confirm
5. **Manage** - View or cancel your bookings

## 👥 Team

Developed as part of IIUM coursework project

## 📝 License

Educational project for IIUM
  - Status (Active/Cancelled)

### Project Structure
```
src/
├── Main.java                  # Application entry point
├── model/
│   ├── Room.java             # Room model class
│   ├── Booking.java          # Booking model class
│   └── User.java             # User model with booking methods
└── view/
    └── BookingView.java      # Main JavaFX GUI for booking management
```

### How to Run

#### Prerequisites
- JDK 11 or higher with JavaFX
- JavaFX SDK installed

#### Compile and Run
```bash
# Navigate to the project directory
cd src

# Compile (if JavaFX is in your JDK)
javac Main.java

# Run
java Main
```

#### Using JavaFX SDK separately
If JavaFX is not bundled with your JDK:
```bash
# Compile
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls Main.java

# Run
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls Main
```

### Demo Features
- Pre-loaded with 5 sample rooms (R001-R005)
- Demo user: Matric No: 2112345
- All room operations (book/cancel) update availability in real-time
- Booking counter generates unique booking IDs automatically

### Minimum Interaction Requirements (Met ✓)
✓ Click "Book Now" → Booking appears in "My Bookings" table + Room availability updates
✓ Click "Cancel" → Booking status changes to "Cancelled" + Room becomes available

### Notes
- This is a standalone component for assignment demonstration
- Does not require integration with team members' components (Login/RoomList)
- All data is stored in memory (no database)
- User.makeBooking() and User.cancelBooking() methods are implemented and working

---
**Student**: Ibrahim  
**Course**: [Your Course Code]  
**Date**: January 4, 2026
