@echo off
REM IIUM Library Facility Image Setup Script
echo ========================================
echo IIUM Library Facility Images Setup
echo ========================================
echo.
echo The resources/images folder has been created at:
echo %~dp0resources\images\
echo.
echo To add facility images:
echo 1. Place your JPG/PNG images in the resources/images/ folder
echo 2. Name them using the facility ID (e.g., SR001.jpg, CR-L1-01.jpg)
echo 3. Restart the application to see the images
echo.
echo Current facilities that can have images:
echo - Study Rooms: SR001.jpg through SR010.jpg
echo - Carrel Rooms: CR-L1-01.jpg, CR-L2-01.jpg, etc.
echo - Discussion Rooms: DR-01.jpg through DR-04.jpg
echo - Computer Lab: CL-01.jpg
echo - Auditorium: AUD-01.jpg
echo - And more...
echo.
echo The application will show placeholder images if no image file exists.
echo.
pause