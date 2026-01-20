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
    private ComboBox<String> filterTypeCombo;
    private ComboBox<String> filterLocationCombo;
    private ComboBox<String> filterStatusCombo;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private ComboBox<String> startTimeCombo;
    private ComboBox<String> endTimeCombo;
    private TableView<Booking> bookingsTable;
    private Label statusLabel;
    private TextArea facilityDetailsArea;
    private Facility selectedFacility;

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

        filterTypeCombo = new ComboBox<>();
        filterTypeCombo.getItems().addAll("All Types", "Room", "Study Area", "Computer Lab", "Auditorium", "Discussion Room");
        filterTypeCombo.setValue("All Types");
        filterTypeCombo.setOnAction(e -> filterFacilities());

        filterLocationCombo = new ComboBox<>();
        filterLocationCombo.getItems().addAll("All Locations", "Level 1", "Level 2", "Level 3");
        filterLocationCombo.setValue("All Locations");
        filterLocationCombo.setOnAction(e -> filterFacilities());

        filterStatusCombo = new ComboBox<>();
        filterStatusCombo.getItems().addAll("All Status", "Available", "Booked");
        filterStatusCombo.setValue("All Status");
        filterStatusCombo.setOnAction(e -> filterFacilities());

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
            new Label("Type:"), filterTypeCombo,
            new Label("Location:"), filterLocationCombo,
            new Label("Status:"), filterStatusCombo
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

        Button bookButton = new Button("Book Selected Facility");
        bookButton.setOnAction(e -> handleBooking());

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
    }

    private void filterFacilities() {
        List<Facility> filtered = facilitiesList.stream()
            .filter(f -> searchField.getText().isEmpty() ||
                        f.getName().toLowerCase().contains(searchField.getText().toLowerCase()) ||
                        f.getId().toLowerCase().contains(searchField.getText().toLowerCase()))
            .filter(f -> filterTypeCombo.getValue().equals("All Types") ||
                        f.getType().toString().contains(filterTypeCombo.getValue()))
            .filter(f -> filterLocationCombo.getValue().equals("All Locations") ||
                        f.getLocation().contains(filterLocationCombo.getValue()))
            .filter(f -> filterStatusCombo.getValue().equals("All Status") ||
                        f.getStatus().toString().equalsIgnoreCase(filterStatusCombo.getValue()))
            .collect(Collectors.toList());

        filteredFacilitiesList.clear();
        filteredFacilitiesList.addAll(filtered);
        updateFacilitiesDisplay();
    }

    private void handleBooking() {
        if (selectedFacility == null) {
            showAlert("Please select a facility to book.");
            return;
        }

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String startTimeStr = startTimeCombo.getValue();
        String endTimeStr = endTimeCombo.getValue();

        if (startDate == null || endDate == null || startTimeStr == null || endTimeStr == null) {
            showAlert("Please select both start and end date/time.");
            return;
        }

        LocalDateTime startTime = LocalDateTime.of(startDate, LocalTime.parse(startTimeStr));
        LocalDateTime endTime = LocalDateTime.of(endDate, LocalTime.parse(endTimeStr));

        Booking booking = BookingService.createBooking(currentUser, selectedFacility, startTime, endTime);

        if (booking != null) {
            statusLabel.setText("Booking successful! Booking ID: " + booking.getBookingID());
            statusLabel.setTextFill(Color.GREEN);
            loadData(); // Refresh data
        } else {
            showAlert("Booking failed. Please check the facility availability and your eligibility.");
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
            statusLabel.setText("Booking cancelled successfully.");
            statusLabel.setTextFill(Color.ORANGE);
            loadData(); // Refresh data
        } else {
            showAlert("Cancellation failed. You can only cancel your own active bookings.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}