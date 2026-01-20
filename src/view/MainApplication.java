package view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.User;
import model.Facility;
import model.services.AuthService;
import view.components.NavigationSidebar;
import view.pages.*;

public class MainApplication extends Application {

    private Stage primaryStage;
    private User currentUser;
    private BorderPane mainLayout;
    private StackPane contentArea;
    private NavigationSidebar sidebar;

    // Pages
    private StudentDashboard studentDashboard;
    private AdminDashboard adminDashboard;
    private FacilityCatalog facilityCatalog;
    private FacilityDetails facilityDetails;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // For testing: Auto-login as admin (matric starting with 1 = admin)
        currentUser = AuthService.login("1123456", "password");

        if (currentUser != null) {
            initializeMainApplication();
        } else {
            // Fallback: Show login dialog
            showLoginDialog();
            if (currentUser != null) {
                initializeMainApplication();
            } else {
                System.exit(0);
            }
        }
    }

    private void showLoginDialog() {
        javafx.scene.control.TextInputDialog loginDialog = new javafx.scene.control.TextInputDialog();
        loginDialog.setTitle("IIUM Library Booking System");
        loginDialog.setHeaderText("Login to Your Account");
        loginDialog.setContentText("Enter your Matric Number:");

        loginDialog.showAndWait().ifPresent(matricNo -> {
            // Simple password dialog
            javafx.scene.control.TextInputDialog passwordDialog = new javafx.scene.control.TextInputDialog();
            passwordDialog.setTitle("IIUM Library Booking System");
            passwordDialog.setHeaderText("Enter Password");
            passwordDialog.setContentText("Password:");

            passwordDialog.showAndWait().ifPresent(password -> {
                currentUser = AuthService.login(matricNo, password);
                if (currentUser == null) {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.ERROR);
                    alert.setTitle("Login Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid matric number or password. Please try again.");
                    alert.showAndWait();
                    showLoginDialog(); // Retry login
                }
            });
        });
    }

    private void initializeMainApplication() {
        primaryStage.setTitle("IIUM Library Booking System - " + currentUser.getRole() + ": " + currentUser.getName());

        // Initialize pages
        initializePages();

        // Setup main layout
        setupMainLayout();

        // Setup navigation
        setupNavigation();

        // Show initial page based on role
        showInitialPage();

        // Create scene
        Scene scene = new Scene(mainLayout, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializePages() {
        studentDashboard = new StudentDashboard(currentUser);
        adminDashboard = new AdminDashboard(currentUser);
        facilityCatalog = new FacilityCatalog(currentUser);
        facilityDetails = new FacilityDetails(currentUser);

        // Setup facility catalog interactions
        facilityCatalog.setOnFacilitySelected(() -> {
            Facility selectedFacility = facilityCatalog.getSelectedFacility();
            if (selectedFacility != null) {
                facilityDetails.setFacility(selectedFacility);
                showPage(facilityDetails);
            }
        });

        // Setup facility details interactions
        facilityDetails.setOnBackToCatalog(() -> showPage(facilityCatalog));
        facilityDetails.setOnBookNow(() -> {
            // Refresh dashboard and show it
            if (studentDashboard != null) {
                // In a real app, we'd refresh the dashboard data
                showPage(studentDashboard);
            }
        });
    }

    private void setupMainLayout() {
        mainLayout = new BorderPane();
        contentArea = new StackPane();
        contentArea.setAlignment(Pos.TOP_LEFT);

        // Initially hide content until navigation is set up
        mainLayout.setCenter(contentArea);
    }

    private void setupNavigation() {
        sidebar = new NavigationSidebar(currentUser);

        // Setup navigation handlers
        sidebar.setOnDashboardSelected(() -> {
            if (AuthService.isAdmin(currentUser)) {
                showPage(adminDashboard);
            } else {
                showPage(studentDashboard);
            }
        });

        sidebar.setOnFacilitiesSelected(() -> showPage(facilityCatalog));
        sidebar.setOnMyBookingsSelected(() -> showPage(studentDashboard)); // Student dashboard has bookings
        sidebar.setOnAdminPanelSelected(() -> {
            if (AuthService.canAccessAdminFeatures(currentUser)) {
                showPage(adminDashboard);
            }
        });

        mainLayout.setLeft(sidebar);
    }

    private void showInitialPage() {
        if (AuthService.isAdmin(currentUser)) {
            showPage(adminDashboard);
        } else {
            showPage(studentDashboard);
        }
    }

    private void showPage(javafx.scene.Node page) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(page);
    }

    public static void main(String[] args) {
        launch(args);
    }
}