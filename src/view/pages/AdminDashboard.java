package view.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.Facility;
import model.Room;
import model.Equipment;
import model.User;
import model.services.FacilityService;
import model.services.BookingService;
import model.enums.FacilityType;
import model.enums.FacilityStatus;
import model.enums.ReservationPrivilege;

import java.util.List;

public class AdminDashboard extends VBox {

    private User currentUser;
    private ObservableList<Facility> facilitiesList;
    private ObservableList<model.Booking> bookingsList;

    // UI Components
    private TableView<Facility> facilitiesTable;
    private TableView<model.Booking> bookingsTable;
    private TextField facilityIdField;
    private TextField facilityNameField;
    private ComboBox<FacilityType> facilityTypeCombo;
    private TextField locationField;
    private TextField capacityField;
    private ComboBox<ReservationPrivilege> privilegeCombo;
    private TextField imagePathField;
    private TextArea notesArea;
    private Label statusLabel;

    public AdminDashboard(User user) {
        this.currentUser = user;
        initializeComponents();
        setupLayout();
        loadData();
    }

    private void initializeComponents() {
        facilitiesList = FXCollections.observableArrayList();
        bookingsList = FXCollections.observableArrayList();

        setupFacilitiesTable();
        setupBookingsTable();

        // Form fields for facility management
        facilityIdField = new TextField();
        facilityIdField.setPromptText("Facility ID");

        facilityNameField = new TextField();
        facilityNameField.setPromptText("Facility Name");

        facilityTypeCombo = new ComboBox<>();
        facilityTypeCombo.getItems().addAll(FacilityType.values());
        facilityTypeCombo.setValue(FacilityType.ROOM);

        locationField = new TextField();
        locationField.setPromptText("Location");

        capacityField = new TextField();
        capacityField.setPromptText("Capacity");

        privilegeCombo = new ComboBox<>();
        privilegeCombo.getItems().addAll(ReservationPrivilege.values());
        privilegeCombo.setValue(ReservationPrivilege.OPEN);

        imagePathField = new TextField();
        imagePathField.setPromptText("Image Path (optional)");

        notesArea = new TextArea();
        notesArea.setPromptText("Notes");
        notesArea.setPrefRowCount(3);

        statusLabel = new Label("Admin Dashboard - " + currentUser.getName());
        statusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
    }

    private void setupFacilitiesTable() {
        facilitiesTable = new TableView<>();
        facilitiesTable.setItems(facilitiesList);

        TableColumn<Facility, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Facility, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Facility, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().getType().toString()));

        TableColumn<Facility, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Facility, Integer> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        TableColumn<Facility, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().getStatus().toString()));

        facilitiesTable.getColumns().addAll(idCol, nameCol, typeCol, locationCol, capacityCol, statusCol);

        facilitiesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadFacilityIntoForm(newSelection);
            }
        });
    }

    private void setupBookingsTable() {
        bookingsTable = new TableView<>();
        bookingsTable.setItems(bookingsList);

        TableColumn<model.Booking, String> idCol = new TableColumn<>("Booking ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookingID"));

        TableColumn<model.Booking, String> userCol = new TableColumn<>("User");
        userCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<model.Booking, String> facilityCol = new TableColumn<>("Facility");
        facilityCol.setCellValueFactory(new PropertyValueFactory<>("facilityId"));

        TableColumn<model.Booking, String> startCol = new TableColumn<>("Start Time");
        startCol.setCellValueFactory(new PropertyValueFactory<>("formattedStartTime"));

        TableColumn<model.Booking, String> endCol = new TableColumn<>("End Time");
        endCol.setCellValueFactory(new PropertyValueFactory<>("formattedEndTime"));

        TableColumn<model.Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().getStatus().toString()));

        bookingsTable.getColumns().addAll(idCol, userCol, facilityCol, startCol, endCol, statusCol);
    }

    private void setupLayout() {
        setSpacing(15);
        setPadding(new Insets(20));

        // Header
        HBox headerSection = new HBox();
        headerSection.setAlignment(Pos.CENTER_LEFT);
        headerSection.getChildren().add(statusLabel);

        // Facility Management Section
        VBox facilityManagementSection = new VBox(10);
        facilityManagementSection.setStyle("-fx-border-color: #ddd; -fx-border-width: 1; -fx-padding: 15; -fx-background-radius: 5;");

        Label facilityTitle = new Label("Facility Management");
        facilityTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        // Form layout
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        formGrid.add(new Label("ID:"), 0, 0);
        formGrid.add(facilityIdField, 1, 0);
        formGrid.add(new Label("Name:"), 2, 0);
        formGrid.add(facilityNameField, 3, 0);

        formGrid.add(new Label("Type:"), 0, 1);
        formGrid.add(facilityTypeCombo, 1, 1);
        formGrid.add(new Label("Location:"), 2, 1);
        formGrid.add(locationField, 3, 1);

        formGrid.add(new Label("Capacity:"), 0, 2);
        formGrid.add(capacityField, 1, 2);
        formGrid.add(new Label("Privilege:"), 2, 2);
        formGrid.add(privilegeCombo, 3, 2);

        formGrid.add(new Label("Image Path:"), 0, 3);
        formGrid.add(imagePathField, 1, 3, 3, 1);

        formGrid.add(new Label("Notes:"), 0, 4);
        formGrid.add(notesArea, 1, 4, 3, 1);

        // Buttons
        HBox buttonBox = new HBox(10);
        Button addButton = new Button("Add Facility");
        Button updateButton = new Button("Update Facility");
        Button deleteButton = new Button("Delete Facility");
        Button clearButton = new Button("Clear Form");

        addButton.setOnAction(e -> handleAddFacility());
        updateButton.setOnAction(e -> handleUpdateFacility());
        deleteButton.setOnAction(e -> handleDeleteFacility());
        clearButton.setOnAction(e -> clearForm());

        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, clearButton);

        facilityManagementSection.getChildren().addAll(facilityTitle, formGrid, buttonBox);

        // Facilities Table Section
        VBox facilitiesSection = new VBox(10);
        facilitiesSection.getChildren().addAll(
            new Label("All Facilities:"),
            facilitiesTable
        );

        // Status Management Section
        VBox statusSection = new VBox(10);
        HBox statusControls = new HBox(10);
        Button markAvailableBtn = new Button("Mark Available");
        Button markBookedBtn = new Button("Mark Booked");
        Button markMaintenanceBtn = new Button("Mark Maintenance");
        Button markClosedBtn = new Button("Mark Temporarily Closed");

        markAvailableBtn.setOnAction(e -> handleStatusChange(FacilityStatus.AVAILABLE));
        markBookedBtn.setOnAction(e -> handleStatusChange(FacilityStatus.BOOKED));
        markMaintenanceBtn.setOnAction(e -> handleStatusChange(FacilityStatus.MAINTENANCE));
        markClosedBtn.setOnAction(e -> handleStatusChange(FacilityStatus.TEMPORARILY_CLOSED));

        statusControls.getChildren().addAll(markAvailableBtn, markBookedBtn, markMaintenanceBtn, markClosedBtn);
        statusSection.getChildren().addAll(new Label("Status Management:"), statusControls);

        // Bookings Overview Section
        VBox bookingsSection = new VBox(10);
        bookingsSection.getChildren().addAll(
            new Label("All Bookings Overview:"),
            bookingsTable
        );

        // Statistics Section
        VBox statsSection = new VBox(10);
        FacilityService.FacilityStats stats = FacilityService.getFacilityStats();

        Label statsLabel = new Label(String.format(
            "System Statistics:\nTotal Facilities: %d\nAvailable: %d\nBooked: %d\nClosed: %d",
            stats.total, stats.available, stats.booked, stats.closed
        ));
        statsLabel.setStyle("-fx-font-family: monospace;");

        statsSection.getChildren().addAll(new Label("Statistics:"), statsLabel);

        // Main layout
        getChildren().addAll(
            headerSection,
            facilityManagementSection,
            facilitiesSection,
            statusSection,
            bookingsSection,
            statsSection
        );
    }

    private void loadData() {
        List<Facility> facilities = FacilityService.getAllFacilities();
        facilitiesList.clear();
        facilitiesList.addAll(facilities);

        List<model.Booking> allBookings = BookingService.getAllBookings();
        bookingsList.clear();
        bookingsList.addAll(allBookings);
    }

    private void loadFacilityIntoForm(Facility facility) {
        facilityIdField.setText(facility.getId());
        facilityNameField.setText(facility.getName());
        facilityTypeCombo.setValue(facility.getType());
        locationField.setText(facility.getLocation());
        capacityField.setText(String.valueOf(facility.getCapacity()));
        privilegeCombo.setValue(facility.getPrivilege());
        imagePathField.setText(facility.getImagePath() != null ? facility.getImagePath() : "");
        notesArea.setText(facility.getNotes() != null ? facility.getNotes() : "");
    }

    private void clearForm() {
        facilityIdField.clear();
        facilityNameField.clear();
        facilityTypeCombo.setValue(FacilityType.ROOM);
        locationField.clear();
        capacityField.clear();
        privilegeCombo.setValue(ReservationPrivilege.OPEN);
        imagePathField.clear();
        notesArea.clear();
    }

    private void handleAddFacility() {
        try {
            String id = facilityIdField.getText().trim();
            String name = facilityNameField.getText().trim();
            FacilityType type = facilityTypeCombo.getValue();
            String location = locationField.getText().trim();
            int capacity = Integer.parseInt(capacityField.getText().trim());
            ReservationPrivilege privilege = privilegeCombo.getValue();
            String imagePath = imagePathField.getText().trim();
            String notes = notesArea.getText().trim();

            if (id.isEmpty() || name.isEmpty() || location.isEmpty()) {
                showAlert("Please fill in all required fields (ID, Name, Location).");
                return;
            }

            Room newFacility = new Room(id, name, type, location, capacity, privilege,
                                      FacilityStatus.AVAILABLE, imagePath.isEmpty() ? null : imagePath, notes);

            boolean success = FacilityService.addFacility(newFacility);
            if (success) {
                statusLabel.setText("Facility added successfully!");
                statusLabel.setTextFill(Color.GREEN);
                clearForm();
                loadData();
            } else {
                showAlert("Failed to add facility. ID may already exist.");
            }
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid number for capacity.");
        }
    }

    private void handleUpdateFacility() {
        Facility selected = facilitiesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a facility to update.");
            return;
        }

        try {
            String id = facilityIdField.getText().trim();
            String name = facilityNameField.getText().trim();
            FacilityType type = facilityTypeCombo.getValue();
            String location = locationField.getText().trim();
            int capacity = Integer.parseInt(capacityField.getText().trim());
            ReservationPrivilege privilege = privilegeCombo.getValue();
            String imagePath = imagePathField.getText().trim();
            String notes = notesArea.getText().trim();

            Room updatedFacility = new Room(id, name, type, location, capacity, privilege,
                                          selected.getStatus(), imagePath.isEmpty() ? null : imagePath, notes);

            boolean success = FacilityService.updateFacility(selected.getId(), updatedFacility);
            if (success) {
                statusLabel.setText("Facility updated successfully!");
                statusLabel.setTextFill(Color.GREEN);
                loadData();
            } else {
                showAlert("Failed to update facility.");
            }
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid number for capacity.");
        }
    }

    private void handleDeleteFacility() {
        Facility selected = facilitiesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a facility to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Facility");
        confirmAlert.setContentText("Are you sure you want to delete facility '" + selected.getName() + "'?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            boolean success = FacilityService.removeFacility(selected.getId());
            if (success) {
                statusLabel.setText("Facility deleted successfully!");
                statusLabel.setTextFill(Color.ORANGE);
                clearForm();
                loadData();
            } else {
                showAlert("Failed to delete facility.");
            }
        }
    }

    private void handleStatusChange(FacilityStatus newStatus) {
        Facility selected = facilitiesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a facility to change status.");
            return;
        }

        boolean success = FacilityService.updateFacilityStatus(selected.getId(), newStatus);
        if (success) {
            statusLabel.setText("Facility status updated to " + newStatus + "!");
            statusLabel.setTextFill(Color.BLUE);
            loadData();
        } else {
            showAlert("Failed to update facility status.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Admin Dashboard");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}