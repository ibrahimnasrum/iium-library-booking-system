# IIUM Library Booking System

A JavaFX-based library room booking system for IIUM (International Islamic University Malaysia).

## Team Members
- **Izwan**: Login & Main Navigation
- **Amir**: Room List & Availability
- **Ibrahim**: Booking Management (Make/Cancel/View)

## Ibrahim's Part - Booking Management

This repository contains Ibrahim's component: **BookingView.java**

### Features Implemented
✅ **Booking Form**
- Select room from dropdown (ComboBox)
- Choose start date & time (DatePicker + ComboBox)
- Choose end date & time (DatePicker + ComboBox)

✅ **Buttons**
- **Book Now**: Creates a new booking and updates room availability
- **Cancel Booking**: Cancels selected booking and makes room available again
- **Refresh**: Reloads all data

✅ **My Bookings Display**
- TableView showing all bookings with:
  - Booking ID
  - Room ID
  - Start Time
  - End Time
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
