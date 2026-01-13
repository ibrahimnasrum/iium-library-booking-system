#!/bin/bash
# IIUM Library Booking System - Run Script for Linux/Mac
# Edit the FX_PATH variable below to match your JavaFX installation

# Set JavaFX path - CHANGE THIS TO YOUR JAVAFX LOCATION
FX_PATH="/path/to/javafx-sdk/lib"

echo "========================================"
echo "IIUM Library Booking System"
echo "========================================"
echo ""
echo "Checking JavaFX path: $FX_PATH"

if [ ! -d "$FX_PATH" ]; then
    echo "ERROR: JavaFX not found at $FX_PATH"
    echo "Please edit run.sh and set the correct FX_PATH"
    echo "Download JavaFX from: https://gluonhq.com/products/javafx/"
    exit 1
fi

echo ""
echo "Compiling Java files..."
javac --module-path "$FX_PATH" --add-modules javafx.controls,javafx.fxml -d bin src/model/*.java src/GUI/*.java src/Main.java

if [ $? -ne 0 ]; then
    echo "ERROR: Compilation failed!"
    exit 1
fi

echo ""
echo "Compilation successful!"
echo ""
echo "Starting application..."
echo ""
java --module-path "$FX_PATH" --add-modules javafx.controls,javafx.fxml -cp bin Main
