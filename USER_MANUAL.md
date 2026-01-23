# 📚 IIUM Library Booking System - User Manual

Complete guide for users of the IIUM Library Booking System.

## 👤 User Roles & Permissions

### Role Assignment
Your access level is automatically determined by your IIUM matric number prefix:

| Matric Prefix | Role | Access Level |
|---------------|------|--------------|
| **0** (e.g., 0123456) | Admin | Full system access, user management |
| **1** (e.g., 1123456) | Staff | Limited administrative access |
| **2** (e.g., 2123456) | Student | Student facilities only |
| **3** (e.g., 3123456) | Postgraduate | Postgraduate + general facilities |

## 🔐 Authentication

### Login Process
1. **Launch Application** - Run `run.bat` or compile manually
2. **Login Screen** - Professional login screen appears
3. **Enter Credentials**:
   - Matric Number: Your 7-digit IIUM matric number
   - Password: Your assigned password
4. **Click Login** - Or press Enter in password field

### Quick Login (Testing)
For testing purposes, use these quick login buttons:
- **Admin**: Instant admin access
- **Student**: Student role testing
- **Postgrad**: Postgraduate role testing

### Error Handling
- **Invalid Matric**: "Please enter your IIUM Matric Number"
- **Invalid Password**: "Please enter your password"
- **Wrong Credentials**: "Invalid matric number or password. Please check your credentials."

## 🏠 Main Interface

### Navigation Sidebar
After login, access these main sections:
- **Dashboard** - Overview and quick actions
- **Facilities** - Browse and search facilities
- **My Bookings** - View your booking history
- **Admin Panel** - System management (Admin only)

### Dashboard
- **Welcome Message** - Personalized greeting
- **Quick Stats** - Your booking summary
- **Recent Activity** - Latest bookings
- **Quick Actions** - Fast access to common tasks

## 🏢 Facilities Management

### Browsing Facilities
1. **Facilities Page** - Click "Facilities" in sidebar
2. **View All Facilities** - 26 IIUM library facilities displayed
3. **Facility Details** - Click any facility for detailed information

### Facility Information
Each facility shows:
- **Facility ID** - Unique identifier
- **Type** - Discussion room, study room, etc.
- **Location** - Building and floor
- **Capacity** - Number of people
- **Equipment** - Available amenities
- **Status** - Available/Reserved/Under Maintenance
- **Booking Rules** - Time restrictions, role requirements

### Search & Filter
- **Search Bar** - Search by facility ID, type, or location
- **Filter Options**:
  - By facility type
  - By location
  - By availability
  - By equipment
  - By capacity

## 📅 Booking Management

### Making a Booking
1. **Select Facility** - Choose from facilities list
2. **View Details** - Check availability and rules
3. **Choose Date & Time** - Select booking slot
4. **Confirm Booking** - Review and submit

### Booking Validation
The system automatically:
- **Checks Conflicts** - Prevents double-booking
- **Validates Permissions** - Ensures role-based access
- **Applies Rules** - Enforces booking policies
- **Updates Status** - Changes facility availability

### Viewing Your Bookings
1. **My Bookings Page** - Click "My Bookings" in sidebar
2. **Booking History** - See all your past and current bookings
3. **Booking Details** - View date, time, facility, status
4. **Cancel Booking** - Cancel upcoming bookings if allowed

### Canceling Bookings
- **Upcoming Bookings** - Can be canceled up to 1 hour before
- **Past Bookings** - Cannot be modified
- **Cancellation Policy** - Check facility-specific rules

## 👑 Admin Panel (Admin Only)

### System Overview
- **User Statistics** - Total users, active bookings
- **Facility Status** - Real-time facility monitoring
- **Booking Analytics** - Usage patterns and trends

### User Management
- **View All Users** - List of system users
- **User Details** - Individual user information
- **Role Management** - Change user roles (if permitted)

### Facility Management
- **Add Facilities** - Create new facilities
- **Edit Facilities** - Modify facility information
- **Maintenance Mode** - Set facilities under maintenance
- **Delete Facilities** - Remove unused facilities

### System Settings
- **Booking Policies** - Configure booking rules
- **Time Restrictions** - Set operating hours
- **Notification Settings** - Configure alerts

## 🔄 Real-time Updates

### Status Synchronization
- **Live Updates** - Facility status changes immediately
- **Booking Conflicts** - Prevented in real-time
- **User Notifications** - Alerts for booking changes

### Refresh Functionality
- **Manual Refresh** - Update button in each section
- **Auto-refresh** - Status updates every 30 seconds
- **Force Refresh** - Reload all data from server

## 🆘 Troubleshooting

### Login Issues
- **Can't Login** - Check matric number format (7 digits)
- **Wrong Password** - Contact administrator for password reset
- **Role Not Working** - Verify matric prefix matches role

### Booking Problems
- **Facility Not Available** - Check real-time status
- **Booking Failed** - May be conflict or permission issue
- **Can't Cancel** - Check cancellation policy

### System Errors
- **Application Crashes** - Restart application
- **Data Not Loading** - Check internet connection
- **UI Not Responding** - Wait for refresh or restart

## 📞 Support & Help

### Getting Help
- **User Manual** - This document
- **Quick Start Guide** - [QUICK_START.md](QUICK_START.md)
- **FAQ** - Check common questions below

### Frequently Asked Questions

**Q: How do I change my password?**
A: Contact your system administrator for password changes.

**Q: Can I book multiple facilities at once?**
A: No, one booking per time slot per user.

**Q: How far in advance can I book?**
A: Up to 30 days in advance, depending on facility rules.

**Q: What if I need to cancel a booking?**
A: Cancel through "My Bookings" page, subject to policy.

**Q: How do I know if a facility is available?**
A: Check real-time status on Facilities page.

## 📋 Booking Policies

### General Rules
- Maximum 2 hours per booking
- 24-hour advance booking required
- No-shows may result in booking restrictions
- Facilities must be used for intended purpose

### Role-Specific Rules
- **Students**: Limited to student-designated facilities
- **Postgraduates**: Access to general and postgraduate facilities
- **Staff**: Additional booking privileges
- **Admins**: Full access to all facilities

### Cancellation Policy
- Cancel up to 1 hour before booking time
- Late cancellations may affect future booking privileges
- No refunds for canceled bookings

## 🎯 Best Practices

### Efficient Usage
- **Plan Ahead** - Book facilities in advance
- **Check Availability** - Use real-time status updates
- **Cancel if Needed** - Free up facilities for others
- **Follow Rules** - Adhere to facility-specific policies

### System Navigation
- **Use Search** - Quickly find facilities
- **Bookmark Favorites** - Note frequently used facilities
- **Check History** - Review past booking patterns
- **Stay Updated** - Monitor booking confirmations

---
**For technical documentation, see [DEVELOPMENT_GUIDE.md](DEVELOPMENT_GUIDE.md)**