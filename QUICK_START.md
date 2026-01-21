# 🚀 IIUM Library Booking System - Quick Start Guide

## Welcome!

Get up and running with the IIUM Library Booking System in under 5 minutes.

## ⚡ Quick Setup (Windows)

### 1. Download & Extract
```bash
# Clone the repository
git clone https://github.com/your-username/iium-library-booking-system.git
cd iium-library-booking-system
```

### 2. Run the Application
```bash
# Double-click run.bat OR run from command line
run.bat
```

**That's it!** The application will start automatically.

## 🎯 First Steps

### Login to the System
The application starts with a **login screen** for proper authentication.

1. **Enter your IIUM Matric Number** (e.g., `3123456` for postgraduate)
2. **Password**: Any password works for demo purposes
3. **Quick Login**: Use the quick login buttons for testing:
   - **Admin (0123456)** → Full system access
   - **Student (2123456)** → Student facilities
   - **Postgrad (3123456)** → Postgraduate facilities

### Make Your First Booking

1. **Browse Facilities** → Click "🏢 Browse Facilities"
2. **Find a Room** → Search for "Carrel" or browse the list
3. **Book It** → Click a facility card → Select date/time → Click "Book"
4. **Success!** → You'll see a confirmation message

### Project Diagrams & Documentation
- The project UML diagram is available in `uml/full_project_diagram.puml` and the rendered `uml/full_project_diagram.png`.
- To render locally, follow `uml/README.md` or use `tools/render-uml.bat` after placing `plantuml.jar` in the repo root.

### Recent Fixes (summary)
- Proper Login screen added (replaces auto-login). ✅
- Closed and Maintenance facility statuses preserved and displayed. ✅
- Filter behavior and facility card refresh fixed. ✅
- Added debug logs to help troubleshoot facility loading and filtering. ✅

## 🧪 Test Scenarios

### ✅ Working Features
- [x] User authentication with IIUM matric numbers
- [x] Role-based access control
- [x] Facility browsing and search
- [x] Real-time booking with conflict detection
- [x] Status updates and refresh functionality
- [x] Booking management (view/cancel)

### 🎮 Try These Tests

#### Test 1: Postgraduate Booking
```
Login: 3123456 (Postgraduate)
Action: Book Carrel Room L1-01
Expected: ✅ Success
```

#### Test 2: Privilege Restriction
```
Login: 2123456 (Regular Student)
Action: Book Carrel Room L1-01
Expected: ❌ "Insufficient privileges"
```

#### Test 3: Time Conflict
```
Login: 3123456
Action: Book same room/time twice
Expected: ❌ "Booking conflict"
```

#### Test 4: Admin Access
```
Login: 0123456 (Admin)
Action: Access Admin Panel
Expected: ✅ Full admin features
```

## 🛠️ Development Quick Start

### Prerequisites
- Java 21 JDK ✅
- JavaFX 21.0.9 SDK ✅ (included in project)

### Compile & Run Manually
```bash
# Compile
javac --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -d bin src\Main.java src\model\enums\*.java src\model\*.java src\model\services\*.java src\view\components\*.java src\view\pages\*.java src\view\*.java

# Run
java --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -cp bin view.MainApplication
```

### Project Structure at a Glance
```
src/
├── view/MainApplication.java    # App entry point
├── view/MainLayout.java         # Main UI
├── view/pages/                  # UI pages
├── model/                       # Data models
├── model/services/              # Business logic
└── model/enums/                 # Constants
```

## 🔧 Troubleshooting

### App Won't Start
```bash
# Check Java version
java --version
# Should show: Java 21.x.x

# Check JavaFX
ls javafx-sdk-21.0.9/lib
# Should contain: javafx-*.jar files
```

### Compilation Errors
```bash
# Clean and recompile
rm -rf bin
mkdir bin
javac --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -d bin src\Main.java src\model\enums\*.java src\model\*.java src\model\services\*.java src\view\components\*.java src\view\pages\*.java src\view\*.java
```

### Status Not Updating
- Click the **🔄 Refresh** button on the facilities page
- Or navigate to Admin Panel and back

### Permission Errors
- Run command prompt as Administrator
- Check file permissions on the project folder

## 📚 Key Features Overview

| Feature | Status | Notes |
|---------|--------|-------|
| User Authentication | ✅ Working | IIUM matric-based roles |
| Facility Booking | ✅ Working | Real-time conflict detection |
| Status Updates | ✅ Working | Manual refresh available |
| Admin Panel | ✅ Working | Facility management |
| Search & Filter | ✅ Working | Location, status, type filters |
| Booking History | ✅ Working | View and cancel bookings |

## 🎯 What You Can Do Right Now

### As Postgraduate Student (3123456)
- ✅ Book carrel rooms
- ✅ View booking history
- ✅ Cancel bookings
- ✅ Browse all facilities

### As Administrator (0123456)
- ✅ All student features
- ✅ Update facility statuses
- ✅ Monitor system usage
- ✅ Manage all bookings

### Testing Different Scenarios
- Switch between user roles using the user menu
- Try booking conflicts, privilege violations, time validations
- Test admin features and status updates

## 📖 Next Steps

1. **Explore the Code** → Check `src/view/` for UI, `src/model/services/` for logic
2. **Read Documentation** → See `README.md`, `API_DOCUMENTATION.md`, `USER_MANUAL.md`
3. **Run Tests** → Try different user roles and booking scenarios
4. **Contribute** → Check `DEVELOPMENT_GUIDE.md` for contribution guidelines

## 🆘 Need Help?

### Quick Fixes
- **App crashes?** Restart and try different user
- **Can't book?** Check user privileges and facility status
- **UI frozen?** Use the refresh button

### Documentation
- 📖 **User Manual** → `USER_MANUAL.md`
- 🛠️ **Development Guide** → `DEVELOPMENT_GUIDE.md`
- 📚 **API Docs** → `API_DOCUMENTATION.md`

### Common Issues
- **"Insufficient privileges"** → Switch to appropriate user role
- **"Facility not available"** → Check status or try different time
- **Status not updating** → Click refresh button or navigate away/back

---

## 🎉 You're All Set!

The IIUM Library Booking System is now running. Start exploring the features and testing different scenarios. Happy coding! 🚀

**Version:** 1.0.0
**Date:** January 2025
**Team:** IIUM Group 5