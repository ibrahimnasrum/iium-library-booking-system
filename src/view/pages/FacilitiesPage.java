package view.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Facility;
import model.User;
import model.enums.FacilityStatus;
import model.services.FacilityService;
import view.components.FacilityCard;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

public class FacilitiesPage extends VBox {

    private User currentUser;
    private Consumer<Facility> navigateToDetailCallback;
    private ObservableList<Facility> facilitiesList;
    private ObservableList<Facility> filteredFacilitiesList;

    // UI Components
    private ScrollPane facilitiesScrollPane;
    private FlowPane facilitiesContainer;
    private TextField searchField;
    private ComboBox<String> filterLocationCombo;
    private ComboBox<String> filterStatusCombo;

    // Store facility cards for dynamic updates
    private Map<String, FacilityCard> facilityCardMap;

    public FacilitiesPage(User user, Consumer<Facility> navigateToDetailCallback) {
        this.currentUser = user;
        this.navigateToDetailCallback = navigateToDetailCallback;
        this.facilityCardMap = new HashMap<>();
        initializeComponents();
        setupLayout();
        loadData();
    }

    private void initializeComponents() {
        // Initialize data lists
        facilitiesList = FXCollections.observableArrayList();
        filteredFacilitiesList = FXCollections.observableArrayList();

        // Search and filter components
        searchField = new TextField();
        searchField.setPromptText("🔍 Search facilities...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-font-size: 14px; -fx-padding: 8;");
        searchField.textProperty().addListener((obs, oldText, newText) -> filterFacilities());

        filterLocationCombo = new ComboBox<>();
        filterLocationCombo.getItems().addAll("All Locations", "Level 1", "Level 2", "Level 3");
        filterLocationCombo.setValue("All Locations");
        filterLocationCombo.setStyle("-fx-font-size: 14px;");
        filterLocationCombo.setOnAction(e -> filterFacilities());

        filterStatusCombo = new ComboBox<>();
        filterStatusCombo.getItems().addAll("All Status", "Available", "Booked", "Closed");
        filterStatusCombo.setValue("All Status");
        filterStatusCombo.setStyle("-fx-font-size: 14px;");
        filterStatusCombo.setOnAction(e -> filterFacilities());
    }

    private void setupLayout() {
        setSpacing(20);
        setPadding(new Insets(30));
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e3f2fd 0%, #f3e5f5 25%, #fff3e0 50%, #e8f5e8 75%, #fce4ec 100%); -fx-background-radius: 0;");

        // Header
        VBox headerBox = createHeader();

        // Search and filter section
        VBox searchSection = createSearchSection();

        // Facilities display section
        VBox facilitiesSection = createFacilitiesSection();

        getChildren().addAll(headerBox, searchSection, facilitiesSection);
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));

        HBox titleRow = new HBox(15);
        titleRow.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("🏢 Browse Facilities");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        Button refreshButton = new Button("🔄 Refresh");
        refreshButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        refreshButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 16;");
        refreshButton.setOnAction(e -> refreshFacilityStatuses());

        titleRow.getChildren().addAll(titleLabel, refreshButton);

        Label subtitleLabel = new Label("Find and explore available facilities for booking");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        subtitleLabel.setStyle("-fx-text-fill: #7f8c8d;");

        header.getChildren().addAll(titleRow, subtitleLabel);
        return header;
    }

    private VBox createSearchSection() {
        VBox searchBox = new VBox(15);
        searchBox.setPadding(new Insets(20));
        searchBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.1, 0, 3);");

        Label searchTitle = new Label("🔍 Search & Filter");
        searchTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        searchTitle.setStyle("-fx-text-fill: #2c3e50;");

        HBox searchRow = new HBox(15);
        searchRow.setAlignment(Pos.CENTER_LEFT);
        searchRow.getChildren().addAll(
            new Label("Search:"), searchField
        );

        HBox filterRow = new HBox(15);
        filterRow.setAlignment(Pos.CENTER_LEFT);
        filterRow.getChildren().addAll(
            new Label("Location:"), filterLocationCombo,
            new Label("Status:"), filterStatusCombo
        );

        searchBox.getChildren().addAll(searchTitle, searchRow, filterRow);
        return searchBox;
    }

    private VBox createFacilitiesSection() {
        VBox facilitiesBox = new VBox(15);

        Label facilitiesTitle = new Label("📋 Available Facilities");
        facilitiesTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        facilitiesTitle.setStyle("-fx-text-fill: #2c3e50;");

        // Facilities container
        facilitiesContainer = new FlowPane();
        facilitiesContainer.setHgap(25);
        facilitiesContainer.setVgap(25);
        facilitiesContainer.setPadding(new Insets(20));
        facilitiesContainer.setStyle("-fx-background-color: white; -fx-background-radius: 12;");

        facilitiesScrollPane = new ScrollPane(facilitiesContainer);
        facilitiesScrollPane.setFitToWidth(true);
        facilitiesScrollPane.setPrefHeight(500);
        facilitiesScrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: #dee2e6; -fx-border-radius: 12; -fx-background-radius: 12;");

        facilitiesBox.getChildren().addAll(facilitiesTitle, facilitiesScrollPane);
        return facilitiesBox;
    }

    private void updateFacilitiesDisplay() {
        facilitiesContainer.getChildren().clear();
        facilityCardMap.clear();

        for (Facility facility : filteredFacilitiesList) {
            FacilityCard card = new FacilityCard(facility);

            // Store reference for dynamic updates
            facilityCardMap.put(facility.getId(), card);

            // Make card clickable
            card.setOnMouseClicked(e -> {
                if (navigateToDetailCallback != null) {
                    navigateToDetailCallback.accept(facility);
                }
            });

            facilitiesContainer.getChildren().add(card);
        }
    }

    private void loadData() {
        // Load facilities
        List<Facility> facilities = FacilityService.getAllFacilities();
        facilitiesList.clear();
        facilitiesList.addAll(facilities);

        // Debug: print facility status counts to help diagnose missing 'Closed' items
        long total = facilities.size();
        long closedCount = facilities.stream().filter(f -> f.getStatus() == model.enums.FacilityStatus.TEMPORARILY_CLOSED).count();
        long maintenanceCount = facilities.stream().filter(f -> f.getStatus() == model.enums.FacilityStatus.MAINTENANCE).count();
        long availableCount = facilities.stream().filter(f -> f.getStatus() == model.enums.FacilityStatus.AVAILABLE).count();
        System.out.println("FacilitiesPage: loadData() - total=" + total + " available=" + availableCount + " closed=" + closedCount + " maintenance=" + maintenanceCount);

        if (closedCount > 0) {
            System.out.println("FacilitiesPage: Closed facilities -> " + facilities.stream().filter(f -> f.getStatus() == model.enums.FacilityStatus.TEMPORARILY_CLOSED).map(Facility::getId).collect(Collectors.joining(", ")));
        }

        filterFacilities();
    }

    public void refreshFacilityStatuses() {
        System.out.println("FacilitiesPage: refreshFacilityStatuses() called");

        // Reload facilities data
        List<Facility> updatedFacilities = FacilityService.getAllFacilities();
        System.out.println("FacilitiesPage: Loaded " + updatedFacilities.size() + " facilities");

        // Update each facility and its corresponding card
        for (Facility updatedFacility : updatedFacilities) {
            FacilityCard card = facilityCardMap.get(updatedFacility.getId());
            if (card != null) {
                // Update the card's status display
                System.out.println("FacilitiesPage: Updating facility " + updatedFacility.getId() + " status to " + updatedFacility.getStatus());
                card.updateStatus(updatedFacility.getStatus());
            } else {
                System.out.println("FacilitiesPage: No card found for facility " + updatedFacility.getId());
            }
        }

        // Refresh the filtered list
        filterFacilities();
        System.out.println("FacilitiesPage: refreshFacilityStatuses() completed");
    }

    private void filterFacilities() {
        List<Facility> filtered = facilitiesList.stream()
            .filter(f -> searchField.getText().isEmpty() ||
                        f.getName().toLowerCase().contains(searchField.getText().toLowerCase()) ||
                        f.getId().toLowerCase().contains(searchField.getText().toLowerCase()))
            .filter(f -> filterLocationCombo.getValue().equals("All Locations") ||
                        f.getLocation().contains(filterLocationCombo.getValue()))
            .filter(f -> {
                String selectedStatus = filterStatusCombo.getValue();
                if (selectedStatus.equals("All Status")) {
                    return true;
                }
                // Handle status mapping
                FacilityStatus facilityStatus = f.getStatus();
                switch (selectedStatus) {
                    case "Available":
                        return facilityStatus == FacilityStatus.AVAILABLE;
                    case "Booked":
                        return facilityStatus == FacilityStatus.BOOKED;
                    case "Closed":
                        return facilityStatus == FacilityStatus.TEMPORARILY_CLOSED;
                    default:
                        return false;
                }
            })
            .collect(Collectors.toList());

        // Debug logging
        System.out.println("FacilitiesPage: filterFacilities() - selectedStatus='" + filterStatusCombo.getValue() + "', search='" + searchField.getText() + "', location='" + filterLocationCombo.getValue() + "' -> matching=" + filtered.size());

        filteredFacilitiesList.clear();
        filteredFacilitiesList.addAll(filtered);
        updateFacilitiesDisplay();
    }
}