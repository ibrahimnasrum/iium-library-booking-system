package view.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Facility;
import model.User;
import model.services.FacilityService;

import java.util.List;
import java.util.function.Consumer;

public class DashboardPage extends VBox {

    private User currentUser;
    private Consumer<String> navigateCallback;

    public DashboardPage(User user, Consumer<String> navigateCallback) {
        this.currentUser = user;
        this.navigateCallback = navigateCallback;
        System.out.println("DashboardPage created with navigateCallback: " + (navigateCallback != null));
        initializeComponents();
        setupLayout();
        loadData();
    }

    private void initializeComponents() {
        setSpacing(20);
        setPadding(new Insets(30));
        setStyle("-fx-background-color: linear-gradient(to bottom right, #667eea 0%, #764ba2 100%);");
    }

    private void setupLayout() {
        // Welcome section
        VBox welcomeSection = createWelcomeSection();

        // Summary cards
        HBox summaryCards = createSummaryCards();

        // Quick stats section
        VBox statsSection = createStatsSection();

        getChildren().addAll(welcomeSection, summaryCards, statsSection);
    }

    private VBox createWelcomeSection() {
        VBox welcomeBox = new VBox(10);
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.setPadding(new Insets(20));
        welcomeBox.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 15; -fx-border-radius: 15;");

        Label welcomeLabel = new Label("Welcome to IIUM Library");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        welcomeLabel.setStyle("-fx-text-fill: white;");

        Label userLabel = new Label("Hello, " + currentUser.getName() + "!");
        userLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        userLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.9);");

        Label roleLabel = new Label("Role: " + currentUser.getRole());
        roleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        roleLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.7);");

        welcomeBox.getChildren().addAll(welcomeLabel, userLabel, roleLabel);
        return welcomeBox;
    }

    private HBox createSummaryCards() {
        HBox cardsContainer = new HBox(25);
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
        VBox totalCard = createSummaryCard("🏢 Total Facilities", String.valueOf(totalFacilities), "#ffffff", "#3b82f6");
        VBox availableCard = createSummaryCard("✅ Available", String.valueOf(availableCount), "#ffffff", "#22c55e");
        VBox bookedCard = createSummaryCard("📅 Booked", String.valueOf(bookedCount), "#ffffff", "#f59e0b");
        VBox closedCard = createSummaryCard("🚫 Closed/Maint", String.valueOf(closedCount), "#ffffff", "#ef4444");

        cardsContainer.getChildren().addAll(totalCard, availableCard, bookedCard, closedCard);
        return cardsContainer;
    }

    private VBox createSummaryCard(String title, String value, String textColor, String bgColor) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(200, 120);
        card.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 16; -fx-padding: 20; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0.3, 0, 8);");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + textColor + "; -fx-font-weight: bold;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: " + textColor + ";");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private VBox createStatsSection() {
        VBox statsBox = new VBox(15);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(20));
        statsBox.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 15; -fx-border-radius: 15;");

        Label statsTitle = new Label("Quick Actions");
        statsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        statsTitle.setStyle("-fx-text-fill: white;");

        HBox actionsBox = new HBox(40); // Increased spacing
        actionsBox.setAlignment(Pos.CENTER);
        actionsBox.setPadding(new Insets(20));
        actionsBox.setPrefHeight(100); // Ensure proper height

        Button browseButton = new Button("📚 Browse Facilities");
        browseButton.setPrefWidth(200);
        browseButton.setPrefHeight(60);
        browseButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; " +
                            "-fx-background-radius: 25; -fx-padding: 15 25; -fx-font-weight: bold; " +
                            "-fx-border-color: #45a049; -fx-border-width: 1; -fx-border-radius: 25;");
        browseButton.setOnMouseEntered(e -> browseButton.setStyle("-fx-background-color: #66BB6A; -fx-text-fill: white; -fx-font-size: 16px; " +
                            "-fx-background-radius: 25; -fx-padding: 15 25; -fx-font-weight: bold; " +
                            "-fx-border-color: #45a049; -fx-border-width: 1; -fx-border-radius: 25; " +
                            "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.3), 8, 0.5, 0, 0);"));
        browseButton.setOnMouseExited(e -> browseButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; " +
                            "-fx-background-radius: 25; -fx-padding: 15 25; -fx-font-weight: bold; " +
                            "-fx-border-color: #45a049; -fx-border-width: 1; -fx-border-radius: 25;"));
        browseButton.setOnAction(e -> {
            System.out.println("Browse Facilities button clicked!");
            if (navigateCallback != null) {
                navigateCallback.accept("facilities");
            } else {
                System.out.println("Navigate callback is null!");
            }
        });

        Button bookButton = new Button("📅 Make a Booking");
        bookButton.setPrefWidth(200);
        bookButton.setPrefHeight(60);
        bookButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; " +
                          "-fx-background-radius: 25; -fx-padding: 15 25; -fx-font-weight: bold; " +
                          "-fx-border-color: #1976D2; -fx-border-width: 1; -fx-border-radius: 25;");
        bookButton.setOnMouseEntered(e -> bookButton.setStyle("-fx-background-color: #42A5F5; -fx-text-fill: white; -fx-font-size: 16px; " +
                          "-fx-background-radius: 25; -fx-padding: 15 25; -fx-font-weight: bold; " +
                          "-fx-border-color: #1976D2; -fx-border-width: 1; -fx-border-radius: 25; " +
                          "-fx-effect: dropshadow(gaussian, rgba(33,150,243,0.3), 8, 0.5, 0, 0);"));
        bookButton.setOnMouseExited(e -> bookButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; " +
                          "-fx-background-radius: 25; -fx-padding: 15 25; -fx-font-weight: bold; " +
                          "-fx-border-color: #1976D2; -fx-border-width: 1; -fx-border-radius: 25;"));
        bookButton.setOnAction(e -> {
            System.out.println("Make a Booking button clicked!");
            if (navigateCallback != null) {
                navigateCallback.accept("booking");
            } else {
                System.out.println("Navigate callback is null!");
            }
        });

        Button myBookingsButton = new Button("📋 View My Bookings");
        myBookingsButton.setPrefWidth(200);
        myBookingsButton.setPrefHeight(60);
        myBookingsButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 16px; " +
                                "-fx-background-radius: 25; -fx-padding: 15 25; -fx-font-weight: bold; " +
                                "-fx-border-color: #F57C00; -fx-border-width: 1; -fx-border-radius: 25;");
        myBookingsButton.setOnMouseEntered(e -> myBookingsButton.setStyle("-fx-background-color: #FFB74D; -fx-text-fill: white; -fx-font-size: 16px; " +
                                "-fx-background-radius: 25; -fx-padding: 15 25; -fx-font-weight: bold; " +
                                "-fx-border-color: #F57C00; -fx-border-width: 1; -fx-border-radius: 25; " +
                                "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.3), 8, 0.5, 0, 0);"));
        myBookingsButton.setOnMouseExited(e -> myBookingsButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 16px; " +
                                "-fx-background-radius: 25; -fx-padding: 15 25; -fx-font-weight: bold; " +
                                "-fx-border-color: #F57C00; -fx-border-width: 1; -fx-border-radius: 25;"));
        myBookingsButton.setOnAction(e -> {
            System.out.println("View My Bookings button clicked!");
            if (navigateCallback != null) {
                navigateCallback.accept("mybookings");
            } else {
                System.out.println("Navigate callback is null!");
            }
        });

        actionsBox.getChildren().addAll(browseButton, bookButton, myBookingsButton);

        statsBox.getChildren().addAll(statsTitle, actionsBox);
        return statsBox;
    }

    private void loadData() {
        // Data is loaded in createSummaryCards method
    }
}