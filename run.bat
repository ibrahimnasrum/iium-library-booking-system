@echo off
REM IIUM Library Booking System - Ibrahim's Booking Component
REM Download JavaFX first from: https://gluonhq.com/products/javafx/
REM Extract it and update the FX_PATH below

REM Set JavaFX path - CHANGE THIS TO YOUR JAVAFX LOCATION
SET FX_PATH="C:\Users\User\OneDrive\Pictures\Documents\GitHub\javafx-sdk-21.0.9\lib"

echo ========================================
echo IIUM Library Booking System
echo Ibrahim's Booking Management Component
echo ========================================
echo.
echo Checking JavaFX path: %FX_PATH%
if not exist %FX_PATH% (
    echo.
    echo ============================================
    echo ERROR: JavaFX not found at %FX_PATH%
    echo ============================================
    echo.
    echo DOWNLOAD JAVAFX:
    echo 1. Go to: https://gluonhq.com/products/javafx/
    echo 2. Download: JavaFX Windows x64 SDK (version 21 or 23)
    echo 3. Extract to: C:\Users\User\OneDrive\Pictures\Documents\GitHub\javafx-sdk-21.0.9
    echo 4. Make sure C:\Program Files\javafx-sdk-21.0.9\lib folder exists
    echo 5. Edit this run.bat file and set FX_PATH correctly
    echo.
    pause
    exit /b 1
)

echo.
echo Compiling Java files...
if not exist bin mkdir bin
javac --module-path %FX_PATH% --add-modules javafx.controls,javafx.fxml -d bin src\Main.java src\model\enums\*.java src\model\*.java src\model\services\*.java src\view\components\*.java src\view\pages\*.java src\view\*.java

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Compilation failed!
    echo.
    pause
    exit /b 1
)

echo.
echo Compilation successful!
echo.
echo Starting IIUM Library Booking System...
echo.
java --module-path %FX_PATH% --add-modules javafx.controls,javafx.fxml -cp bin view.MainApplication

pause

