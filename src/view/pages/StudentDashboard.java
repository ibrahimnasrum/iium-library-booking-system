package view.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Booking;
import model.Facility;
import model.Room;
import model.User;
import model.services.BookingService;
import model.services.BookingPolicy;
import model.services.FacilityService;
import view.components.FacilityCard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class StudentDashboard extends VBox {

    private User currentUser;
    private ObservableList<Facility> facilitiesList;
    private ObservableList<Facility> filteredFacilitiesList;
    private ObservableList<Booking> bookingsList;

    // UI Components
    private ScrollPane facilitiesScrollPane;
    private FlowPane facilitiesContainer;
    private TextField searchField;
    private ComboBox<String> filterLocationCombo;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private ComboBox<String> startTimeCombo;
    private ComboBox<String> endTimeCombo;
    private TableView<Booking> bookingsTable;
    private Label statusLabel;
    private TextArea facilityDetailsArea;
    private Facility selectedFacility;
    private HBox summaryCards;
    private Button bookButton;

    public StudentDashboard(User user) {
        this.currentUser = user;
        initializeComponents();
        setupLayout();
        loadData();
    }

    private void initializeComponents() {
        // Initialize data lists
        facilitiesList = FXCollections.observableArrayList();
        filteredFacilitiesList = FXCollections.observableArrayList();
        bookingsList = FXCollections.observableArrayList();

        // Search and filter components
        searchField = new TextField();
        searchField.setPromptText("Search facilities...");
        searchField.textProperty().addListener((obs, oldText, newText) -> filterFacilities());

        filterLocationCombo = new ComboBox<>();
        filterLocationCombo.getItems().addAll("All Locations", "Level 1", "Level 2", "Level 3");
        filterLocationCombo.setValue("All Locations");
        filterLocationCombo.setOnAction(e -> filterFacilities());

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

        // Create summary cards
        summaryCards = createSummaryCards();

        // Tables
        setupFacilitiesCards();
        setupBookingsTable();

        // Other components
        statusLabel = new Label("Welcome, " + currentUser.getName() + "!");
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        statusLabel.setTextFill(Color.BLUE);

        facilityDetailsArea = new TextArea();
        facilityDetailsArea.setEditable(false);
        facilityDetailsArea.setPrefRowCount(6);
    }

    private void setupFacilitiesCards() {
        facilitiesContainer = new FlowPane();
        facilitiesContainer.setHgap(20);
        facilitiesContainer.setVgap(20);
        facilitiesContainer.setPadding(new Insets(20));
        facilitiesContainer.setStyle("-fx-background-color: #f8f9fa;");

        facilitiesScrollPane = new ScrollPane(facilitiesContainer);
        facilitiesScrollPane.setFitToWidth(true);
        facilitiesScrollPane.setPrefHeight(400);
        facilitiesScrollPane.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #ddd;");

        updateFacilitiesDisplay();
    }

    private void updateFacilitiesDisplay() {
        facilitiesContainer.getChildren().clear();

        for (Facility facility : filteredFacilitiesList) {
            FacilityCard card = new FacilityCard(facility);

            // Make card clickable
            card.setOnMouseClicked(e -> {
                selectedFacility = facility;
                facilityDetailsArea.setText(facility.getDetailedInfo());
                updateBookingButtonState(); // Update button state when facility is selected
                // Highlight selected card
                for (var child : facilitiesContainer.getChildren()) {
                    child.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");
                }
                card.setStyle("-fx-background-color: #e3f2fd; -fx-border-color: #2196f3; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5;");
            });

            facilitiesContainer.getChildren().add(card);
        }
    }

    private void setupBookingsTable() {
        bookingsTable = new TableView<>();
        bookingsTable.setItems(bookingsList);

        TableColumn<Booking, String> idCol = new TableColumn<>("Booking ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
        idCol.setPrefWidth(100);

        TableColumn<Booking, String> facilityCol = new TableColumn<>("Facility");
        facilityCol.setCellValueFactory(new PropertyValueFactory<>("facilityId"));
        facilityCol.setPrefWidth(100);

        TableColumn<Booking, String> startCol = new TableColumn<>("Start Time");
        startCol.setCellValueFactory(new PropertyValueFactory<>("formattedStartTime"));
        startCol.setPrefWidth(150);

        TableColumn<Booking, String> endCol = new TableColumn<>("End Time");
        endCol.setCellValueFactory(new PropertyValueFactory<>("formattedEndTime"));
        endCol.setPrefWidth(150);

        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().getStatus().toString()));
        statusCol.setPrefWidth(100);

        bookingsTable.getColumns().addAll(idCol, facilityCol, startCol, endCol, statusCol);
    }

    private HBox createSummaryCards() {
        HBox cardsContainer = new HBox(20);
        cardsContainer.setAlignment(Pos.CENTER);

        // Get facility statistics
        List<Facility> allFacilities = FacilityService.getAllFacilities();
        long totalFacilities = allFacilities.size();
        long availableCount = allFacilities.stream().filter(f -> f.getStatus().toString().equals("AVAILABLE")).count();
        long bookedCount = allFacilities.stream().filter(f -> f.getStatus().toString().equals("BOOKED")).count();
        long closedCount = allFacilities.stream().filter(f ->
            f.getStatus().toString().equals("TEMPORARILY_CLOSED") ||
            f.getStatus().toString().equals("MAINTENANCE")).count();

        // Create summary cards
        VBox totalCard = createSummaryCard("🏢 Total Facilities", String.valueOf(totalFacilities), "#3b82f6");
        VBox availableCard = createSummaryCard("✅ Available", String.valueOf(availableCount), "#22c55e");
        VBox bookedCard = createSummaryCard("📅 Booked", String.valueOf(bookedCount), "#f59e0b");
        VBox closedCard = createSummaryCard("🚫 Closed/Maint", String.valueOf(closedCount), "#ef4444");

        cardsContainer.getChildren().addAll(totalCard, availableCard, bookedCard, closedCard);
        return cardsContainer;
    }

    private VBox createSummaryCard(String title, String value, String color) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(180, 100);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-padding: 16; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 18, 0.2, 0, 6);");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private void refreshSummaryCards() {
        // Get updated facility statistics
        List<Facility> allFacilities = FacilityService.getAllFacilities();
        long totalFacilities = allFacilities.size();
        long availableCount = allFacilities.stream().filter(f -> f.getStatus().toString().equals("AVAILABLE")).count();
        long bookedCount = allFacilities.stream().filter(f -> f.getStatus().toString().equals("BOOKED")).count();
        long closedCount = allFacilities.stream().filter(f ->
            f.getStatus().toString().equals("TEMPORARILY_CLOSED") ||
            f.getStatus().toString().equals("MAINTENANCE")).count();

        // Update existing cards if they exist
        if (summaryCards != null && summaryCards.getChildren().size() >= 4) {
            updateSummaryCard((VBox) summaryCards.getChildren().get(0), "🏢 Total Facilities", String.valueOf(totalFacilities));
            updateSummaryCard((VBox) summaryCards.getChildren().get(1), "✅ Available", String.valueOf(availableCount));
            updateSummaryCard((VBox) summaryCards.getChildren().get(2), "📅 Booked", String.valueOf(bookedCount));
            updateSummaryCard((VBox) summaryCards.getChildren().get(3), "🚫 Closed/Maint", String.valueOf(closedCount));
        }
    }

    private void updateSummaryCard(VBox card, String title, String value) {
        if (card.getChildren().size() >= 2) {
            Label titleLabel = (Label) card.getChildren().get(0);
            Label valueLabel = (Label) card.getChildren().get(1);
            titleLabel.setText(title);
            valueLabel.setText(value);
        }
    }

    private void updateBookingButtonState() {
        if (selectedFacility == null) {
            bookButton.setDisable(true);
            bookButton.setText("Book Selected Facility");
            return;
        }

        // Check if facility is available for booking
        boolean isAvailable = selectedFacility.getStatus().toString().equals("AVAILABLE");
        boolean isNotClosed = !selectedFacility.getStatus().toString().equals("TEMPORARILY_CLOSED") &&
                             !selectedFacility.getStatus().toString().equals("MAINTENANCE");

        if (!isAvailable || !isNotClosed) {
            bookButton.setDisable(true);
            bookButton.setText("Facility Not Available");
        } else {
            bookButton.setDisable(false);
            bookButton.setText("Book Selected Facility");
        }
    }

    private void setupLayout() {
        setSpacing(10);
        setPadding(new Insets(20));

        // Top section - Welcome and search
        HBox topSection = new HBox(10);
        topSection.setAlignment(Pos.CENTER_LEFT);
        topSection.getChildren().addAll(statusLabel);

        // Search and filter section
        HBox searchSection = new HBox(10);
        searchSection.setAlignment(Pos.CENTER_LEFT);
        searchSection.getChildren().addAll(
            new Label("Search:"), searchField,
            new Label("Location:"), filterLocationCombo
        );

        // Facilities section
        VBox facilitiesSection = new VBox(10);
        facilitiesSection.getChildren().addAll(
            new Label("Available Facilities:"),
            facilitiesScrollPane,
            new Label("Facility Details:"),
            facilityDetailsArea
        );

        // Booking section
        VBox bookingSection = new VBox(10);
        HBox dateTimeSection = new HBox(10);
        dateTimeSection.getChildren().addAll(
            new Label("Start Date:"), startDatePicker,
            new Label("Start Time:"), startTimeCombo,
            new Label("End Date:"), endDatePicker,
            new Label("End Time:"), endTimeCombo
        );

        bookButton = new Button("Book Selected Facility");
        bookButton.getStyleClass().add("primary-btn");
        bookButton.setOnAction(e -> handleBooking());
        bookButton.setDisable(true); // Initially disabled until facility is selected

        bookingSection.getChildren().addAll(
            new Label("Make a Booking:"),
            dateTimeSection,
            bookButton
        );

        // My bookings section
        VBox myBookingsSection = new VBox(10);
        Button cancelButton = new Button("Cancel Selected Booking");
        cancelButton.setOnAction(e -> handleCancellation());

        myBookingsSection.getChildren().addAll(
            new Label("My Bookings:"),
            bookingsTable,
            cancelButton
        );

        // Main layout
        getChildren().addAll(
            topSection,
            summaryCards,
            searchSection,
            facilitiesSection,
            bookingSection,
            myBookingsSection
        );
    }

    private void loadData() {
        // Load facilities
        List<Facility> facilities = FacilityService.getAllFacilities();
        facilitiesList.clear();
        facilitiesList.addAll(facilities);
        filterFacilities();

        // Load user's bookings
        List<Booking> userBookings = BookingService.getBookingsForUser(currentUser.getMatricNo());
        bookingsList.clear();
        bookingsList.addAll(userBookings);

        // Refresh summary cards
        refreshSummaryCards();

        // Update booking button state
        updateBookingButtonState();
    }

    private void filterFacilities() {
        List<Facility> filtered = facilitiesList.stream()
            .filter(f -> searchField.getText().isEmpty() ||
                        f.getName().toLowerCase().contains(searchField.getText().toLowerCase()) ||
                        f.getId().toLowerCase().contains(searchField.getText().toLowerCase()))
            .filter(f -> filterLocationCombo.getValue().equals("All Locations") ||
                        f.getLocation().contains(filterLocationCombo.getValue()))
            .filter(f -> BookingPolicy.canUserBookFacility(currentUser, f)) // Only show facilities user can book
            .collect(Collectors.toList());

        filteredFacilitiesList.clear();
        filteredFacilitiesList.addAll(filtered);
        updateFacilitiesDisplay();
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
        }

        // Validate booking duration (max 3 hours for discussion rooms, etc.)
        long hours = java.time.Duration.between(startTime, endTime).toHours();
        if (hours > 3) {
            showToast("❌ Bookings cannot exceed 3 hours.", "error");
            return;
        }

        Booking booking = BookingService.createBooking(currentUser, selectedFacility, startTime, endTime);

        if (booking != null) {
            showToast("✅ Booking successful! ID: " + booking.getBookingID(), "success");
            loadData(); // Refresh data
        } else {
            showToast("❌ Booking failed. Check availability and eligibility.", "error");
        }
    }

    private void handleCancellation() {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showAlert("Please select a booking to cancel.");
            return;
        }

        boolean success = BookingService.cancelBooking(currentUser, selectedBooking);
        if (success) {
            showToast("✅ Booking cancelled successfully.", "success");
            loadData(); // Refresh data
        } else {
            showToast("❌ Cancellation failed. Check booking ownership.", "error");
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
            statusLabel.setText("Welcome, " + currentUser.getName() + "!");
            statusLabel.getStyleClass().removeAll("status-success", "status-error", "status-info");
        });
        pause.play();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}