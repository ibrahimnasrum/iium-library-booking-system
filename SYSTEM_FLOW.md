# IIUM Library Booking System - System Flow Diagram

## Complete Integration Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                     APPLICATION START                            │
│                         Main.java                                │
│                           ↓                                      │
│                   Launch Login.java                              │
└─────────────────────────────────────────────────────────────────┘
                             ↓
┌─────────────────────────────────────────────────────────────────┐
│                      LOGIN SCREEN                                │
│                      (Login.java)                                │
│                                                                  │
│  ┌────────────────────────────────────────────────┐             │
│  │  Enter Matric Number: [20241234______]         │             │
│  │  Enter Password:      [********______]         │             │
│  │                                                 │             │
│  │             [LOGIN BUTTON]                     │             │
│  └────────────────────────────────────────────────┘             │
│                           ↓                                      │
│                  Validate Credentials                            │
│                  User.login(matric, pass)                        │
│                           ↓                                      │
│            SessionManager.setCurrentUser(user)                   │
└─────────────────────────────────────────────────────────────────┘
                             ↓
                    [Login Successful]
                             ↓
┌─────────────────────────────────────────────────────────────────┐
│                   ROOM LIST VIEW                                 │
│                (RoomListView.java)                               │
│                                                                  │
│  ┌──────────────────────────────────────────────────────┐       │
│  │ Room ID │ Type            │ Capacity │ Location │ Status│    │
│  ├──────────────────────────────────────────────────────┤       │
│  │ R001    │ Study Room      │ 4        │ Level 1  │ ✓ Available│
│  │ R002    │ Discussion Room │ 6        │ Level 2  │ ✓ Available│
│  │ R003    │ Meeting Room    │ 8        │ Level 3  │ ✗ Booked   │
│  │ R004    │ Private Room    │ 2        │ Level 1  │ ✓ Available│
│  │ R005    │ Conference Room │ 12       │ Level 4  │ ✓ Available│
│  └──────────────────────────────────────────────────────┘       │
│                                                                  │
│  [Show Available] [Refresh List] [Proceed to Booking]           │
│                                                                  │
│  Data loaded from: SessionManager.getAllRooms()                 │
└─────────────────────────────────────────────────────────────────┘
                             ↓
                   [Select Room & Proceed]
                             ↓
┌─────────────────────────────────────────────────────────────────┐
│                     BOOKING VIEW                                 │
│                  (BookingView.java)                              │
│                                                                  │
│  ┌────────────────────────────────────────────────┐             │
│  │ Room: [R001 (Study Room)     ▼] Available ✓   │             │
│  │                                                 │             │
│  │ Start: [2026-01-15] [09:00 ▼]                 │             │
│  │ End:   [2026-01-15] [11:00 ▼]                 │             │
│  │                                                 │             │
│  │     [Book Now]  [Cancel Booking]               │             │
│  └────────────────────────────────────────────────┘             │
│                           ↓                                      │
│                    [Book Now Clicked]                            │
│                           ↓                                      │
│         User.makeBooking(room, startTime, endTime)              │
│                           ↓                                      │
│              Room.setAvailabilityStatus("Booked")               │
│                           ↓                                      │
│          Booking added to User.myBookings list                   │
│                                                                  │
│  ┌──────────────────────────────────────────────────────┐       │
│  │                   MY BOOKINGS                         │       │
│  ├──────────────────────────────────────────────────────┤       │
│  │ ID   │ Room │ Start            │ End              │ Status│  │
│  ├──────────────────────────────────────────────────────┤       │
│  │ B001 │ R001 │ 2026-01-15 09:00 │ 2026-01-15 11:00 │ Active │  │
│  │ B002 │ R002 │ 2026-01-16 14:00 │ 2026-01-16 16:00 │ Active │  │
│  └──────────────────────────────────────────────────────┘       │
│                                                                  │
│  Select booking & click [Cancel Booking] to cancel              │
│                           ↓                                      │
│              User.cancelBooking(booking)                         │
│                           ↓                                      │
│         Room.setAvailabilityStatus("Available")                 │
└─────────────────────────────────────────────────────────────────┘
```

## Data Flow Architecture

```
┌──────────────────────────────────────────────────────────────┐
│                    SESSION MANAGER                            │
│              (model/SessionManager.java)                      │
│                                                               │
│  Stores:                                                      │
│  • Current logged-in User                                     │
│  • List of all Rooms                                          │
│  • Global application state                                   │
│                                                               │
│  Singleton pattern - accessed from all GUI classes           │
└──────────────────────────────────────────────────────────────┘
         ↑                    ↑                    ↑
         │                    │                    │
   ┌─────┴──────┐    ┌────────┴────────┐   ┌──────┴───────┐
   │  Login.java │    │ RoomListView    │   │  BookingView │
   │            │    │     .java       │   │    .java     │
   └────────────┘    └─────────────────┘   └──────────────┘
         │                    │                    │
         └────────────────────┴────────────────────┘
                              ↓
                    ┌──────────────────┐
                    │   MODEL CLASSES  │
                    ├──────────────────┤
                    │  • User.java     │
                    │  • Room.java     │
                    │  • Booking.java  │
                    └──────────────────┘
```

## Key Integration Points

### 1. Login → RoomListView
- **Trigger**: Successful login (matric & password validation)
- **Action**: 
  - Create User object: `User.login(matric, password)`
  - Store in session: `SessionManager.getInstance().setCurrentUser(user)`
  - Open RoomListView window
  - Close Login window

### 2. RoomListView → BookingView
- **Trigger**: User selects room and clicks "Proceed to Booking"
- **Action**:
  - Get selected Room from table
  - Create BookingView with selected room: `new BookingView(selectedRoom)`
  - Open BookingView window
  - Room is pre-selected in dropdown

### 3. BookingView → Model Updates
- **Book Action**:
  - Get current user: `SessionManager.getInstance().getCurrentUser()`
  - Validate inputs (dates, times, room selection)
  - Create booking: `currentUser.makeBooking(room, startTime, endTime)`
  - Update room status: `room.setAvailabilityStatus("Booked")`
  - Refresh displays

- **Cancel Action**:
  - Get selected booking from table
  - Cancel booking: `currentUser.cancelBooking(booking)`
  - Update room status: `room.setAvailabilityStatus("Available")`
  - Refresh displays

### 4. Real-time Availability Checking
- **When**: On room selection change
- **How**: Check `room.getAvailabilityStatus()`
- **Display**: Color-coded label (Green=Available, Red=Booked)

## File Dependencies

```
Main.java
  └─→ Login.java
        ├─→ SessionManager.java
        ├─→ User.java (login validation)
        └─→ RoomListView.java
              ├─→ SessionManager.java (get rooms)
              ├─→ Room.java (display)
              └─→ BookingView.java
                    ├─→ SessionManager.java (get user & rooms)
                    ├─→ User.java (makeBooking, cancelBooking)
                    ├─→ Room.java (availability)
                    └─→ Booking.java (booking details)
```

## Summary

✅ **End-to-end integration complete**
✅ **Session management implemented**
✅ **All GUIs connected with data flow**
✅ **Real-time availability checking**
✅ **Booking creation and cancellation**
✅ **User-specific booking history**

The system now works seamlessly from login through booking!
