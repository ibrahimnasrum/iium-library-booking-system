# IIUM Library Booking System - User Guide

## System Flow

The system has been fully integrated to work end-to-end:

### 1. Login Screen
- Enter your **Matric Number** (e.g., 20241234)
- Enter your **Password** (any non-empty text for demo)
- Click **LOGIN**
- Upon successful login, you'll be redirected to the Room List View

### 2. Room List View
- View all available rooms with their details:
  - Room ID
  - Type (Study Room, Discussion Room, etc.)
  - Capacity
  - Location
  - Availability Status (color-coded: green = Available, red = Booked)
- **Show Available** button: Filters to show only available rooms
- **Refresh List** button: Refreshes the room list from the system
- **Proceed to Booking** button: Opens the booking view (enabled when you select a room)
  - Select a room from the table
  - Click "Proceed to Booking" to book that room

### 3. Booking View
- The selected room is pre-selected in the dropdown
- Choose your booking details:
  - **Room**: Select from available rooms
  - **Start Date & Time**: Choose start date and time (30-minute intervals)
  - **End Date & Time**: Choose end date and time
- **Book Now** button: Creates the booking
  - System validates that end time is after start time
  - System checks room availability
  - Room status is updated to "Booked"
  - Booking appears in your "My Bookings" table
- **Cancel Booking** button: Cancels a selected booking
  - Select a booking from the "My Bookings" table
  - Click "Cancel Booking"
  - Room becomes available again
  - Booking status changes to "Cancelled"

### 4. My Bookings Table
- View all your bookings with:
  - Booking ID
  - Room ID
  - Start date/time
  - End date/time
  - Status (Active/Cancelled)

## Running the System

### Option 1: Run Main.java
```bash
java src/Main.java
```

### Option 2: Run Login.java directly
```bash
java src/GUI/Login.java
```

## Features Implemented

✅ Login with matric number and password
✅ Session management (logged-in user persists across views)
✅ Room list view with filtering
✅ Room availability checking
✅ Booking creation with validation
✅ Booking cancellation
✅ Real-time room status updates
✅ User-specific booking history
✅ Seamless navigation between views

## Sample Rooms

The system includes 5 pre-configured rooms:
- R001: Study Room (4 capacity, Level 1)
- R002: Discussion Room (6 capacity, Level 2)
- R003: Meeting Room (8 capacity, Level 3)
- R004: Private Room (2 capacity, Level 1)
- R005: Conference Room (12 capacity, Level 4)

## Technical Details

### Classes Structure:
- **model/User.java**: User authentication and booking management
- **model/Room.java**: Room information and availability
- **model/Booking.java**: Booking details and status
- **model/SessionManager.java**: Global session and data management
- **GUI/Login.java**: Login screen
- **GUI/RoomListView.java**: Room listing and selection
- **GUI/BookingView.java**: Booking creation and management
- **Main.java**: Application entry point

### Data Flow:
1. User logs in → User object created → Stored in SessionManager
2. RoomListView loads rooms from SessionManager
3. User selects room → BookingView opens with pre-selected room
4. User makes booking → User.makeBooking() updates Room availability
5. Bookings stored in User object → Displayed in table
6. Cancel booking → Room availability restored
