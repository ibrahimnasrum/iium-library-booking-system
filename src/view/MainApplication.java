package view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import java.util.List;
import model.services.AuthService;
import view.pages.LoginPage;

public class MainApplication extends Application {

    private Stage primaryStage;
    private User currentUser;

    @Override
    public void start(Stage primaryStage) {
        System.out.println("MainApplication: start() method called");
        this.primaryStage = primaryStage;

        // Setup stage properties
        setupStage(primaryStage);
        System.out.println("MainApplication: Stage setup complete");

        // Show login page first
        showLoginPage();
    }

    private void setupStage(Stage stage) {
        stage.setTitle("IIUM Library Booking System");
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.centerOnScreen();
    }

    private void showLoginPage() {
        System.out.println("MainApplication: showLoginPage() called - creating LoginPage");
        LoginPage loginPage = new LoginPage(this::handleLoginSuccess);
        Scene loginScene = new Scene(loginPage, 1000, 700);
        primaryStage.setScene(loginScene);
        primaryStage.show();
        System.out.println("MainApplication: Login scene set and stage shown");
    }

    private void handleLoginSuccess(User user) {
        this.currentUser = user;

        System.out.println("Current user logged in: " + currentUser.getMatricNo() + " (Role: " + currentUser.getRole() + ")");

        // Initialize system data after successful login
        initializeSystemData();

        // Show main application layout
        showMainLayout();
    }

    private void initializeSystemData() {
        // Clear any leftover bookings from previous sessions for clean testing
        model.services.BookingService.clearAllBookingsAndResetFacilities();
        // Also run cleanup to ensure expired bookings are handled
        model.services.BookingService.initializeAndCleanup();

        // Only set facilities to AVAILABLE if they are currently BOOKED (preserve MAINTENANCE and CLOSED status)
        List<model.Facility> allFacilities = model.services.FacilityService.getAllFacilities();
        for (model.Facility facility : allFacilities) {
            if (facility.getStatus() == model.enums.FacilityStatus.BOOKED) {
                facility.setStatus(model.enums.FacilityStatus.AVAILABLE);
            }
        }
        System.out.println("System data initialized - Only BOOKED facilities reset to AVAILABLE status");
    }

    private void showMainLayout() {
        MainLayout mainLayout = new MainLayout(currentUser);
        Scene mainScene = new Scene(mainLayout, 1000, 700);
        java.net.URL css = getClass().getResource("/styles/theme.css");
        if (css != null) {
            mainScene.getStylesheets().add(css.toExternalForm());
        } else {
            System.out.println("Warning: theme.css not found; continuing without stylesheet");
        }
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("IIUM Library Booking System - " + currentUser.getMatricNo());
    }

    public static void main(String[] args) {
        launch(args);
    }
}