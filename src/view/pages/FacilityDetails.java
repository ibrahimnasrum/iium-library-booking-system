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
import model.Equipment;
import model.User;
import model.services.BookingService;
import model.services.BookingPolicy;
import model.enums.FacilityStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class FacilityDetails extends VBox {

    private User currentUser;
    private Facility facility;
    private Runnable onBookNow;
    private Runnable onBackToCatalog;

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

    public FacilityDetails(User user) {
        this.currentUser = user;
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

        idLabel = new Label();
        idLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 14px;");

        locationLabel = new Label();
        locationLabel.setStyle("-fx-font-size: 16px;");

        capacityLabel = new Label();
        capacityLabel.setStyle("-fx-font-size: 16px;");

        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        privilegeLabel = new Label();
        privilegeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        descriptionArea = new TextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setStyle("-fx-control-inner-background: #f8f9fa;");

        equipmentList = new VBox(5);
        equipmentList.setPadding(new Insets(10));

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

        DatePicker startDatePicker = new DatePicker(LocalDate.now());
        DatePicker endDatePicker = new DatePicker(LocalDate.now());

        ComboBox<String> startTimeCombo = new ComboBox<>();
        ComboBox<String> endTimeCombo = new ComboBox<>();

        // Populate time combos
        for (int hour = 8; hour <= 22; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                String time = String.format("%02d:%02d", hour, minute);
                startTimeCombo.getItems().add(time);
                endTimeCombo.getItems().add(time);
            }
        }
        startTimeCombo.setValue("09:00");
        endTimeCombo.setValue("10:00");

        dateTimeBox.getChildren().addAll(
            new Label("Start:"), startDatePicker, startTimeCombo,
            new Label("End:"), endDatePicker, endTimeCombo
        );

        // Book button
        Button bookButton = new Button("Book Now");
        bookButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
        bookButton.setOnAction(e -> handleBooking(startDatePicker, endDatePicker, startTimeCombo, endTimeCombo));

        // Booking rules
        Label rulesLabel = new Label("Booking Rules:");
        rulesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextArea rulesArea = new TextArea();
        rulesArea.setEditable(false);
        rulesArea.setWrapText(true);
        rulesArea.setPrefRowCount(3);
        rulesArea.setText(getBookingRules());

        bookingSection.getChildren().addAll(bookingTitle, dateTimeBox, bookButton, rulesLabel, rulesArea);
    }

    private void setupLayout() {
        setSpacing(20);
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #f8f9fa;");

        // Back button
        Button backButton = new Button("← Back to Catalog");
        backButton.setOnAction(e -> {
            if (onBackToCatalog != null) onBackToCatalog.run();
        });

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
        getChildren().addAll(backButton, mainContent, bookingSection);
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
        nameLabel.setText(facility.getName());
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
        rules.append("• Business hours: ").append(BookingPolicy.getBusinessHours()).append("\n");
        rules.append("• ").append(BookingPolicy.getCancellationPolicy()).append("\n");

        if (facility != null && facility.getPrivilege() != model.enums.ReservationPrivilege.OPEN) {
            rules.append("• Access restriction: ").append(facility.getPrivilege().toString()).append("\n");
        }

        return rules.toString();
    }

    private void handleBooking(DatePicker startDate, DatePicker endDate,
                              ComboBox<String> startTime, ComboBox<String> endTime) {
        if (facility == null) return;

        LocalDate startDateValue = startDate.getValue();
        LocalDate endDateValue = endDate.getValue();
        String startTimeStr = startTime.getValue();
        String endTimeStr = endTime.getValue();

        if (startDateValue == null || endDateValue == null || startTimeStr == null || endTimeStr == null) {
            showAlert("Please select both start and end date/time.");
            return;
        }

        LocalDateTime startDateTime = LocalDateTime.of(startDateValue, LocalTime.parse(startTimeStr));
        LocalDateTime endDateTime = LocalDateTime.of(endDateValue, LocalTime.parse(endTimeStr));

        model.Booking booking = BookingService.createBooking(currentUser, facility, startDateTime, endDateTime);

        if (booking != null) {
            showAlert("Booking successful! Booking ID: " + booking.getBookingID());
            if (onBookNow != null) onBookNow.run();
        } else {
            showAlert("Booking failed. Please check availability and your eligibility.");
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

    public void setOnBookNow(Runnable handler) {
        this.onBookNow = handler;
    }

    public void setOnBackToCatalog(Runnable handler) {
        this.onBackToCatalog = handler;
    }
}