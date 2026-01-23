package view.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Booking;
import model.Facility;
import model.User;
import model.services.BookingService;
import model.services.BookingPolicy;
import model.services.FacilityService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class BookingPage extends VBox {

    private User currentUser;
    private Facility selectedFacility;

    // UI Components
    private ComboBox<String> facilityCombo;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private ComboBox<String> startTimeCombo;
    private ComboBox<String> endTimeCombo;
    private Button bookButton;
    private TextArea bookingSummaryArea;
    private Label statusLabel;
    private Runnable onBookingChangedCallback;

    public BookingPage(User user) {
        this(user, null);
    }

    public BookingPage(User user, Runnable onBookingChangedCallback) {
        this.currentUser = user;
        this.onBookingChangedCallback = onBookingChangedCallback;
        initializeComponents();
        setupLayout();
        loadData();
    }

    private void initializeComponents() {
        // Facility selection
        facilityCombo = new ComboBox<>();
        facilityCombo.setPromptText("Select a facility...");
        facilityCombo.setPrefWidth(300);
        facilityCombo.setStyle("-fx-font-size: 14px;");
        facilityCombo.setOnAction(e -> updateSelectedFacility());

        // Date and time pickers
        startDatePicker = new DatePicker(LocalDate.now());
        endDatePicker = new DatePicker(LocalDate.now());
        startTimeCombo = new ComboBox<>();
        endTimeCombo = new ComboBox<>();

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

        // Book button
        bookButton = new Button("📅 Confirm Booking");
        bookButton.getStyleClass().add("primary-btn");
        bookButton.setStyle("-fx-font-size: 16px; -fx-padding: 12 24; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 8;");
        bookButton.setOnAction(e -> handleBooking());

        // Booking summary
        bookingSummaryArea = new TextArea();
        bookingSummaryArea.setEditable(false);
        bookingSummaryArea.setPrefRowCount(6);
        bookingSummaryArea.setStyle("-fx-font-size: 14px; -fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5;");

        // Status label
        statusLabel = new Label("Ready to make a booking");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
    }

    private void setupLayout() {
        setSpacing(25);
        setPadding(new Insets(30));
        setStyle("-fx-background-color: linear-gradient(to bottom, #e8f5e8 0%, #f1f8e9 100%);");

        // Header
        VBox headerBox = createHeader();

        // Facility selection section
        VBox facilitySection = createFacilitySection();

        // Date and time section
        VBox dateTimeSection = createDateTimeSection();

        // Booking summary section
        VBox summarySection = createSummarySection();

        // Action buttons section
        VBox actionSection = createActionSection();

        getChildren().addAll(headerBox, facilitySection, dateTimeSection, summarySection, actionSection);
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label titleLabel = new Label("📅 Make a Booking");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        Label subtitleLabel = new Label("Reserve your preferred facility for study or meetings");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        subtitleLabel.setStyle("-fx-text-fill: #7f8c8d;");

        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }

    private VBox createFacilitySection() {
        VBox facilityBox = new VBox(15);
        facilityBox.setPadding(new Insets(20));
        facilityBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.1, 0, 3);");

        Label facilityTitle = new Label("🏢 Select Facility");
        facilityTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        facilityTitle.setStyle("-fx-text-fill: #2c3e50;");

        HBox facilityRow = new HBox(15);
        facilityRow.setAlignment(Pos.CENTER_LEFT);
        facilityRow.getChildren().addAll(
            new Label("Facility:"), facilityCombo
        );

        facilityBox.getChildren().addAll(facilityTitle, facilityRow);
        return facilityBox;
    }

    private VBox createDateTimeSection() {
        VBox dateTimeBox = new VBox(15);
        dateTimeBox.setPadding(new Insets(20));
        dateTimeBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.1, 0, 3);");

        Label dateTimeTitle = new Label("🕐 Select Date & Time");
        dateTimeTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        dateTimeTitle.setStyle("-fx-text-fill: #2c3e50;");

        HBox startRow = new HBox(15);
        startRow.setAlignment(Pos.CENTER_LEFT);
        startRow.getChildren().addAll(
            new Label("Start Date:"), startDatePicker,
            new Label("Start Time:"), startTimeCombo
        );

        HBox endRow = new HBox(15);
        endRow.setAlignment(Pos.CENTER_LEFT);
        endRow.getChildren().addAll(
            new Label("End Date:"), endDatePicker,
            new Label("End Time:"), endTimeCombo
        );

        Label noteLabel = new Label("💡 Note: Maximum booking duration is 3 hours");
        noteLabel.setStyle("-fx-text-fill: #f39c12; -fx-font-size: 12px;");

        dateTimeBox.getChildren().addAll(dateTimeTitle, startRow, endRow, noteLabel);
        return dateTimeBox;
    }

    private VBox createSummarySection() {
        VBox summaryBox = new VBox(15);
        summaryBox.setPadding(new Insets(20));
        summaryBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.1, 0, 3);");

        Label summaryTitle = new Label("📋 Booking Summary");
        summaryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        summaryTitle.setStyle("-fx-text-fill: #2c3e50;");

        summaryBox.getChildren().addAll(summaryTitle, bookingSummaryArea);
        return summaryBox;
    }

    private VBox createActionSection() {
        VBox actionBox = new VBox(15);
        actionBox.setAlignment(Pos.CENTER);
        actionBox.setPadding(new Insets(20));

        actionBox.getChildren().addAll(bookButton, statusLabel);
        return actionBox;
    }

    private void updateSelectedFacility() {
        String selectedFacilityId = facilityCombo.getValue();
        if (selectedFacilityId != null) {
            selectedFacility = FacilityService.findFacilityById(selectedFacilityId);
            updateBookingSummary();
        }
    }

    private void updateBookingSummary() {
        if (selectedFacility == null) {
            bookingSummaryArea.setText("Please select a facility first.");
            return;
        }

        StringBuilder summary = new StringBuilder();
        summary.append("🏢 Facility: ").append(selectedFacility.getName()).append("\n");
        summary.append("📍 Location: ").append(selectedFacility.getLocation()).append("\n");
        summary.append("🏷️ Type: ").append(selectedFacility.getType()).append("\n");
        summary.append("📊 Status: ").append(selectedFacility.getStatus()).append("\n\n");

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String startTime = startTimeCombo.getValue();
        String endTime = endTimeCombo.getValue();

        if (startDate != null && endDate != null && startTime != null && endTime != null) {
            summary.append("📅 Start: ").append(startDate).append(" ").append(startTime).append("\n");
            summary.append("🏁 End: ").append(endDate).append(" ").append(endTime).append("\n");

            LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.parse(startTime));
            LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.parse(endTime));

            long hours = java.time.Duration.between(startDateTime, endDateTime).toHours();
            summary.append("⏱️ Duration: ").append(hours).append(" hours\n");

            if (hours > 3) {
                summary.append("\n⚠️ Warning: Booking exceeds 3-hour limit!");
            }
        }

        bookingSummaryArea.setText(summary.toString());
    }

    private void loadData() {
        // Load available facilities
        List<Facility> facilities = FacilityService.getAllFacilities();
        facilityCombo.getItems().clear();

        for (Facility facility : facilities) {
            // Only show available facilities that user can book
            if (facility.getStatus().toString().equals("AVAILABLE") &&
                BookingPolicy.canUserBookFacility(currentUser, facility)) {
                facilityCombo.getItems().add(facility.getId() + " - " + facility.getName());
            }
        }

        // Add listeners for real-time summary updates
        startDatePicker.setOnAction(e -> updateBookingSummary());
        endDatePicker.setOnAction(e -> updateBookingSummary());
        startTimeCombo.setOnAction(e -> updateBookingSummary());
        endTimeCombo.setOnAction(e -> updateBookingSummary());
    }

    private void handleBooking() {
        if (selectedFacility == null) {
            showToast("❌ Please select a facility to book.", "error");
            return;
        }

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String startTimeStr = startTimeCombo.getValue();
        String endTimeStr = endTimeCombo.getValue();

        if (startDate == null || endDate == null || startTimeStr == null || endTimeStr == null) {
            showToast("❌ Please select both start and end date/time.", "error");
            return;
        }

        LocalDateTime startTime = LocalDateTime.of(startDate, LocalTime.parse(startTimeStr));
        LocalDateTime endTime = LocalDateTime.of(endDate, LocalTime.parse(endTimeStr));

        // Auto-fix: if end time is before start time, assume next day
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            endTime = endTime.plusDays(1);
            endDatePicker.setValue(endTime.toLocalDate());
            endTimeCombo.setValue(endTime.toLocalTime().toString());
            showToast("ℹ️ End time adjusted to next day.", "info");
            updateBookingSummary();
        }

        // Validate booking duration (max 3 hours)
        long hours = java.time.Duration.between(startTime, endTime).toHours();
        if (hours > 3) {
            showToast("❌ Bookings cannot exceed 3 hours.", "error");
            return;
        }

        Booking booking = BookingService.createBooking(currentUser, selectedFacility, startTime, endTime);

        if (booking != null) {
            showToast("✅ Booking successful! ID: " + booking.getBookingID(), "success");
            // Reset form
            facilityCombo.setValue(null);
            selectedFacility = null;
            bookingSummaryArea.clear();
            loadData(); // Refresh available facilities

            // Notify that booking status changed
            if (onBookingChangedCallback != null) {
                onBookingChangedCallback.run();
            }
        } else {
            showToast("❌ Booking failed. Check availability and eligibility.", "error");
        }
    }

    private void showToast(String message, String type) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("status-success", "status-error", "status-info");

        switch (type) {
            case "success" -> statusLabel.getStyleClass().add("status-success");
            case "error" -> statusLabel.getStyleClass().add("status-error");
            case "info" -> statusLabel.getStyleClass().add("status-info");
        }

        // Auto-hide toast after 3 seconds
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
        pause.setOnFinished(e -> {
            statusLabel.setText("Ready to make a booking");
            statusLabel.getStyleClass().removeAll("status-success", "status-error", "status-info");
        });
        pause.play();
    }
}