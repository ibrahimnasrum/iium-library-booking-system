# 📖 IIUM Library Booking System - User Manual

## Welcome to the IIUM Library Booking System

This user manual will guide you through all the features and functionality of the IIUM Library Booking System, designed specifically for IIUM students, staff, and postgraduate users.

## Table of Contents

- [Getting Started](#getting-started)
- [User Roles and Permissions](#user-roles-and-permissions)
- [Navigating the Application](#navigating-the-application)
- [Browsing Facilities](#browsing-facilities)
- [Making a Booking](#making-a-booking)
- [Managing Your Bookings](#managing-your-bookings)
- [Admin Features](#admin-features)
- [Troubleshooting](#troubleshooting)
- [FAQ](#faq)

## Getting Started

### System Requirements

- **Operating System**: Windows 10/11
- **Java Runtime**: Java 21 or higher
- **Memory**: Minimum 512MB RAM
- **Display**: 1024x768 resolution or higher

### Installation

1. **Download the Application**
   - Clone or download the project from GitHub
   - Ensure JavaFX SDK is available

2. **Run the Application**
   ```bash
   # Use the provided batch file
   run.bat

   # Or run manually
   java --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -cp bin view.MainApplication
   ```

3. **First Time Setup**
   - The application opens with a **Login screen** for proper authentication. Use your IIUM matric number to sign in.
   - Use the quick login buttons or the user switching feature to test different roles (Admin/Staff/Student/Postgrad).

## User Roles and Permissions

The system automatically determines your role based on your IIUM matriculation number:

### 🎓 Postgraduate Students (Matric: 3XXXXXX)
- **Can book**: Postgraduate facilities + General student facilities
- **Examples**: Carrel rooms, postgraduate study areas
- **Restrictions**: Cannot book staff-only facilities

### 👨‍🎓 Regular Students (Matric: 2XXXXXX)
- **Can book**: General student facilities
- **Examples**: Discussion rooms, study areas, computer rooms
- **Restrictions**: Cannot book postgraduate or staff facilities

### 👨‍🏫 Staff Members (Matric: 1XXXXXX)
- **Can book**: Staff facilities + General facilities
- **Examples**: Staff rooms, library use facilities
- **Restrictions**: Cannot book postgraduate-only facilities

### 👑 Administrators (Matric: 0XXXXXX)
- **Can book**: All facilities in the system
- **Additional access**: Admin panel for system management
- **Examples**: All facility types, system monitoring

### Switching User Accounts

To test different user roles:

1. Click the **user icon** (👤) in the top-right corner
2. Select from quick options:
   - **Admin (0...)**: Set matric to 0123456
   - **Staff (1...)**: Set matric to 1123456
   - **Student (2...)**: Set matric to 2123456
   - **Postgrad (3...)**: Set matric to 3123456
3. Or enter a custom matric number
4. Click **Switch User**

## Navigating the Application

### Main Interface

The application uses a sidebar navigation with the following sections:

- **🏠 Dashboard**: Overview and quick actions
- **🏢 Browse Facilities**: Search and book facilities
- **📅 My Bookings**: View and manage your bookings
- **⚙️ Admin Panel**: System administration (admin only)
- **👤 User Menu**: Account switching and logout

### Status Indicators

Facility status is indicated by color:
- 🟢 **Green**: Available for booking
- 🔴 **Red**: Currently booked
- 🟠 **Orange**: Under maintenance
- ⚪ **Gray**: Temporarily closed or reserved

## Browsing Facilities

### Accessing Facilities

1. Click **"Browse Facilities"** from the dashboard or sidebar
2. View all available facilities in card format
3. Use search and filter options to find specific facilities

### Search and Filter Options

#### 🔍 Search Facilities
- **Search by name**: Enter facility name or ID
- **Real-time filtering**: Results update as you type
- **Examples**: "Carrel", "L1-01", "Discussion"

#### 📍 Filter by Location
- **All Locations**: Show all facilities
- **Level 1**: Ground floor facilities
- **Level 2**: First floor facilities
- **Level 3**: Second floor facilities

#### 📊 Filter by Status
- **All Status**: Show all facilities
- **Available**: Only bookable facilities
- **Booked**: Currently reserved facilities
- **Closed**: Unavailable facilities

### Facility Information

Each facility card shows:
- **Facility Name**: e.g., "Carrel Room L1-01"
- **Type**: Carrel Room, Discussion Room, etc.
- **Status**: Current availability with color coding
- **Capacity**: Number of people
- **Equipment**: Available resources (PC, Projector, etc.)
- **Access Level**: Who can book (Students Only, Postgraduate Only, etc.)

## Making a Booking

### Step-by-Step Booking Process

1. **Select a Facility**
   - Browse facilities or use search
   - Click on a facility card to view details

2. **Review Facility Details**
   - Check capacity and equipment
   - Verify access privileges
   - Read detailed information

3. **Choose Booking Time**
   - **Date**: Select from calendar (future dates only)
   - **Start Time**: Choose from dropdown (business hours)
   - **End Time**: Choose duration (1-3 hours typically)

4. **Booking Validation**
   - System checks:
     - User privileges
     - Facility availability
     - Time conflicts
     - Business hours (typically 8 AM - 10 PM)

5. **Confirm Booking**
   - Review booking details
   - Click **"Book Facility"**
   - Receive confirmation with booking ID

### Booking Rules

- **Advance Booking**: Must book at least 30 minutes in advance
- **Business Hours**: 8:00 AM to 10:00 PM (IIUM library hours)
- **Duration Limits**: 1-3 hours per booking (facility dependent)
- **Conflict Prevention**: Cannot double-book the same facility/time
- **Privilege Enforcement**: Users can only book permitted facilities

### Booking Confirmation

After successful booking:
- ✅ **Success message** with booking ID
- ✅ **Facility status updates** to "BOOKED"
- ✅ **Navigation to booking history**
- ✅ **Email/SMS notification** (future feature)

## Managing Your Bookings

### Viewing Your Bookings

1. Click **"My Bookings"** from the sidebar
2. View all your active and past bookings
3. Bookings are sorted by date (newest first)

### Booking Information

Each booking shows:
- **Booking ID**: Unique identifier (e.g., BK-20250121-001)
- **Facility**: Name and location
- **Date & Time**: Start and end times
- **Status**: Active, Cancelled, Expired, Completed
- **Actions**: Cancel (if active)

### Cancelling a Booking

1. Find your booking in "My Bookings"
2. Click **"Cancel"** button (only for active bookings)
3. Confirm cancellation
4. Facility becomes available for others

### Booking History

- **Active Bookings**: Currently reserved
- **Past Bookings**: Completed or cancelled
- **Booking Statistics**: Total bookings, success rate
- **Cancellation Policy**: Can cancel up to 30 minutes before start time

## Admin Features

### Accessing Admin Panel

1. Login as administrator (matric starting with 0)
2. Click **"Admin Panel"** from sidebar
3. Access advanced system management features

### Facility Management

- **Update Status**: Change facility availability
- **Maintenance Mode**: Mark facilities for maintenance
- **Bulk Operations**: Update multiple facilities
- **Status Monitoring**: Real-time facility status overview

### User Management

- **View All Users**: See all system users
- **User Statistics**: Booking patterns and usage
- **Access Control**: Manage user permissions
- **Audit Trail**: Track system changes

### System Monitoring

- **Booking Analytics**: Usage statistics
- **Facility Utilization**: Occupancy rates
- **System Health**: Performance metrics
- **Error Logs**: System issues and resolutions

## Troubleshooting

### Common Issues

#### "Insufficient Privileges" Error
- **Cause**: Trying to book restricted facility
- **Solution**: Check your user role and facility access level
- **Example**: Students cannot book postgraduate-only facilities

#### "Facility Not Available" Error
- **Cause**: Facility is booked, under maintenance, or closed
- **Solution**: Choose different time or facility
- **Check**: Use refresh button to update status

#### "Invalid Booking Time" Error
- **Cause**: Outside business hours or too short advance booking
- **Solution**: Book during 8 AM - 10 PM, at least 30 minutes ahead
- **Check**: Verify date and time selections

#### "Booking Conflict" Error
- **Cause**: Another user booked the same time slot
- **Solution**: Choose different time or facility
- **Prevention**: Book as soon as possible

### Application Issues

#### Application Won't Start
- **Check**: Java 21 installation
- **Verify**: JavaFX SDK path in run.bat
- **Solution**: Update PATH environment variable

#### UI Not Responding
- **Cause**: Memory issues or large dataset
- **Solution**: Restart application
- **Prevention**: Close other memory-intensive applications

#### Status Not Updating
- **Cause**: UI refresh issue
- **Solution**: Click "🔄 Refresh" button
- **Alternative**: Navigate to admin panel and back

### Getting Help

1. **Check Documentation**: Review this user manual
2. **Test Different Users**: Try different matric numbers
3. **Contact Support**: Reach out to development team
4. **Check Logs**: Review console output for error messages

## FAQ

### General Questions

**Q: How many facilities are available?**
A: The system includes 26 real IIUM library facilities across multiple categories.

**Q: Can I book multiple facilities at once?**
A: No, each booking is for one facility at a time.

**Q: How far in advance can I book?**
A: You can book up to several months in advance, but must be at least 30 minutes ahead.

**Q: What happens if I forget to cancel?**
A: The system automatically expires unused bookings, but please cancel if you won't attend.

### Booking Questions

**Q: Can I extend my booking?**
A: Currently, you must make a new booking. Extension feature planned for future release.

**Q: Can I book recurring sessions?**
A: Not currently supported. Each booking is one-time only.

**Q: What if someone else books my facility?**
A: First-come, first-served. Book as early as possible.

**Q: Can I book for someone else?**
A: No, bookings are tied to your matric number only.

### Technical Questions

**Q: Is my data stored permanently?**
A: Currently uses in-memory storage. Data persists during session only.

**Q: Can multiple users use the system simultaneously?**
A: Single-user desktop application. Multi-user support planned.

**Q: Does it work on Mac/Linux?**
A: Currently Windows-only. Cross-platform support possible.

**Q: Can I get notifications?**
A: Not currently implemented. Email/SMS notifications planned.

### Access Questions

**Q: I'm a postgraduate but can't book carrel rooms?**
A: Ensure your matric number starts with "3". Use user switching to test.

**Q: Staff can't access student facilities?**
A: Staff have access to staff facilities + general facilities, not student-only.

**Q: What facilities can regular students book?**
A: Discussion rooms, study areas, computer rooms, multimedia rooms.

---

## Support Information

**Development Team:**
- Ibrahim Bin Nasrum (2116467) - Booking Management
- Muhammad Izwan Bin Muhammad Isham (2428113) - Login & Navigation
- Mohammad Amir Imtiyaz Bin Mohd Annuar (2212545) - Room List & Availability

**Project Repository:** [GitHub Link]

**Version:** 1.0.0
**Last Updated:** January 2025

For additional support, please contact your course instructor or the development team.