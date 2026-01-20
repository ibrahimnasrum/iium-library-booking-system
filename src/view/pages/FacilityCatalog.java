package view.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Facility;
import model.User;
import model.services.FacilityService;
import view.components.FacilityCard;

import java.util.List;
import java.util.stream.Collectors;

public class FacilityCatalog extends VBox {

    private User currentUser;
    private FlowPane cardsContainer;
    private TextField searchField;
    private ComboBox<String> typeFilterCombo;
    private ComboBox<String> statusFilterCombo;
    private Runnable onFacilitySelected;

    public FacilityCatalog(User user) {
        this.currentUser = user;
        initializeComponents();
        setupLayout();
        loadFacilities();
    }

    private void initializeComponents() {
        cardsContainer = new FlowPane();
        cardsContainer.setHgap(20);
        cardsContainer.setVgap(20);
        cardsContainer.setPadding(new Insets(20));

        searchField = new TextField();
        searchField.setPromptText("Search facilities...");
        searchField.setPrefWidth(300);
        searchField.textProperty().addListener((obs, oldText, newText) -> filterFacilities());

        typeFilterCombo = new ComboBox<>();
        typeFilterCombo.getItems().addAll("All Types", "Room", "Study Area", "Computer Lab", "Auditorium",
                                        "Discussion Room", "Viewing Room", "Carrel Room", "Research Room");
        typeFilterCombo.setValue("All Types");
        typeFilterCombo.setOnAction(e -> filterFacilities());

        statusFilterCombo = new ComboBox<>();
        statusFilterCombo.getItems().addAll("All Status", "Available", "Booked", "Maintenance", "Temporarily Closed");
        statusFilterCombo.setValue("All Status");
        statusFilterCombo.setOnAction(e -> filterFacilities());
    }

    private void setupLayout() {
        setSpacing(15);
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #f8f9fa;");

        // Header
        Label titleLabel = new Label("Facility Catalog");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Search and filter bar
        HBox searchBar = new HBox(15);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(10));
        searchBar.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-padding: 15;");

        Label searchLabel = new Label("🔍");
        searchLabel.setStyle("-fx-font-size: 16px;");

        searchBar.getChildren().addAll(
            searchLabel,
            searchField,
            new Label("Type:"),
            typeFilterCombo,
            new Label("Status:"),
            statusFilterCombo
        );

        // Scroll pane for cards
        ScrollPane scrollPane = new ScrollPane(cardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setPadding(new Insets(10));

        // Stats bar
        HBox statsBar = new HBox(20);
        statsBar.setAlignment(Pos.CENTER);
        statsBar.setPadding(new Insets(10));
        statsBar.setStyle("-fx-background-color: white; -fx-background-radius: 5;");

        FacilityService.FacilityStats stats = FacilityService.getFacilityStats();
        Label totalLabel = new Label("Total Facilities: " + stats.total);
        Label availableLabel = new Label("Available: " + stats.available);
        Label bookedLabel = new Label("Booked: " + stats.booked);
        Label closedLabel = new Label("Closed: " + stats.closed);

        totalLabel.setStyle("-fx-font-weight: bold;");
        availableLabel.setStyle("-fx-text-fill: #27ae60;");
        bookedLabel.setStyle("-fx-text-fill: #e74c3c;");
        closedLabel.setStyle("-fx-text-fill: #95a5a6;");

        statsBar.getChildren().addAll(totalLabel, availableLabel, bookedLabel, closedLabel);

        getChildren().addAll(titleLabel, searchBar, statsBar, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }

    private void loadFacilities() {
        List<Facility> facilities = FacilityService.getAllFacilities();
        displayFacilities(facilities);
    }

    private void filterFacilities() {
        List<Facility> allFacilities = FacilityService.getAllFacilities();

        List<Facility> filtered = allFacilities.stream()
            .filter(f -> searchField.getText().isEmpty() ||
                        f.getName().toLowerCase().contains(searchField.getText().toLowerCase()) ||
                        f.getId().toLowerCase().contains(searchField.getText().toLowerCase()))
            .filter(f -> typeFilterCombo.getValue().equals("All Types") ||
                        f.getType().toString().contains(typeFilterCombo.getValue()))
            .filter(f -> statusFilterCombo.getValue().equals("All Status") ||
                        f.getStatus().toString().equalsIgnoreCase(statusFilterCombo.getValue()))
            .collect(Collectors.toList());

        displayFacilities(filtered);
    }

    private void displayFacilities(List<Facility> facilities) {
        cardsContainer.getChildren().clear();

        for (Facility facility : facilities) {
            FacilityCard card = new FacilityCard(facility);
            card.setOnCardClicked(() -> {
                if (onFacilitySelected != null) {
                    // Store selected facility for parent to handle
                    selectedFacility = facility;
                    onFacilitySelected.run();
                }
            });
            cardsContainer.getChildren().add(card);
        }

        // If no facilities found
        if (facilities.isEmpty()) {
            Label noResultsLabel = new Label("No facilities found matching your criteria.");
            noResultsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
            cardsContainer.getChildren().add(noResultsLabel);
        }
    }

    // Store selected facility (for parent component to access)
    private Facility selectedFacility;

    public Facility getSelectedFacility() {
        return selectedFacility;
    }

    public void setOnFacilitySelected(Runnable handler) {
        this.onFacilitySelected = handler;
    }

    public void refresh() {
        loadFacilities();
    }
}