@echo off
REM IIUM Library Booking System - Run Script for Windows
REM Edit the FX_PATH variable below to match your JavaFX installation

REM Set JavaFX path - CHANGE THIS TO YOUR JAVAFX LOCATION
SET FX_PATH=C:\javafx-sdk-21\lib

echo ========================================
echo IIUM Library Booking System
echo ========================================
echo.
echo Checking JavaFX path: %FX_PATH%
if not exist "%FX_PATH%" (
    echo ERROR: JavaFX not found at %FX_PATH%
    echo Please edit run.bat and set the correct FX_PATH
    echo Download JavaFX from: https://gluonhq.com/products/javafx/
    pause
    exit /b 1
)

echo.
echo Compiling Java files...
javac --module-path "%FX_PATH%" --add-modules javafx.controls,javafx.fxml -d bin src\model\*.java src\GUI\*.java src\Main.java

if %errorlevel% neq 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo.
echo Compilation successful!
echo.
echo Starting application...
echo.
java --module-path "%FX_PATH%" --add-modules javafx.controls,javafx.fxml -cp bin Main

pause
