package view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.User;
import model.Room;
import model.Booking;
import model.enums.Role;
import model.Booking;
import model.Room;
import model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class BookingView extends Application {
    
    private User currentUser;
    private ObservableList<Room> roomsList;
    private ObservableList<Room> filteredRoomsList;
    private ObservableList<Booking> bookingsList;
    
    // UI Components
    private TableView<Room> roomsTable;
    private TextField searchField;
    private ComboBox<String> filterTypeCombo;
    private ComboBox<String> filterLocationCombo;
    private ComboBox<String> filterEligibilityCombo;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private ComboBox<String> startTimeCombo;
    private ComboBox<String> endTimeCombo;
    private TableView<Booking> bookingsTable;
    private Label statusLabel;
    private TextArea roomDetailsArea;
    private Room selectedRoom;

    @Override
    public void start(Stage primaryStage) {
        // Initialize demo user
        currentUser = new User("2112345", "password123", "Ibrahim", Role.STUDENT);
        roomsList = FXCollections.observableArrayList(User.getAllRooms());
        filteredRoomsList = FXCollections.observableArrayList(roomsList);
        bookingsList = FXCollections.observableArrayList(currentUser.getMyBookings());

        // Main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #ecf0f1;");

        // Top: Title and User Info
        VBox topSection = createTopSection();
        mainLayout.setTop(topSection);

        // Center: Split between Room List and Booking Form
        SplitPane centerSplit = new SplitPane();
        centerSplit.setDividerPositions(0.55);
        
        VBox leftSection = createRoomListSection();
        VBox rightSection = createBookingFormSection();
        
        centerSplit.getItems().addAll(leftSection, rightSection);
        mainLayout.setCenter(centerSplit);

        // Bottom: My Bookings Table
        VBox bottomSection = createMyBookingsSection();
        mainLayout.setBottom(bottomSection);

        // Scene and Stage
        Scene scene = new Scene(mainLayout, 1200, 800);
        primaryStage.setTitle("IIUM Library Booking System - Room Booking Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createTopSection() {
        VBox topBox = new VBox(10);
        topBox.setPadding(new Insets(15));
        topBox.setAlignment(Pos.CENTER);
        topBox.setStyle("-fx-background-color: #2c3e50;");

        Label titleLabel = new Label("IIUM Library Room Booking System");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.WHITE);

        Label userLabel = new Label("Student: " + currentUser.getName() + " | Matric No: " + currentUser.getMatricNo());
        userLabel.setFont(Font.font("Arial", 13));
        userLabel.setTextFill(Color.web("#ecf0f1"));

        topBox.getChildren().addAll(titleLabel, userLabel);
        return topBox;
    }

    private VBox createRoomListSection() {
        VBox roomBox = new VBox(10);
        roomBox.setPadding(new Insets(15));
        roomBox.setStyle("-fx-background-color: white;");

        Label roomTitle = new Label("Available Rooms & Facilities");
        roomTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        roomTitle.setTextFill(Color.web("#34495e"));

        // Search Bar
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        Label searchLabel = new Label("Search:");
        searchLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        searchField = new TextField();
        searchField.setPromptText("Search by room ID, type, or location...");
        searchField.setPrefWidth(300);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        searchBox.getChildren().addAll(searchLabel, searchField);

        // Filter Controls
        HBox filterBox1 = new HBox(10);
        filterBox1.setAlignment(Pos.CENTER_LEFT);
        
        Label typeLabel = new Label("Type:");
        typeLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 11));
        filterTypeCombo = new ComboBox<>();
        filterTypeCombo.getItems().addAll("All Types", "Student Lounge", "Carrel Room", "Discussion Room", 
            "Research Room", "Viewing Room", "Exhibition Area", "Computer Lab", "Multi Purpose Room", 
            "MBSB AZKA-PPZ Corner", "Library Auditorium");
        filterTypeCombo.setValue("All Types");
        filterTypeCombo.setPrefWidth(180);
        filterTypeCombo.setOnAction(e -> applyFilters());
        
        Label locationLabel = new Label("Location:");
        locationLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 11));
        filterLocationCombo = new ComboBox<>();
        filterLocationCombo.getItems().addAll("All Locations", "Level 1", "Level 2", "Level 3");
        filterLocationCombo.setValue("All Locations");
        filterLocationCombo.setPrefWidth(130);
        filterLocationCombo.setOnAction(e -> applyFilters());
        
        filterBox1.getChildren().addAll(typeLabel, filterTypeCombo, locationLabel, filterLocationCombo);

        HBox filterBox2 = new HBox(10);
        filterBox2.setAlignment(Pos.CENTER_LEFT);
        
        Label eligibilityLabel = new Label("Eligibility:");
        eligibilityLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 11));
        filterEligibilityCombo = new ComboBox<>();
        filterEligibilityCombo.getItems().addAll("All", "Open", "Staff Only", "Postgraduate Only", 
            "Staff & Students", "Academic Staff Only", "Special Needs/Disabled");
        filterEligibilityCombo.setValue("All");
        filterEligibilityCombo.setPrefWidth(180);
        filterEligibilityCombo.setOnAction(e -> applyFilters());
        
        Button showAvailableBtn = new Button("Show Available Only");
        showAvailableBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        showAvailableBtn.setOnAction(e -> showAvailableRooms());
        
        Button showAllBtn = new Button("Show All Rooms");
        showAllBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        showAllBtn.setOnAction(e -> showAllRooms());
        
        filterBox2.getChildren().addAll(eligibilityLabel, filterEligibilityCombo, showAvailableBtn, showAllBtn);

        // Rooms Table
        roomsTable = new TableView<>();
        roomsTable.setItems(filteredRoomsList);
        roomsTable.setPrefHeight(250);
        
        TableColumn<Room, String> idCol = new TableColumn<>("Room ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        idCol.setPrefWidth(100);

        TableColumn<Room, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(180);

        TableColumn<Room, Integer> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        capacityCol.setPrefWidth(80);

        TableColumn<Room, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationCol.setPrefWidth(120);

        TableColumn<Room, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("availabilityStatus"));
        statusCol.setPrefWidth(90);
        statusCol.setCellFactory(column -> new TableCell<Room, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if (status.equals("Available")) {
                        setStyle("-fx-background-color: #d5f4e6; -fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    } else if (status.equals("Booked")) {
                        setStyle("-fx-background-color: #fadbd8; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    } else if (status.equals("Closed")) {
                        setStyle("-fx-background-color: #e8e8e8; -fx-text-fill: #95a5a6; -fx-font-weight: bold;");
                    }
                }
            }
        });

        roomsTable.getColumns().addAll(idCol, typeCol, capacityCol, locationCol, statusCol);
        roomsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedRoom = newSelection;
                displayRoomDetails(newSelection);
            }
        });

        // Room Details Area
        Label detailsLabel = new Label("Room Details & Information");
        detailsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        roomDetailsArea = new TextArea();
        roomDetailsArea.setEditable(false);
        roomDetailsArea.setPrefHeight(120);
        roomDetailsArea.setWrapText(true);
        roomDetailsArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
        roomDetailsArea.setText("Select a room to view details...");

        Label roomCountLabel = new Label("Total Rooms: " + roomsList.size());
        roomCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        roomCountLabel.setTextFill(Color.web("#7f8c8d"));

        roomBox.getChildren().addAll(roomTitle, new Separator(), searchBox, filterBox1, filterBox2, 
            new Separator(), roomsTable, detailsLabel, roomDetailsArea, roomCountLabel);
        return roomBox;
    }

    private VBox createBookingFormSection() {
        VBox formBox = new VBox(12);
        formBox.setPadding(new Insets(15));
        formBox.setStyle("-fx-background-color: white;");

        Label formTitle = new Label("Make a New Booking");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        formTitle.setTextFill(Color.web("#34495e"));

        // Selected Room Display
        Label selectedRoomLabel = new Label("Selected Room: (Select from table)");
        selectedRoomLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        selectedRoomLabel.setTextFill(Color.web("#e74c3c"));
        selectedRoomLabel.setWrapText(true);
        
        // Update label when room is selected
        roomsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedRoomLabel.setText("Selected: " + newVal.getRoomID() + " - " + newVal.getType());
                selectedRoomLabel.setTextFill(Color.web("#27ae60"));
            }
        });

        // Start Date & Time
        GridPane dateTimeGrid = new GridPane();
        dateTimeGrid.setHgap(10);
        dateTimeGrid.setVgap(10);
        
        Label startLabel = new Label("Start Date & Time:");
        startLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        
        startDatePicker = new DatePicker(LocalDate.now());
        startDatePicker.setPrefWidth(150);
        
        startTimeCombo = new ComboBox<>(getTimeSlots());
        startTimeCombo.setValue("09:00");
        startTimeCombo.setPrefWidth(100);

        Label endLabel = new Label("End Date & Time:");
        endLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        
        endDatePicker = new DatePicker(LocalDate.now());
        endDatePicker.setPrefWidth(150);
        
        endTimeCombo = new ComboBox<>(getTimeSlots());
        endTimeCombo.setValue("12:00");
        endTimeCombo.setPrefWidth(100);

        dateTimeGrid.add(startLabel, 0, 0);
        dateTimeGrid.add(startDatePicker, 1, 0);
        dateTimeGrid.add(startTimeCombo, 2, 0);
        dateTimeGrid.add(endLabel, 0, 1);
        dateTimeGrid.add(endDatePicker, 1, 1);
        dateTimeGrid.add(endTimeCombo, 2, 1);

        // Booking Duration Info
        Label durationInfo = new Label("Most rooms: 3-hour maximum booking (Extendable if not reserved by others)");
        durationInfo.setFont(Font.font("Arial", 11));
        durationInfo.setTextFill(Color.web("#7f8c8d"));
        durationInfo.setWrapText(true);

        // Buttons
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Button bookNowBtn = new Button("BOOK NOW");
        bookNowBtn.setMaxWidth(Double.MAX_VALUE);
        bookNowBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 12 30;");
        bookNowBtn.setOnAction(e -> handleBookNow());

        Button cancelBookingBtn = new Button("CANCEL SELECTED BOOKING");
        cancelBookingBtn.setMaxWidth(Double.MAX_VALUE);
        cancelBookingBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 30;");
        cancelBookingBtn.setOnAction(e -> handleCancelBooking());

        Button refreshBtn = new Button("REFRESH ALL DATA");
        refreshBtn.setMaxWidth(Double.MAX_VALUE);
        refreshBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 30;");
        refreshBtn.setOnAction(e -> refreshView());

        buttonBox.getChildren().addAll(bookNowBtn, cancelBookingBtn, refreshBtn);

        // Status Label
        statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        statusLabel.setWrapText(true);
        statusLabel.setPadding(new Insets(10));
        statusLabel.setStyle("-fx-background-radius: 5; -fx-padding: 10;");

        // Instructions
        VBox instructionBox = new VBox(5);
        instructionBox.setStyle("-fx-background-color: #e8f4f8; -fx-padding: 10; -fx-background-radius: 5;");
        Label instructionTitle = new Label("How to Book:");
        instructionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Label inst1 = new Label("1. Select a room from the table");
        Label inst2 = new Label("2. Choose date & time");
        Label inst3 = new Label("3. Click 'BOOK NOW'");
        Label inst4 = new Label("4. View your bookings below");
        inst1.setFont(Font.font("Arial", 11));
        inst2.setFont(Font.font("Arial", 11));
        inst3.setFont(Font.font("Arial", 11));
        inst4.setFont(Font.font("Arial", 11));
        instructionBox.getChildren().addAll(instructionTitle, inst1, inst2, inst3, inst4);

        formBox.getChildren().addAll(formTitle, new Separator(), selectedRoomLabel, 
            new Separator(), dateTimeGrid, durationInfo, buttonBox, statusLabel, 
            new Separator(), instructionBox);
        return formBox;
    }

    private VBox createMyBookingsSection() {
        VBox bookingsBox = new VBox(10);
        bookingsBox.setPadding(new Insets(15));
        bookingsBox.setStyle("-fx-background-color: white;");

        Label bookingsTitle = new Label("My Bookings");
        bookingsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        bookingsTitle.setTextFill(Color.web("#34495e"));

        // TableView for bookings
        bookingsTable = new TableView<>();
        bookingsTable.setItems(bookingsList);
        bookingsTable.setPrefHeight(180);

        TableColumn<Booking, String> idCol = new TableColumn<>("Booking ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
        idCol.setPrefWidth(100);

        TableColumn<Booking, String> roomCol = new TableColumn<>("Room ID");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        roomCol.setPrefWidth(100);

        TableColumn<Booking, String> startCol = new TableColumn<>("Start Time");
        startCol.setCellValueFactory(new PropertyValueFactory<>("formattedStartTime"));
        startCol.setPrefWidth(150);

        TableColumn<Booking, String> endCol = new TableColumn<>("End Time");
        endCol.setCellValueFactory(new PropertyValueFactory<>("formattedEndTime"));
        endCol.setPrefWidth(150);

        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);
        statusCol.setCellFactory(column -> new TableCell<Booking, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if (status.equals("Active")) {
                        setStyle("-fx-background-color: #d5f4e6; -fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-background-color: #fadbd8; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    }
                }
            }
        });

        bookingsTable.getColumns().addAll(idCol, roomCol, startCol, endCol, statusCol);

        Label bookingCount = new Label("Total Bookings: 0");
        bookingCount.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        bookingCount.setTextFill(Color.web("#7f8c8d"));
        
        bookingsList.addListener((javafx.collections.ListChangeListener.Change<? extends Booking> c) -> {
            long activeCount = bookingsList.stream().filter(b -> b.getStatus().equals("Active")).count();
            bookingCount.setText("Total Bookings: " + bookingsList.size() + " | Active: " + activeCount);
        });

        bookingsBox.getChildren().addAll(bookingsTitle, bookingsTable, bookingCount);
        return bookingsBox;
    }

    private void displayRoomDetails(Room room) {
        StringBuilder details = new StringBuilder();
        details.append("===================================================\n");
        details.append("ROOM INFORMATION\n");
        details.append("===================================================\n\n");
        details.append("Room ID:         ").append(room.getRoomID()).append("\n");
        details.append("Type:            ").append(room.getType()).append("\n");
        details.append("Location:        ").append(room.getLocation()).append("\n");
        details.append("Capacity:        ").append(room.getCapacity()).append(room.getCapacity() > 0 ? " persons" : " (N/A)").append("\n");
        details.append("Status:          ").append(room.getAvailabilityStatus()).append("\n");
        details.append("Equipment:       ").append(room.getEquipment()).append("\n");
        details.append("Eligibility:     ").append(room.getEligibility()).append("\n");
        if (!room.getNotes().isEmpty()) {
            details.append("Notes:           ").append(room.getNotes()).append("\n");
        }
        details.append("\n===================================================");
        
        roomDetailsArea.setText(details.toString());
    }

    private ObservableList<String> getTimeSlots() {
        ObservableList<String> times = FXCollections.observableArrayList();
        for (int hour = 8; hour <= 22; hour++) {
            times.add(String.format("%02d:00", hour));
            times.add(String.format("%02d:30", hour));
        }
        return times;
    }

    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase();
        String typeFilter = filterTypeCombo.getValue();
        String locationFilter = filterLocationCombo.getValue();
        String eligibilityFilter = filterEligibilityCombo.getValue();

        List<Room> filtered = roomsList.stream()
            .filter(room -> {
                // Search filter
                boolean matchesSearch = searchText.isEmpty() ||
                    room.getRoomID().toLowerCase().contains(searchText) ||
                    room.getType().toString().toLowerCase().contains(searchText) ||
                    room.getLocation().toLowerCase().contains(searchText);

                // Type filter
                boolean matchesType = typeFilter.equals("All Types") ||
                    room.getType().toString().contains(typeFilter);

                // Location filter
                boolean matchesLocation = locationFilter.equals("All Locations") || 
                    room.getLocation().contains(locationFilter);

                // Eligibility filter
                boolean matchesEligibility = eligibilityFilter.equals("All") || 
                    room.getEligibility().equals(eligibilityFilter);

                return matchesSearch && matchesType && matchesLocation && matchesEligibility;
            })
            .collect(Collectors.toList());

        filteredRoomsList.setAll(filtered);
        showStatus("Showing " + filtered.size() + " of " + roomsList.size() + " rooms", "#3498db");
    }

    private void showAvailableRooms() {
        List<Room> available = roomsList.stream()
            .filter(room -> room.getAvailabilityStatus().equals("Available"))
            .collect(Collectors.toList());
        filteredRoomsList.setAll(available);
        showStatus("Showing " + available.size() + " available rooms", "#27ae60");
    }

    private void showAllRooms() {
        filteredRoomsList.setAll(roomsList);
        searchField.clear();
        filterTypeCombo.setValue("All Types");
        filterLocationCombo.setValue("All Locations");
        filterEligibilityCombo.setValue("All");
        showStatus("Showing all " + roomsList.size() + " rooms", "#3498db");
    }

    private void handleBookNow() {
        // Validate room selection
        if (selectedRoom == null) {
            showStatus("Please select a room from the table first!", "#e74c3c");
            return;
        }

        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            showStatus("Please select start and end dates!", "#e74c3c");
            return;
        }

        if (startTimeCombo.getValue() == null || endTimeCombo.getValue() == null) {
            showStatus("Please select start and end times!", "#e74c3c");
            return;
        }

        // Check if room is available
        if (!selectedRoom.getAvailabilityStatus().equals("Available")) {
            showStatus("Selected room is " + selectedRoom.getAvailabilityStatus() + "!", "#e74c3c");
            return;
        }

        // Create date-time objects
        LocalTime startTime = LocalTime.parse(startTimeCombo.getValue());
        LocalTime endTime = LocalTime.parse(endTimeCombo.getValue());
        LocalDateTime startDateTime = LocalDateTime.of(startDatePicker.getValue(), startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDatePicker.getValue(), endTime);

        // Validate time range
        if (endDateTime.isBefore(startDateTime) || endDateTime.isEqual(startDateTime)) {
            showStatus("End time must be after start time!", "#e74c3c");
            return;
        }

        // Make booking
        boolean success = currentUser.makeBooking(selectedRoom, startDateTime, endDateTime);
        
        if (success) {
            showStatus("SUCCESS! Booking confirmed for " + selectedRoom.getRoomID() + " - " + selectedRoom.getType(), "#27ae60");
            refreshView();
            
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Confirmed");
            alert.setHeaderText("Your booking has been successfully created!");
            alert.setContentText("Room: " + selectedRoom.getRoomID() + " - " + selectedRoom.getType() + 
                "\nStart: " + startDateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                "\nEnd: " + endDateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            alert.showAndWait();
        } else {
            showStatus("Booking failed! Please try again.", "#e74c3c");
        }
    }

    private void handleCancelBooking() {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        
        if (selectedBooking == null) {
            showStatus("Please select a booking from 'My Bookings' table to cancel!", "#e74c3c");
            return;
        }

        if (selectedBooking.getStatus().equals("Cancelled")) {
            showStatus("This booking is already cancelled!", "#e74c3c");
            return;
        }

        // Confirm cancellation
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Cancel Booking");
        confirmAlert.setHeaderText("Are you sure you want to cancel this booking?");
        confirmAlert.setContentText("Booking ID: " + selectedBooking.getBookingID() + 
            "\nRoom: " + selectedBooking.getRoomID() +
            "\nStart: " + selectedBooking.getFormattedStartTime() +
            "\nEnd: " + selectedBooking.getFormattedEndTime());
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = currentUser.cancelBooking(selectedBooking);
                
                if (success) {
                    showStatus("Booking cancelled successfully! Room " + selectedBooking.getRoomID() + " is now available.", "#27ae60");
                    refreshView();
                } else {
                    showStatus("Failed to cancel booking!", "#e74c3c");
                }
            }
        });
    }

    private void refreshView() {
        // Refresh room list
        roomsList.setAll(User.getAllRooms());
        filteredRoomsList.setAll(roomsList);
        
        // Refresh bookings list
        bookingsList.setAll(currentUser.getMyBookings());
        
        // Clear selection
        roomsTable.getSelectionModel().clearSelection();
        selectedRoom = null;
        roomDetailsArea.setText("Select a room to view details...");
        
        showStatus("All data refreshed successfully!", "#3498db");
    }

    private void showStatus(String message, String color) {
        statusLabel.setText(message);
        statusLabel.setTextFill(Color.web(color));
        statusLabel.setStyle("-fx-background-color: " + color + "22; -fx-background-radius: 5; -fx-padding: 10;");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
