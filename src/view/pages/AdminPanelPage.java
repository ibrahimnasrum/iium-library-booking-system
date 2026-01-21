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
import model.Facility;
import model.User;
import model.enums.FacilityStatus;
import model.services.BookingService;
import model.services.FacilityService;

import java.util.List;

public class AdminPanelPage extends VBox {

    private User currentUser;
    private Runnable refreshCallback;
    private ObservableList<Booking> allBookingsList;
    private ObservableList<Facility> facilitiesList;

    // UI Components
    private TabPane tabPane;
    private TableView<Booking> bookingsTable;
    private TableView<Facility> facilitiesTable;
    private Button refreshButton;
    private Button updateFacilityButton;
    private Label statusLabel;

    public AdminPanelPage(User user, Runnable refreshCallback) {
        this.currentUser = user;
        this.refreshCallback = refreshCallback;
        initializeComponents();
        setupLayout();
        loadData();
    }

    private void initializeComponents() {
        // Initialize data lists
        allBookingsList = FXCollections.observableArrayList();
        facilitiesList = FXCollections.observableArrayList();

        // Tab pane
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Buttons
        refreshButton = new Button("🔄 Refresh Data");
        refreshButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 6;");
        refreshButton.setOnAction(e -> loadData());

        updateFacilityButton = new Button("✏️ Update Facility Status");
        updateFacilityButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-color: #FF9800; -fx-text-fill: white; -fx-background-radius: 6;");
        updateFacilityButton.setOnAction(e -> handleFacilityUpdate());

        // Status label
        statusLabel = new Label("Admin Panel - Manage bookings and facilities");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
    }

    private void setupLayout() {
        setSpacing(20);
        setPadding(new Insets(30));
        setStyle("-fx-background-color: linear-gradient(to bottom, #e8eaf6 0%, #c5cae9 100%);");

        // Header
        VBox headerBox = createHeader();

        // Tab pane with content
        createTabs();

        // Action buttons section
        VBox actionSection = createActionSection();

        getChildren().addAll(headerBox, tabPane, actionSection);
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label titleLabel = new Label("⚙️ Admin Panel");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        Label subtitleLabel = new Label("Manage all bookings and facility statuses");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        subtitleLabel.setStyle("-fx-text-fill: #7f8c8d;");

        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }

    private void createTabs() {
        // Bookings Management Tab
        Tab bookingsTab = new Tab("📅 All Bookings");
        bookingsTab.setContent(createBookingsManagementTab());

        // Facilities Management Tab
        Tab facilitiesTab = new Tab("🏢 Facilities Management");
        facilitiesTab.setContent(createFacilitiesManagementTab());

        // Statistics Tab
        Tab statsTab = new Tab("📊 Statistics");
        statsTab.setContent(createStatisticsTab());

        tabPane.getTabs().addAll(bookingsTab, facilitiesTab, statsTab);
    }

    private VBox createBookingsManagementTab() {
        VBox bookingsBox = new VBox(15);
        bookingsBox.setPadding(new Insets(20));

        Label titleLabel = new Label("📋 All Bookings Overview");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        setupBookingsTable();

        bookingsBox.getChildren().addAll(titleLabel, bookingsTable);
        return bookingsBox;
    }

    private void setupBookingsTable() {
        bookingsTable = new TableView<>();
        bookingsTable.setItems(allBookingsList);
        bookingsTable.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-radius: 8; -fx-background-radius: 8;");

        TableColumn<Booking, String> idCol = new TableColumn<>("Booking ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
        idCol.setPrefWidth(120);

        TableColumn<Booking, String> userCol = new TableColumn<>("User");
        userCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userCol.setPrefWidth(100);

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

        bookingsTable.getColumns().addAll(idCol, userCol, facilityCol, startCol, endCol, statusCol);
    }

    private VBox createFacilitiesManagementTab() {
        VBox facilitiesBox = new VBox(15);
        facilitiesBox.setPadding(new Insets(20));

        Label titleLabel = new Label("🏢 Facilities Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        setupFacilitiesTable();

        facilitiesBox.getChildren().addAll(titleLabel, facilitiesTable);
        return facilitiesBox;
    }

    private void setupFacilitiesTable() {
        facilitiesTable = new TableView<>();
        facilitiesTable.setItems(facilitiesList);
        facilitiesTable.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-radius: 8; -fx-background-radius: 8;");

        TableColumn<Facility, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);

        TableColumn<Facility, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);

        TableColumn<Facility, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().getType().toString()));
        typeCol.setPrefWidth(120);

        TableColumn<Facility, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationCol.setPrefWidth(100);

        TableColumn<Facility, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().getStatus().toString()));
        statusCol.setPrefWidth(120);

        // Add status styling
        statusCol.setCellFactory(column -> new TableCell<Facility, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status.toUpperCase()) {
                        case "AVAILABLE" -> setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724;");
                        case "BOOKED" -> setStyle("-fx-background-color: #fff3cd; -fx-text-fill: #856404;");
                        case "MAINTENANCE" -> setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24;");
                        case "TEMPORARILY_CLOSED" -> setStyle("-fx-background-color: #e2e3e5; -fx-text-fill: #383d41;");
                        default -> setStyle("");
                    }
                }
            }
        });

        facilitiesTable.getColumns().addAll(idCol, nameCol, typeCol, locationCol, statusCol);
    }

    private VBox createStatisticsTab() {
        VBox statsBox = new VBox(20);
        statsBox.setPadding(new Insets(20));
        statsBox.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("📊 System Statistics");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Statistics cards will be populated in loadData
        VBox statsCards = new VBox(15);
        statsCards.setAlignment(Pos.CENTER);

        statsBox.getChildren().addAll(titleLabel, statsCards);
        return statsBox;
    }

    private VBox createActionSection() {
        VBox actionBox = new VBox(15);
        actionBox.setAlignment(Pos.CENTER);
        actionBox.setPadding(new Insets(20));

        HBox buttonRow = new HBox(15);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.getChildren().addAll(refreshButton, updateFacilityButton);

        actionBox.getChildren().addAll(buttonRow, statusLabel);
        return actionBox;
    }

    private void loadData() {
        // Load all bookings
        List<Booking> allBookings = BookingService.getAllBookings();
        allBookingsList.clear();
        allBookingsList.addAll(allBookings);

        // Load all facilities
        List<Facility> allFacilities = FacilityService.getAllFacilities();
        facilitiesList.clear();
        facilitiesList.addAll(allFacilities);

        // Update statistics tab
        updateStatisticsTab();

        // Update status
        statusLabel.setText("Data refreshed - " + allBookings.size() + " bookings, " + allFacilities.size() + " facilities");
    }

    private void updateStatisticsTab() {
        if (tabPane.getTabs().size() >= 3) {
            VBox statsContent = (VBox) tabPane.getTabs().get(2).getContent();
            if (statsContent.getChildren().size() >= 2) {
                VBox statsCards = (VBox) statsContent.getChildren().get(1);
                statsCards.getChildren().clear();

                // Calculate statistics
                long totalBookings = allBookingsList.size();
                long confirmedBookings = allBookingsList.stream()
                    .filter(b -> b.getStatus().toString().equals("CONFIRMED")).count();
                long cancelledBookings = allBookingsList.stream()
                    .filter(b -> b.getStatus().toString().equals("CANCELLED")).count();
                long totalFacilities = facilitiesList.size();
                long availableFacilities = facilitiesList.stream()
                    .filter(f -> f.getStatus().toString().equals("AVAILABLE")).count();

                // Create stats cards
                statsCards.getChildren().addAll(
                    createStatsCard("📅 Total Bookings", String.valueOf(totalBookings), "#3b82f6"),
                    createStatsCard("✅ Confirmed", String.valueOf(confirmedBookings), "#22c55e"),
                    createStatsCard("❌ Cancelled", String.valueOf(cancelledBookings), "#ef4444"),
                    createStatsCard("🏢 Total Facilities", String.valueOf(totalFacilities), "#f59e0b"),
                    createStatsCard("✅ Available", String.valueOf(availableFacilities), "#22c55e")
                );
            }
        }
    }

    private HBox createStatsCard(String title, String value, String color) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(300);
        card.setPrefHeight(80);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 15; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.1, 0, 3);");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private void handleFacilityUpdate() {
        Facility selectedFacility = facilitiesTable.getSelectionModel().getSelectedItem();
        if (selectedFacility == null) {
            showToast("❌ Please select a facility to update.", "error");
            return;
        }

        // Create status selection dialog
        ChoiceDialog<FacilityStatus> dialog = new ChoiceDialog<>(selectedFacility.getStatus(),
            FacilityStatus.AVAILABLE, FacilityStatus.BOOKED, FacilityStatus.MAINTENANCE, FacilityStatus.TEMPORARILY_CLOSED);
        dialog.setTitle("Update Facility Status");
        dialog.setHeaderText("Update status for: " + selectedFacility.getName());
        dialog.setContentText("Choose new status:");

        dialog.showAndWait().ifPresent(newStatus -> {
            if (newStatus != selectedFacility.getStatus()) {
                // Update facility status using FacilityService
                boolean success = FacilityService.updateFacilityStatus(selectedFacility.getId(), newStatus);
                if (success) {
                    showToast("✅ Facility status updated successfully.", "success");
                    loadData(); // Refresh admin panel data

                    // Notify other pages to refresh
                    if (refreshCallback != null) {
                        refreshCallback.run();
                    }
                } else {
                    showToast("❌ Failed to update facility status.", "error");
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
            statusLabel.setText("Admin Panel - Manage bookings and facilities");
            statusLabel.getStyleClass().removeAll("status-success", "status-error", "status-info");
        });
        pause.play();
    }
}