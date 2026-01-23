package view.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Booking;
import model.User;
import model.services.BookingService;

import java.util.List;

public class MyBookingsPage extends VBox {

    private User currentUser;
    private ObservableList<Booking> bookingsList;
    private Runnable refreshFacilitiesCallback;

    // UI Components
    private TableView<Booking> bookingsTable;
    private Button cancelButton;
    private Button refreshButton;
    private Label statusLabel;
    private TextArea bookingDetailsArea;

    public MyBookingsPage(User user) {
        this(user, null);
    }

    public MyBookingsPage(User user, Runnable refreshFacilitiesCallback) {
        this.currentUser = user;
        this.refreshFacilitiesCallback = refreshFacilitiesCallback;
        initializeComponents();
        setupLayout();
        loadData();
    }

    private void initializeComponents() {
        // Initialize data list
        bookingsList = FXCollections.observableArrayList();

        // Table
        setupBookingsTable();

        // Buttons
        cancelButton = new Button("❌ Cancel Selected Booking");
        cancelButton.getStyleClass().add("danger-btn");
        cancelButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 6;");
        cancelButton.setOnAction(e -> handleCancellation());

        refreshButton = new Button("🔄 Refresh");
        refreshButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 6;");
        refreshButton.setOnAction(e -> loadData());

        // Status label
        statusLabel = new Label("Select a booking to view details");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        // Booking details area
        bookingDetailsArea = new TextArea();
        bookingDetailsArea.setEditable(false);
        bookingDetailsArea.setPrefRowCount(8);
        bookingDetailsArea.setStyle("-fx-font-size: 14px; -fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5;");
    }

    private void setupLayout() {
        setSpacing(20);
        setPadding(new Insets(30));
        setStyle("-fx-background-color: linear-gradient(to bottom, #fff3cd 0%, #ffeaa7 100%);");

        // Header
        VBox headerBox = createHeader();

        // Bookings table section
        VBox tableSection = createTableSection();

        // Booking details section
        VBox detailsSection = createDetailsSection();

        // Action buttons section
        VBox actionSection = createActionSection();

        getChildren().addAll(headerBox, tableSection, detailsSection, actionSection);
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label titleLabel = new Label("📋 My Bookings");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        Label subtitleLabel = new Label("View and manage your facility reservations");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        subtitleLabel.setStyle("-fx-text-fill: #7f8c8d;");

        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }

    private void setupBookingsTable() {
        bookingsTable = new TableView<>();
        bookingsTable.setItems(bookingsList);
        bookingsTable.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-radius: 8; -fx-background-radius: 8;");

        TableColumn<Booking, String> idCol = new TableColumn<>("Booking ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
        idCol.setPrefWidth(120);
        idCol.setStyle("-fx-font-weight: bold;");

        TableColumn<Booking, String> facilityCol = new TableColumn<>("Facility");
        facilityCol.setCellValueFactory(new PropertyValueFactory<>("facilityId"));
        facilityCol.setPrefWidth(120);

        TableColumn<Booking, String> startCol = new TableColumn<>("Start Time");
        startCol.setCellValueFactory(new PropertyValueFactory<>("formattedStartTime"));
        startCol.setPrefWidth(180);

        TableColumn<Booking, String> endCol = new TableColumn<>("End Time");
        endCol.setCellValueFactory(new PropertyValueFactory<>("formattedEndTime"));
        endCol.setPrefWidth(180);

        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().getStatus().toString()));
        statusCol.setPrefWidth(100);

        // Add status styling
        statusCol.setCellFactory(column -> new TableCell<Booking, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status.toUpperCase()) {
                        case "CONFIRMED" -> setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724;");
                        case "CANCELLED" -> setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24;");
                        case "COMPLETED" -> setStyle("-fx-background-color: #d1ecf1; -fx-text-fill: #0c5460;");
                        default -> setStyle("");
                    }
                }
            }
        });

        bookingsTable.getColumns().addAll(idCol, facilityCol, startCol, endCol, statusCol);

        // Add selection listener
        bookingsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateBookingDetails(newSelection);
            }
        });
    }

    private VBox createTableSection() {
        VBox tableBox = new VBox(15);
        tableBox.setPadding(new Insets(20));
        tableBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.1, 0, 3);");

        Label tableTitle = new Label("📅 Your Reservations");
        tableTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        tableTitle.setStyle("-fx-text-fill: #2c3e50;");

        tableBox.getChildren().addAll(tableTitle, bookingsTable);
        return tableBox;
    }

    private VBox createDetailsSection() {
        VBox detailsBox = new VBox(15);
        detailsBox.setPadding(new Insets(20));
        detailsBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.1, 0, 3);");

        Label detailsTitle = new Label("📄 Booking Details");
        detailsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        detailsTitle.setStyle("-fx-text-fill: #2c3e50;");

        detailsBox.getChildren().addAll(detailsTitle, bookingDetailsArea);
        return detailsBox;
    }

    private VBox createActionSection() {
        VBox actionBox = new VBox(15);
        actionBox.setAlignment(Pos.CENTER);
        actionBox.setPadding(new Insets(20));

        HBox buttonRow = new HBox(15);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.getChildren().addAll(refreshButton, cancelButton);

        actionBox.getChildren().addAll(buttonRow, statusLabel);
        return actionBox;
    }

    private void updateBookingDetails(Booking booking) {
        if (booking == null) {
            bookingDetailsArea.setText("No booking selected.");
            return;
        }

        StringBuilder details = new StringBuilder();
        details.append("🆔 Booking ID: ").append(booking.getBookingID()).append("\n");
        details.append("👤 User: ").append(booking.getUserId()).append("\n");
        details.append("🏢 Facility: ").append(booking.getFacilityId()).append("\n");
        details.append("📅 Start Time: ").append(booking.getFormattedStartTime()).append("\n");
        details.append("🏁 End Time: ").append(booking.getFormattedEndTime()).append("\n");
        details.append("📊 Status: ").append(booking.getStatus()).append("\n");
        details.append("📝 Booked: ").append(booking.getFormattedStartTime()).append("\n");

        // Calculate duration
        long hours = java.time.Duration.between(booking.getStartTime(), booking.getEndTime()).toHours();
        details.append("⏱️ Duration: ").append(hours).append(" hours\n");

        bookingDetailsArea.setText(details.toString());
    }

    private void loadData() {
        // Load user's bookings
        List<Booking> userBookings = BookingService.getBookingsForUser(currentUser.getMatricNo());
        bookingsList.clear();
        bookingsList.addAll(userBookings);

        // Update status
        statusLabel.setText("Loaded " + userBookings.size() + " bookings");
    }

    private void handleCancellation() {
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showToast("❌ Please select a booking to cancel.", "error");
            return;
        }

        // Check if booking can be cancelled (not already completed/cancelled)
        if (selectedBooking.getStatus().toString().equals("COMPLETED") ||
            selectedBooking.getStatus().toString().equals("CANCELLED")) {
            showToast("❌ This booking cannot be cancelled.", "error");
            return;
        }

        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Booking");
        alert.setHeaderText("Are you sure you want to cancel this booking?");
        alert.setContentText("Booking ID: " + selectedBooking.getBookingID() + "\nFacility: " + selectedBooking.getFacilityId());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = BookingService.cancelBooking(currentUser, selectedBooking);
                if (success) {
                    showToast("✅ Booking cancelled successfully.", "success");
                    loadData(); // Refresh data
                    bookingDetailsArea.clear();
                    // Refresh facilities page to update status
                    if (refreshFacilitiesCallback != null) {
                        refreshFacilitiesCallback.run();
                    }
                } else {
                    showToast("❌ Cancellation failed. Check booking ownership.", "error");
                }
            }
        });
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
            statusLabel.setText("Select a booking to view details");
            statusLabel.getStyleClass().removeAll("status-success", "status-error", "status-info");
        });
        pause.play();
    }
}