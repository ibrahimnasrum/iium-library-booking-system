# 🚀 IIUM Library Booking System - Quick Start Guide

Get the IIUM Library Booking System up and running in 5 minutes!

## ⚡ 5-Minute Setup

### Step 1: Prerequisites Check
- ✅ Java 21 JDK installed
- ✅ JavaFX SDK 21.0.9 (bundled in project)

### Step 2: Launch the Application
Double-click `run.bat` or run in terminal:
```cmd
run.bat
```

### Step 3: Login Screen Appears
The professional login screen will appear as the first layer.

## 🧪 Test Scenarios

### Quick Login Testing
Use these matric numbers for instant testing:

| Role | Matric Number | Access Level |
|------|---------------|--------------|
| **Admin** | `0123456` | Full system access |
| **Student** | `2123456` | Student facilities only |
| **Postgrad** | `3123456` | Postgraduate + general facilities |

### Test Flow
1. **Launch** → Login screen appears
2. **Enter Matric** → Type `0123456` (Admin)
3. **Enter Password** → Type `password`
4. **Click Login** → Access main system
5. **Explore Features** → Test booking, facilities, admin panel

## 🔧 Manual Compilation (Alternative)

If `run.bat` doesn't work:

### Compile:
```powershell
javac --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -d bin src\Main.java src\model\*.java src\model\services\*.java src\view\*.java src\view\pages\*.java
```

### Run:
```powershell
java --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -cp bin Main
```

## 🐛 Troubleshooting Quick Fixes

### Issue: "JavaFX runtime not found"
**Fix**: Use bundled JavaFX SDK path in compilation commands.

### Issue: "Class not found"
**Fix**: Ensure all source files are compiled together.

### Issue: Login fails
**Fix**: Use quick login buttons or check matric format (7 digits starting with role prefix).

### Issue: GUI doesn't appear
**Fix**: Check JavaFX modules are correctly added.

## 🎯 What to Expect

### ✅ Successful Launch Indicators
- Login screen appears with IIUM branding
- Quick login buttons visible
- Matric number and password fields active
- Professional gradient background

### ✅ Post-Login Features
- Main navigation sidebar
- Dashboard with user info
- Facilities page with 26 IIUM rooms
- Booking management interface
- Admin panel (for admin users)

## 📞 Need Help?

- Check [USER_MANUAL.md](USER_MANUAL.md) for detailed usage
- See [DEVELOPMENT_GUIDE.md](DEVELOPMENT_GUIDE.md) for technical details
- Review [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for code reference

## ⚡ Quick Commands Reference

```cmd
# Run application
run.bat

# Compile manually
javac --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -d bin src\Main.java src\model\*.java src\model\services\*.java src\view\*.java src\view\pages\*.java

# Run manually
java --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -cp bin Main

# Test authentication
java --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -cp bin tools.AuthTest

# Run auto-login test
java --module-path "javafx-sdk-21.0.9\lib" --add-modules javafx.controls,javafx.fxml -cp bin tools.AutoLoginTest
```

---
**Ready to explore the IIUM Library Booking System! 🏛️✨**