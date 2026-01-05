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
import model.Booking;
import model.Room;
import model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BookingView extends Application {
    
    private User currentUser;
    private ObservableList<Room> roomsList;
    private ObservableList<Booking> bookingsList;
    
    // UI Components
    private ComboBox<Room> roomComboBox;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private ComboBox<String> startTimeCombo;
    private ComboBox<String> endTimeCombo;
    private TableView<Booking> bookingsTable;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        // Initialize demo user
        currentUser = new User("2112345", "password123", "Ibrahim");
        roomsList = FXCollections.observableArrayList(User.getAllRooms());
        bookingsList = FXCollections.observableArrayList(currentUser.getMyBookings());

        // Main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // Top: Title and User Info
        VBox topSection = createTopSection();
        mainLayout.setTop(topSection);

        // Center: Booking Form
        VBox centerSection = createBookingForm();
        mainLayout.setCenter(centerSection);

        // Bottom: My Bookings Table
        VBox bottomSection = createMyBookingsSection();
        mainLayout.setBottom(bottomSection);

        // Scene and Stage
        Scene scene = new Scene(mainLayout, 900, 700);
        primaryStage.setTitle("IIUM Library Booking System - Booking Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createTopSection() {
        VBox topBox = new VBox(10);
        topBox.setPadding(new Insets(0, 0, 20, 0));
        topBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("📚 IIUM Library Room Booking");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        Label userLabel = new Label("Logged in as: " + currentUser.getName() + " (" + currentUser.getMatricNo() + ")");
        userLabel.setFont(Font.font("Arial", 14));
        userLabel.setTextFill(Color.web("#7f8c8d"));

        topBox.getChildren().addAll(titleLabel, userLabel);
        return topBox;
    }

    private VBox createBookingForm() {
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(20));
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        formBox.setMaxWidth(800);
        formBox.setAlignment(Pos.TOP_CENTER);

        Label formTitle = new Label("🏢 Make a New Booking");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        formTitle.setTextFill(Color.web("#34495e"));

        // Room Selection
        HBox roomBox = new HBox(10);
        roomBox.setAlignment(Pos.CENTER_LEFT);
        Label roomLabel = new Label("Select Room:");
        roomLabel.setMinWidth(100);
        roomLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        
        roomComboBox = new ComboBox<>(roomsList);
        roomComboBox.setPromptText("Choose a room...");
        roomComboBox.setPrefWidth(500);
        roomBox.getChildren().addAll(roomLabel, roomComboBox);

        // Start Date & Time
        HBox startBox = new HBox(10);
        startBox.setAlignment(Pos.CENTER_LEFT);
        Label startLabel = new Label("Start:");
        startLabel.setMinWidth(100);
        startLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        
        startDatePicker = new DatePicker(LocalDate.now());
        startDatePicker.setPrefWidth(200);
        
        startTimeCombo = new ComboBox<>(getTimeSlots());
        startTimeCombo.setValue("09:00");
        startTimeCombo.setPrefWidth(100);
        
        startBox.getChildren().addAll(startLabel, startDatePicker, new Label("at"), startTimeCombo);

        // End Date & Time
        HBox endBox = new HBox(10);
        endBox.setAlignment(Pos.CENTER_LEFT);
        Label endLabel = new Label("End:");
        endLabel.setMinWidth(100);
        endLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        
        endDatePicker = new DatePicker(LocalDate.now());
        endDatePicker.setPrefWidth(200);
        
        endTimeCombo = new ComboBox<>(getTimeSlots());
        endTimeCombo.setValue("10:00");
        endTimeCombo.setPrefWidth(100);
        
        endBox.getChildren().addAll(endLabel, endDatePicker, new Label("at"), endTimeCombo);

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Button bookNowBtn = new Button("📅 Book Now");
        bookNowBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 30;");
        bookNowBtn.setOnAction(e -> handleBookNow());

        Button cancelBookingBtn = new Button("❌ Cancel Selected Booking");
        cancelBookingBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 30;");
        cancelBookingBtn.setOnAction(e -> handleCancelBooking());

        Button refreshBtn = new Button("🔄 Refresh");
        refreshBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 30;");
        refreshBtn.setOnAction(e -> refreshView());

        buttonBox.getChildren().addAll(bookNowBtn, cancelBookingBtn, refreshBtn);

        // Status Label
        statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setMaxWidth(Double.MAX_VALUE);

        formBox.getChildren().addAll(formTitle, new Separator(), roomBox, startBox, endBox, buttonBox, statusLabel);
        return formBox;
    }

    private VBox createMyBookingsSection() {
        VBox bookingsBox = new VBox(10);
        bookingsBox.setPadding(new Insets(20, 0, 0, 0));

        Label bookingsTitle = new Label("📋 My Bookings");
        bookingsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        bookingsTitle.setTextFill(Color.web("#34495e"));

        // TableView for bookings
        bookingsTable = new TableView<>();
        bookingsTable.setItems(bookingsList);
        bookingsTable.setPrefHeight(250);

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

        bookingsTable.getColumns().addAll(idCol, roomCol, startCol, endCol, statusCol);

        bookingsBox.getChildren().addAll(bookingsTitle, bookingsTable);
        return bookingsBox;
    }

    private ObservableList<String> getTimeSlots() {
        ObservableList<String> times = FXCollections.observableArrayList();
        for (int hour = 8; hour <= 22; hour++) {
            times.add(String.format("%02d:00", hour));
            times.add(String.format("%02d:30", hour));
        }
        return times;
    }

    private void handleBookNow() {
        // Validate inputs
        Room selectedRoom = roomComboBox.getValue();
        if (selectedRoom == null) {
            showStatus("⚠️ Please select a room!", "#e74c3c");
            return;
        }

        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            showStatus("⚠️ Please select start and end dates!", "#e74c3c");
            return;
        }

        if (startTimeCombo.getValue() == null || endTimeCombo.getValue() == null) {
            showStatus("⚠️ Please select start and end times!", "#e74c3c");
            return;
        }

        // Check if room is available
        if (!selectedRoom.getAvailabilityStatus().equals("Available")) {
            showStatus("⚠️ Selected room is not available!", "#e74c3c");
            return;
        }

        // Create date-time objects
        LocalTime startTime = LocalTime.parse(startTimeCombo.getValue());
        LocalTime endTime = LocalTime.parse(endTimeCombo.getValue());
        LocalDateTime startDateTime = LocalDateTime.of(startDatePicker.getValue(), startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDatePicker.getValue(), endTime);

        // Validate time range
        if (endDateTime.isBefore(startDateTime) || endDateTime.isEqual(startDateTime)) {
            showStatus("⚠️ End time must be after start time!", "#e74c3c");
            return;
        }

        // Make booking
        boolean success = currentUser.makeBooking(selectedRoom, startDateTime, endDateTime);
        
        if (success) {
            showStatus("✅ Booking successful! Room " + selectedRoom.getRoomID() + " is now booked.", "#27ae60");
            refreshView();
        } else {
            showStatus("❌ Booking failed! Please try again.", "#e74c3c");
        }
    }

    private void handleCancelBooking() {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        
        if (selectedBooking == null) {
            showStatus("⚠️ Please select a booking to cancel!", "#e74c3c");
            return;
        }

        if (selectedBooking.getStatus().equals("Cancelled")) {
            showStatus("⚠️ This booking is already cancelled!", "#e74c3c");
            return;
        }

        // Confirm cancellation
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Cancel Booking");
        confirmAlert.setHeaderText("Are you sure you want to cancel this booking?");
        confirmAlert.setContentText("Booking ID: " + selectedBooking.getBookingID() + "\nRoom: " + selectedBooking.getRoomID());
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = currentUser.cancelBooking(selectedBooking);
                
                if (success) {
                    showStatus("✅ Booking cancelled successfully! Room is now available.", "#27ae60");
                    refreshView();
                } else {
                    showStatus("❌ Failed to cancel booking!", "#e74c3c");
                }
            }
        });
    }

    private void refreshView() {
        // Refresh room list
        roomsList.setAll(User.getAllRooms());
        roomComboBox.setItems(roomsList);
        
        // Refresh bookings list
        bookingsList.setAll(currentUser.getMyBookings());
        bookingsTable.setItems(bookingsList);
        
        showStatus("🔄 View refreshed!", "#3498db");
    }

    private void showStatus(String message, String color) {
        statusLabel.setText(message);
        statusLabel.setTextFill(Color.web(color));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
