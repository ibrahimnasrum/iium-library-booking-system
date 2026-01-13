# IIUM Library Booking System - Setup and Run Instructions

## ✅ Integration Complete!

The library booking system has been fully integrated with the following flow:
1. **Login Screen** → Enter matric number and password
2. **Room List View** → View and select available rooms
3. **Booking View** → Create and manage bookings
4. **Check Availability** → Real-time room availability updates

## 📋 Prerequisites

This is a **JavaFX application**. You need to have JavaFX installed to run it.

### Install JavaFX

#### Option 1: Download JavaFX SDK
1. Download JavaFX SDK from: https://gluonhq.com/products/javafx/
2. Extract it to a location (e.g., `C:\javafx-sdk-21`)
3. Note the path to the `lib` folder (e.g., `C:\javafx-sdk-21\lib`)

#### Option 2: Use Maven/Gradle (Recommended for production)
Add JavaFX dependencies to your `pom.xml` or `build.gradle`

## 🚀 How to Run

### Method 1: Using Command Line with JavaFX

**On Windows:**
```powershell
# Set JavaFX path (adjust to your installation)
$FX_PATH = "C:\javafx-sdk-21\lib"

# Compile
javac --module-path $FX_PATH --add-modules javafx.controls,javafx.fxml -d bin src/model/*.java src/GUI/*.java src/Main.java

# Run
java --module-path $FX_PATH --add-modules javafx.controls,javafx.fxml -cp bin Main
```

**On Linux/Mac:**
```bash
# Set JavaFX path
FX_PATH="/path/to/javafx-sdk/lib"

# Compile
javac --module-path $FX_PATH --add-modules javafx.controls,javafx.fxml -d bin src/model/*.java src/GUI/*.java src/Main.java

# Run
java --module-path $FX_PATH --add-modules javafx.controls,javafx.fxml -cp bin Main
```

### Method 2: Using an IDE (Easiest)

#### IntelliJ IDEA:
1. Open the project in IntelliJ IDEA
2. Go to File → Project Structure → Libraries
3. Add JavaFX library (download from https://gluonhq.com/products/javafx/)
4. Right-click on `Main.java` → Run 'Main.main()'

#### Eclipse:
1. Open the project in Eclipse
2. Right-click project → Build Path → Configure Build Path
3. Add External JARs from JavaFX lib folder
4. Right-click `Main.java` → Run As → Java Application

#### VS Code:
1. Install "Extension Pack for Java" extension
2. Install "JavaFX Support" extension
3. Download JavaFX SDK
4. Add VM arguments in launch.json:
```json
{
    "vmArgs": "--module-path \"C:/javafx-sdk-21/lib\" --add-modules javafx.controls,javafx.fxml"
}
```
5. Run Main.java

### Method 3: Create a Run Script

**Windows (run.ps1):**
```powershell
$FX_PATH = "C:\javafx-sdk-21\lib"  # Adjust this path
javac --module-path $FX_PATH --add-modules javafx.controls,javafx.fxml -d bin src/model/*.java src/GUI/*.java src/Main.java
java --module-path $FX_PATH --add-modules javafx.controls,javafx.fxml -cp bin Main
```

**Linux/Mac (run.sh):**
```bash
#!/bin/bash
FX_PATH="/path/to/javafx-sdk/lib"  # Adjust this path
javac --module-path $FX_PATH --add-modules javafx.controls,javafx.fxml -d bin src/model/*.java src/GUI/*.java src/Main.java
java --module-path $FX_PATH --add-modules javafx.controls,javafx.fxml -cp bin Main
```

## 📱 How to Use the System

### Step 1: Login
1. Launch the application
2. Enter any **Matric Number** (e.g., "20241234")
3. Enter any **Password** (any text works for demo)
4. Click **LOGIN**

### Step 2: View and Select Room
1. You'll see the **Room List View** with all available rooms
2. Room details shown: ID, Type, Capacity, Location, Availability
3. Use **Show Available** button to filter available rooms only
4. Click **Refresh List** to update the room list
5. **Select a room** from the table
6. Click **Proceed to Booking** button

### Step 3: Make a Booking
1. The **Booking View** opens with your selected room pre-selected
2. Choose **Start Date** and **Start Time**
3. Choose **End Date** and **End Time**
4. Click **Book Now**
5. Your booking appears in the "My Bookings" table below
6. Room availability is updated automatically

### Step 4: Check Availability
- Availability is shown next to the room selection
- Green = Available
- Red = Booked
- The "My Bookings" table shows all your bookings with:
  - Booking ID
  - Room ID
  - Start/End times
  - Status (Active/Cancelled)

### Step 5: Cancel a Booking (Optional)
1. Select a booking from the "My Bookings" table
2. Click **Cancel Booking**
3. Room becomes available again
4. Booking status changes to "Cancelled"

## 🏗️ System Architecture

### Files Created/Modified:

**Model Classes:**
- `src/model/User.java` - User authentication and booking management
- `src/model/Room.java` - Room information
- `src/model/Booking.java` - Booking details
- `src/model/SessionManager.java` - ✨ **NEW** - Global session management

**GUI Classes:**
- `src/GUI/Login.java` - ✅ **UPDATED** - Now integrates with SessionManager
- `src/GUI/RoomListView.java` - ✅ **UPDATED** - Fixed and integrated with model
- `src/GUI/BookingView.java` - ✅ **UPDATED** - Fully integrated with model classes

**Main:**
- `src/Main.java` - ✅ **UPDATED** - Entry point, launches Login screen

### Data Flow:
```
Login.java
    ↓ (authenticate user)
SessionManager (stores current user & rooms)
    ↓ (user logged in)
RoomListView.java
    ↓ (select room)
BookingView.java
    ↓ (make booking)
User.makeBooking() → Updates Room availability
    ↓
Display updated bookings & availability
```

## ✨ Features Implemented

- ✅ **End-to-end integration** from login to booking
- ✅ **Session management** - User persists across views
- ✅ **Login validation** - Matric number and password required
- ✅ **Room listing** - View all rooms with details
- ✅ **Room filtering** - Show only available rooms
- ✅ **Room selection** - Pre-select room from list view
- ✅ **Booking creation** - Create bookings with validation
- ✅ **Booking cancellation** - Cancel bookings and free rooms
- ✅ **Real-time availability** - Room status updates immediately
- ✅ **Booking history** - View all user bookings
- ✅ **Status color coding** - Visual feedback for availability

## 📦 Sample Data

The system includes 5 pre-configured rooms:
- **R001**: Study Room (4 capacity, Level 1)
- **R002**: Discussion Room (6 capacity, Level 2)
- **R003**: Meeting Room (8 capacity, Level 3)
- **R004**: Private Room (2 capacity, Level 1)
- **R005**: Conference Room (12 capacity, Level 4)

## 🐛 Troubleshooting

### "Error: JavaFX runtime components are missing"
- Install JavaFX SDK
- Add `--module-path` and `--add-modules` flags when running

### "Cannot find symbol: Application"
- JavaFX not in classpath
- Use the compile/run commands with JavaFX module path

### "Class not found: Main"
- Make sure you compiled the files first
- Check that `bin` directory exists and contains compiled .class files

### Window doesn't open
- Check Java version (Java 11+ recommended for JavaFX)
- Ensure JavaFX modules are loaded correctly

## 📝 Notes

- This is a **demo/prototype system** with simplified authentication
- In production, you would:
  - Connect to a real database
  - Implement proper password hashing
  - Add user registration
  - Implement proper date/time conflict checking
  - Add more validation and error handling
  - Implement persistent storage

## 🎯 Next Steps for Production

1. Add database integration (MySQL/PostgreSQL)
2. Implement proper authentication with password hashing
3. Add user roles (Student, Staff, Admin)
4. Implement booking conflict detection
5. Add email notifications
6. Create admin panel for room management
7. Add booking history and reports
8. Implement proper logging
9. Add unit tests
10. Deploy as a standalone application

---

**System successfully integrated! All components now work together end-to-end.** 🎉
