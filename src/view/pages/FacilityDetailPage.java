package view.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Facility;
import model.User;
import model.Equipment;
import model.Booking;
import model.services.BookingService;
import model.services.BookingPolicy;
import model.enums.FacilityStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Consumer;

public class FacilityDetailPage extends VBox {

    private User currentUser;
    private Facility facility;
    private Consumer<String> navigateCallback;

    // UI Components
    private ImageView facilityImage;
    private Label nameLabel;
    private Label idLabel;
    private Label locationLabel;
    private Label capacityLabel;
    private Label statusLabel;
    private Label privilegeLabel;
    private TextArea descriptionArea;
    private VBox equipmentList;
    private VBox bookingSection;

    // Booking components
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private ComboBox<String> startTimeCombo;
    private ComboBox<String> endTimeCombo;

    public FacilityDetailPage(User user, Consumer<String> navigateCallback) {
        this.currentUser = user;
        this.navigateCallback = navigateCallback;
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        facilityImage = new ImageView();
        facilityImage.setFitWidth(400);
        facilityImage.setFitHeight(250);
        facilityImage.setPreserveRatio(true);
        facilityImage.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ddd; -fx-border-width: 1;");

        nameLabel = new Label();
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        nameLabel.setStyle("-fx-text-fill: #2c3e50;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(400);

        idLabel = new Label();
        idLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 14px;");
        idLabel.setWrapText(true);
        idLabel.setMaxWidth(400);

        locationLabel = new Label();
        locationLabel.setStyle("-fx-font-size: 16px;");
        locationLabel.setWrapText(true);
        locationLabel.setMaxWidth(400);

        capacityLabel = new Label();
        capacityLabel.setStyle("-fx-font-size: 16px;");
        capacityLabel.setWrapText(true);
        capacityLabel.setMaxWidth(400);

        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(400);

        privilegeLabel = new Label();
        privilegeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        privilegeLabel.setWrapText(true);
        privilegeLabel.setMaxWidth(400);

        descriptionArea = new TextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setStyle("-fx-control-inner-background: #f8f9fa;");

        equipmentList = new VBox(5);
        equipmentList.setPadding(new Insets(10));

        // Initialize booking components
        startDatePicker = new DatePicker(LocalDate.now());
        endDatePicker = new DatePicker(LocalDate.now());
        startTimeCombo = new ComboBox<>();
        endTimeCombo = new ComboBox<>();

        setupBookingSection();
    }

    private void setupBookingSection() {
        bookingSection = new VBox(15);
        bookingSection.setPadding(new Insets(20));
        bookingSection.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1; -fx-border-radius: 5;");

        Label bookingTitle = new Label("Make a Booking");
        bookingTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Date and time pickers
        HBox dateTimeBox = new HBox(15);
        dateTimeBox.setAlignment(Pos.CENTER_LEFT);

        // Reset date pickers to today
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());

        // Function to populate time combos based on selected date
        Runnable populateTimeCombos = () -> {
            LocalDate selectedDate = startDatePicker.getValue();
            startTimeCombo.getItems().clear();
            endTimeCombo.getItems().clear();

            LocalDateTime earliestAllowed;
            if (selectedDate.equals(LocalDate.now())) {
                // Today: only times at least 30 minutes from now
                earliestAllowed = LocalDateTime.now().plusMinutes(30);
            } else {
                // Future date: any business hour
                earliestAllowed = LocalDateTime.of(selectedDate, LocalTime.of(8, 0));
            }

            for (int hour = 8; hour <= 22; hour++) {
                for (int minute = 0; minute < 60; minute += 30) {
                    LocalDateTime timeSlot = LocalDateTime.of(selectedDate, LocalTime.of(hour, minute));
                    if (timeSlot.isAfter(earliestAllowed) || timeSlot.isEqual(earliestAllowed) ||
                        !selectedDate.equals(LocalDate.now())) {
                        String time = String.format("%02d:%02d", hour, minute);
                        startTimeCombo.getItems().add(time);
                        endTimeCombo.getItems().add(time);
                    }
                }
            }

            // Set default values if available
            if (!startTimeCombo.getItems().isEmpty()) {
                startTimeCombo.setValue(startTimeCombo.getItems().get(0));
                endTimeCombo.setValue(startTimeCombo.getItems().get(0));
            }
        };

        // Initial population
        populateTimeCombos.run();

        // Update times when date changes
        startDatePicker.setOnAction(e -> populateTimeCombos.run());

        // Set default start time to next available slot (at least 30 minutes from now)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextAvailable = now.plusMinutes(30);
        
        // Round up to next 30-minute interval
        int minute = nextAvailable.getMinute();
        int roundedMinute = ((minute + 29) / 30) * 30; // Round up to nearest 30
        if (roundedMinute >= 60) {
            nextAvailable = nextAvailable.plusHours(1).withMinute(0);
        } else {
            nextAvailable = nextAvailable.withMinute(roundedMinute);
        }
        
        // Ensure within business hours
        LocalDate startDate = LocalDate.now();
        if (nextAvailable.getHour() < 8) {
            nextAvailable = nextAvailable.withHour(8).withMinute(0);
        } else if (nextAvailable.getHour() >= 22) {
            // Next day
            startDate = LocalDate.now().plusDays(1);
            startDatePicker.setValue(startDate);
            nextAvailable = LocalDateTime.of(startDate, LocalTime.of(8, 0));
            populateTimeCombos.run(); // Refresh time options for new date
        }
        startDatePicker.setValue(startDate);

        // Set default time if available in combo
        String defaultStartTime = String.format("%02d:%02d", nextAvailable.getHour(), nextAvailable.getMinute());
        if (startTimeCombo.getItems().contains(defaultStartTime)) {
            startTimeCombo.setValue(defaultStartTime);
        } else if (!startTimeCombo.getItems().isEmpty()) {
            startTimeCombo.setValue(startTimeCombo.getItems().get(0));
        }

        // Set end time to minimum booking duration after start
        LocalDateTime endTime = nextAvailable.plusMinutes(BookingPolicy.getMinBookingMinutes());
        LocalDate endDate = startDate;
        if (endTime.getHour() >= 22) {
            endDate = startDate.plusDays(1);
        }
        endDatePicker.setValue(endDate);
        
        // Set default end time if available in combo
        String defaultEndTime = String.format("%02d:%02d", endTime.getHour(), endTime.getMinute());
        if (endTimeCombo.getItems().contains(defaultEndTime)) {
            endTimeCombo.setValue(defaultEndTime);
        } else if (!endTimeCombo.getItems().isEmpty()) {
            endTimeCombo.setValue(endTimeCombo.getItems().get(0));
        }

        dateTimeBox.getChildren().addAll(
            new Label("Start:"), startDatePicker, startTimeCombo,
            new Label("End:"), endDatePicker, endTimeCombo
        );

        // Booking rules
        Label rulesLabel = new Label("Booking Rules:");
        rulesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextArea rulesArea = new TextArea();
        rulesArea.setEditable(false);
        rulesArea.setWrapText(true);
        rulesArea.setPrefRowCount(3);
        rulesArea.setText(getBookingRules());

        // Make Booking button
        Button bookButton = new Button("Make Booking");
        bookButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 16px; " +
                           "-fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 5;");
        bookButton.setOnAction(e -> handleBooking());

        bookingSection.getChildren().addAll(bookingTitle, dateTimeBox, rulesLabel, rulesArea, bookButton);
    }

    private void setupLayout() {
        setSpacing(20);
        setPadding(new Insets(30));
        setStyle("-fx-background-color: #f8f9fa;");

        // Top buttons
        HBox topButtons = new HBox(10);
        topButtons.setAlignment(Pos.CENTER_LEFT);

        // Back button
        Button backButton = new Button("← Back to Facilities");
        backButton.setOnAction(e -> {
            if (navigateCallback != null) {
                navigateCallback.accept("facilities");
            }
        });

        topButtons.getChildren().addAll(backButton);

        // Main content layout
        HBox mainContent = new HBox(30);

        // Left side - Image and basic info
        VBox leftSide = new VBox(15);
        leftSide.getChildren().addAll(facilityImage, nameLabel, idLabel);

        // Right side - Details
        VBox rightSide = new VBox(15);

        // Basic info grid
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(15);
        infoGrid.setVgap(10);

        infoGrid.add(new Label("Location:"), 0, 0);
        infoGrid.add(locationLabel, 1, 0);
        infoGrid.add(new Label("Capacity:"), 0, 1);
        infoGrid.add(capacityLabel, 1, 1);
        infoGrid.add(new Label("Status:"), 0, 2);
        infoGrid.add(statusLabel, 1, 2);
        infoGrid.add(new Label("Access:"), 0, 3);
        infoGrid.add(privilegeLabel, 1, 3);

        // Equipment section
        VBox equipmentSection = new VBox(10);
        Label equipmentTitle = new Label("Equipment & Amenities");
        equipmentTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        equipmentSection.getChildren().addAll(equipmentTitle, equipmentList);

        // Description section
        VBox descriptionSection = new VBox(10);
        Label descriptionTitle = new Label("Description & Notes");
        descriptionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        descriptionSection.getChildren().addAll(descriptionTitle, descriptionArea);

        rightSide.getChildren().addAll(infoGrid, equipmentSection, descriptionSection);

        mainContent.getChildren().addAll(leftSide, rightSide);

        // Add all to main layout
        getChildren().addAll(topButtons, mainContent, bookingSection);
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
        updateDisplay();
    }

    private void updateDisplay() {
        if (facility == null) return;

        // Update image
        try {
            if (facility.getImagePath() != null && !facility.getImagePath().isEmpty()) {
                // Convert relative path to absolute file URL
                java.io.File imageFile = new java.io.File(facility.getImagePath());
                if (imageFile.exists()) {
                    String imageUrl = imageFile.toURI().toString();
                    Image image = new Image(imageUrl, 400, 250, true, true);
                    facilityImage.setImage(image);
                } else {
                    // Image file doesn't exist, show placeholder
                    Image placeholder = new Image("https://via.placeholder.com/400x250/3498db/ffffff?text=" +
                        facility.getId(), 400, 250, false, true);
                    facilityImage.setImage(placeholder);
                }
            } else {
                // No image path, show placeholder with facility ID
                Image placeholder = new Image("https://via.placeholder.com/400x250/3498db/ffffff?text=" +
                    facility.getId(), 400, 250, false, true);
                facilityImage.setImage(placeholder);
            }
        } catch (Exception e) {
            // Error loading image, show error placeholder
            Image placeholder = new Image("https://via.placeholder.com/400x250/e74c3c/ffffff?text=Error",
                400, 250, false, true);
            facilityImage.setImage(placeholder);
        }

        // Update labels
        String displayName = cleanFacilityName(facility.getName(), facility.getType()).toUpperCase();
        nameLabel.setText(displayName);
        idLabel.setText("ID: " + facility.getId());
        locationLabel.setText(facility.getLocation());
        capacityLabel.setText(String.valueOf(facility.getCapacity()));

        // Status with color
        statusLabel.setText(facility.getStatus().toString());
        String statusColor = getStatusColor(facility.getStatus());
        statusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + statusColor + ";");

        privilegeLabel.setText(facility.getPrivilege().toString());

        // Description
        descriptionArea.setText(facility.getDetailedInfo());

        // Equipment
        updateEquipmentList();

        // Update booking section visibility
        bookingSection.setVisible(facility.isAvailable());
        bookingSection.setManaged(facility.isAvailable());

        // Reset booking dates to today when facility changes
        if (startDatePicker != null) {
            startDatePicker.setValue(LocalDate.now());
        }
        if (endDatePicker != null) {
            endDatePicker.setValue(LocalDate.now());
        }
    }

    private void updateEquipmentList() {
        equipmentList.getChildren().clear();

        List<Equipment> equipment = facility.getEquipment();
        if (equipment.isEmpty()) {
            Label noEquipmentLabel = new Label("No equipment listed");
            noEquipmentLabel.setStyle("-fx-text-fill: #666; -fx-font-style: italic;");
            equipmentList.getChildren().add(noEquipmentLabel);
        } else {
            for (Equipment eq : equipment) {
                HBox equipmentItem = new HBox(10);
                equipmentItem.setAlignment(Pos.CENTER_LEFT);
                equipmentItem.setPadding(new Insets(5));

                Label bullet = new Label("•");
                bullet.setStyle("-fx-font-weight: bold; -fx-text-fill: #3498db;");

                Label equipmentLabel = new Label(eq.toString());
                equipmentLabel.setStyle("-fx-font-size: 14px;");

                equipmentItem.getChildren().addAll(bullet, equipmentLabel);
                equipmentList.getChildren().add(equipmentItem);
            }
        }
    }

    private String getBookingRules() {
        StringBuilder rules = new StringBuilder();
        rules.append("• Maximum booking duration: ").append(BookingPolicy.getMaxBookingHours()).append(" hours\n");
        rules.append("• Minimum booking duration: ").append(BookingPolicy.getMinBookingMinutes()).append(" minutes\n");
        rules.append("• Minimum advance booking: ").append(BookingPolicy.getMinAdvanceMinutes()).append(" minutes\n");
        rules.append("• Maximum advance booking: ").append(BookingPolicy.getMaxAdvanceDays()).append(" days\n");
        rules.append("• Maximum bookings per day: ").append(BookingPolicy.getMaxBookingsPerUserPerDay()).append("\n");
        rules.append("• Business hours: ").append(BookingPolicy.getBusinessHours()).append("\n");
        rules.append("• ").append(BookingPolicy.getCancellationPolicy()).append("\n");

        if (facility != null && facility.getPrivilege() != model.enums.ReservationPrivilege.OPEN) {
            rules.append("• Access restriction: ").append(facility.getPrivilege().toString()).append("\n");
        }

        return rules.toString();
    }

    private String validateBookingDetails(User user, Facility facility, LocalDateTime startTime, LocalDateTime endTime) {
        // Check basic parameters
        if (user == null || facility == null || startTime == null || endTime == null) {
            return "Invalid booking parameters.";
        }

        // Check if facility is available
        if (!facility.isAvailable()) {
            return "Facility is not currently available.";
        }

        // Check privilege requirements
        if (!BookingPolicy.hasRequiredPrivilege(user, facility)) {
            return "You don't have permission to book this facility.";
        }

        // Check booking duration
        if (!BookingPolicy.isValidDuration(startTime, endTime)) {
            long minutes = java.time.Duration.between(startTime, endTime).toMinutes();
            if (minutes < BookingPolicy.getMinBookingMinutes()) {
                return "Booking duration is too short. Minimum duration is " + BookingPolicy.getMinBookingMinutes() + " minutes.";
            } else if (minutes > BookingPolicy.getMaxBookingHours() * 60) {
                return "Booking duration is too long. Maximum duration is " + BookingPolicy.getMaxBookingHours() + " hours.";
            }
            return "Invalid booking duration.";
        }

        // Check minimum advance booking time (30 minutes)
        if (!BookingPolicy.hasMinimumAdvanceTime(startTime)) {
            return "Bookings must be made at least 30 minutes in advance.";
        }

        // Check maximum advance booking time (14 days)
        if (!BookingPolicy.isWithinAdvanceLimit(startTime)) {
            return "Bookings cannot be made more than 14 days in advance.";
        }

        // Check user hasn't exceeded daily booking limit
        if (!BookingPolicy.isWithinUserDailyLimit(user, startTime)) {
            return "You have reached the maximum number of bookings allowed per day (" + BookingPolicy.getMaxBookingsPerUserPerDay() + ").";
        }

        // Check for overlapping bookings for this user
        if (BookingPolicy.hasUserBookingConflict(user, startTime, endTime)) {
            return "You already have a booking that conflicts with this time slot.";
        }

        // Check booking time is in the future
        if (startTime.isBefore(LocalDateTime.now())) {
            return "Booking start time must be in the future.";
        }

        // Check business hours
        if (!BookingPolicy.isWithinBusinessHours(startTime, endTime)) {
            return "Booking times must be within business hours (8:00 AM - 10:00 PM).";
        }

        return null; // No validation errors
    }

    private void handleBooking() {
        if (facility == null) {
            showAlert("No facility selected.");
            return;
        }

        // Get selected dates and times
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String startTimeStr = startTimeCombo.getValue();
        String endTimeStr = endTimeCombo.getValue();

        if (startDate == null || endDate == null || startTimeStr == null || endTimeStr == null) {
            showAlert("Please select both start and end date/time.");
            return;
        }

        // Parse times
        LocalTime startTime = LocalTime.parse(startTimeStr);
        LocalTime endTime = LocalTime.parse(endTimeStr);

        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        // Validate booking details
        String validationError = validateBookingDetails(currentUser, facility, startDateTime, endDateTime);
        if (validationError != null) {
            showAlert("Booking Error: " + validationError);
            return;
        }

        // Attempt to create booking
        Booking booking = BookingService.createBooking(currentUser, facility, startDateTime, endDateTime);
        if (booking != null) {
            showAlert("Booking created successfully!\n\n" +
                     "Facility: " + facility.getName() + "\n" +
                     "Date: " + startDateTime.toLocalDate() + "\n" +
                     "Time: " + startTimeStr + " - " + endTimeStr + "\n\n" +
                     "You can view your bookings in the 'My Bookings' section.");

            // Navigate back to facilities page to refresh the status
            if (navigateCallback != null) {
                navigateCallback.accept("facilities");
            }
        } else {
            showAlert("Failed to create booking. Please try again or contact support.");
        }
    }

    private String getStatusColor(FacilityStatus status) {
        switch (status) {
            case AVAILABLE: return "#27ae60"; // Green
            case BOOKED: return "#e74c3c"; // Red
            case MAINTENANCE: return "#f39c12"; // Orange
            case TEMPORARILY_CLOSED: return "#95a5a6"; // Gray
            case RESERVED: return "#9b59b6"; // Purple
            default: return "#95a5a6"; // Gray
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Facility Details");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String cleanFacilityName(String name, model.enums.FacilityType type) {
        if (name == null) return "";

        String cleanName = name.trim();

        // Remove common redundant prefixes
        cleanName = cleanName.replaceAll("(?i)^IIUM\\s+", "").trim();
        cleanName = cleanName.replaceAll("(?i)^Library\\s+", "").trim();

        // For facilities with parenthetical details, keep the core name + details
        // but avoid redundancy with the type label below
        String typeString = type.toString().replace("_", " ").toLowerCase();

        // For CARREL_ROOM, if name contains "Carrel Room", just remove "Room" to avoid duplication
        if (typeString.equals("carrel room") && cleanName.toLowerCase().contains("carrel room")) {
            cleanName = cleanName.replaceAll("(?i)room", "").trim();
        }
        // For COMPUTER_LAB, if name contains "Computer Lab", just remove "Lab" to avoid duplication
        else if (typeString.equals("computer lab") && cleanName.toLowerCase().contains("computer lab")) {
            cleanName = cleanName.replaceAll("(?i)lab", "").trim();
        }
        // For DISCUSSION_ROOM, if name contains "Discussion Room", just remove "Room" to avoid duplication
        else if (typeString.equals("discussion room") && cleanName.toLowerCase().contains("discussion room")) {
            cleanName = cleanName.replaceAll("(?i)room", "").trim();
        }
        // For other room types, remove "Room" only if it's redundant with the type
        else if (typeString.contains("room") && cleanName.toLowerCase().endsWith(" room")) {
            cleanName = cleanName.substring(0, cleanName.length() - 5).trim();
        }
        // For areas, remove "Area" only if it's redundant with the type
        else if (typeString.contains("area") && cleanName.toLowerCase().endsWith(" area")) {
            cleanName = cleanName.substring(0, cleanName.length() - 5).trim();
        }

        // If the name becomes too short or empty, use the original
        if (cleanName.length() < 2) {
            return name;
        }

        return cleanName.trim();
    }
}