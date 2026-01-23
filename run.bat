@echo off
REM IIUM Library Booking System
REM Uses bundled JavaFX SDK

REM Set JavaFX path to bundled SDK
SET FX_PATH="javafx-sdk-21.0.9\lib"

echo ========================================
echo IIUM Library Booking System
echo Professional Login & Booking Management
echo ========================================
echo.
echo Using bundled JavaFX SDK: %FX_PATH%
if not exist %FX_PATH% (
    echo.
    echo ============================================
    echo ERROR: JavaFX not found at %FX_PATH%
    echo ============================================
    echo.
    echo The bundled JavaFX SDK is missing.
    echo Please ensure javafx-sdk-21.0.9 folder exists.
    echo.
    pause
    exit /b 1
)

:continue
echo.
echo Compiling Java files...
if not exist bin mkdir bin
javac --module-path %FX_PATH% --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base,javafx.media,javafx.web,javafx.swing -d bin src\Main.java src\model\enums\*.java src\model\*.java src\model\services\*.java src\view\components\*.java src\view\pages\*.java src\view\*.java

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
echo Login screen will appear first
echo.
java --module-path %FX_PATH% --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base,javafx.media,javafx.web,javafx.swing -cp bin Main

