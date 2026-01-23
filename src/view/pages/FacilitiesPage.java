package view.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Facility;
import model.User;
import model.services.FacilityService;
import model.services.BookingPolicy;
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

    // Store facility cards for dynamic updates
    private Map<Facility, FacilityCard> facilityCardMap;

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
    }

    private void setupLayout() {
        setSpacing(20);
        setPadding(new Insets(30));
        setMaxWidth(Double.MAX_VALUE);
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

        // Header with title and refresh button
        HBox titleBox = new HBox(20);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.setMaxWidth(Double.MAX_VALUE);

        Label titleLabel = new Label("🏢 Browse Facilities");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button refreshButton = new Button("🔄 Refresh Status");
        refreshButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 6 10; -fx-background-radius: 6;");
        refreshButton.setOnAction(e -> refreshFacilityStatuses());

        titleBox.getChildren().addAll(titleLabel, spacer, refreshButton);

        Label subtitleLabel = new Label("Find and explore available facilities for booking");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        subtitleLabel.setStyle("-fx-text-fill: #7f8c8d;");

        header.getChildren().addAll(titleBox, subtitleLabel);
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
            new Label("Location:"), filterLocationCombo
        );

        searchBox.getChildren().addAll(searchTitle, searchRow, filterRow);
        return searchBox;
    }

    private VBox createFacilitiesSection() {
        VBox facilitiesBox = new VBox(15);
        facilitiesBox.setMaxWidth(Double.MAX_VALUE);

        HBox facilitiesTitleRow = new HBox(10);
        facilitiesTitleRow.setAlignment(Pos.CENTER_LEFT);
        facilitiesTitleRow.setMaxWidth(Double.MAX_VALUE);

        Label facilitiesTitle = new Label("📋 Available Facilities");
        facilitiesTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        facilitiesTitle.setStyle("-fx-text-fill: #2c3e50;");

        facilitiesTitleRow.getChildren().addAll(facilitiesTitle);
        // Ensure title row spans full width so right-aligned button is visible
        facilitiesTitleRow.prefWidthProperty().bind(facilitiesBox.widthProperty());

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

        facilitiesBox.getChildren().addAll(facilitiesTitleRow, facilitiesScrollPane);
        return facilitiesBox;
    }

    private void updateFacilitiesDisplay() {
        facilitiesContainer.getChildren().clear();
        facilityCardMap.clear();

        for (Facility facility : filteredFacilitiesList) {
            FacilityCard card = new FacilityCard(facility);

            // Store reference for dynamic updates
            facilityCardMap.put(facility, card);

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
        // Load facilities accessible by current user
        List<Facility> facilities = FacilityService.getAccessibleFacilities(currentUser);
        facilitiesList.clear();
        facilitiesList.addAll(facilities);
        filterFacilities();
    }

    public void refreshFacilityStatuses() {
        // Reload facilities data that user can access
        List<Facility> updatedFacilities = FacilityService.getAccessibleFacilities(currentUser);

        // Update existing facilities and their cards
        for (Facility updatedFacility : updatedFacilities) {
            FacilityCard card = facilityCardMap.get(updatedFacility);
            if (card != null) {
                // Update the card's status display
                card.updateStatus(updatedFacility.getStatus());
            }
        }

        // Reload the full list in case accessibility changed
        loadData();
    }

    private void refreshFacilities() {
        // Reload all facility data from service
        loadData();
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
}