package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.User;
import model.services.AuthService;
import view.pages.*;

public class MainLayout extends BorderPane {

    private User currentUser;

    // Pages
    private DashboardPage dashboardPage;
    private FacilitiesPage facilitiesPage;
    private BookingPage bookingPage;
    private MyBookingsPage myBookingsPage;
    private AdminPanelPage adminPanelPage;
    private FacilityDetailPage facilityDetailPage;

    // Shared state
    private model.Facility selectedFacility;

    // Navigation buttons
    private Button dashboardBtn;
    private Button facilitiesBtn;
    private Button bookingBtn;
    private Button myBookingsBtn;
    private Button adminBtn;
    private Button logoutBtn;

    public MainLayout(User user) {
        this.currentUser = user;
        initializePages();
        setupLayout();
        showPage("dashboard"); // Default page
    }

    private void initializePages() {
        dashboardPage = new DashboardPage(currentUser, this::showPage);
        facilitiesPage = new FacilitiesPage(currentUser, this::navigateToFacilityDetail);
        bookingPage = new BookingPage(currentUser, this::refreshFacilitiesPage);
        myBookingsPage = new MyBookingsPage(currentUser);
        facilityDetailPage = new FacilityDetailPage(currentUser, this::showPage);

        // Only create admin panel if user is admin
        if (currentUser.getRole().toString().equals("ADMIN")) {
            adminPanelPage = new AdminPanelPage(currentUser, this::refreshAllFacilityDisplays);
        }
    }

    private void setupLayout() {
        // Create sidebar
        VBox sidebar = createSidebar();

        // Set sidebar to left
        setLeft(sidebar);

        // Set default center content
        setCenter(dashboardPage);
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(250);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: linear-gradient(to bottom, #2c3e50 0%, #34495e 100%); " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.5, 0, 0);");

        // Logo/Title section
        VBox logoSection = createLogoSection();
        sidebar.getChildren().add(logoSection);

        // Navigation buttons
        VBox navSection = createNavigationSection();
        sidebar.getChildren().add(navSection);

        // User info section
        VBox userSection = createUserSection();
        sidebar.getChildren().add(userSection);

        // Logout button
        logoutBtn = new Button("🚪 Logout");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; " +
                          "-fx-padding: 12; -fx-background-radius: 8; -fx-font-weight: bold;");
        logoutBtn.setOnAction(e -> handleLogout());

        sidebar.getChildren().add(logoutBtn);

        return sidebar;
    }

    private VBox createLogoSection() {
        VBox logoBox = new VBox(5);
        logoBox.setAlignment(Pos.CENTER);
        logoBox.setPadding(new Insets(0, 0, 30, 0));

        Label logoLabel = new Label("🏫 IIUM");
        logoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        logoLabel.setStyle("-fx-text-fill: white;");

        Label subtitleLabel = new Label("Library System");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        subtitleLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.8);");

        logoBox.getChildren().addAll(logoLabel, subtitleLabel);
        return logoBox;
    }

    private VBox createNavigationSection() {
        VBox navBox = new VBox(5);
        navBox.setPadding(new Insets(0, 0, 30, 0));

        // Navigation buttons
        dashboardBtn = createNavButton("🏠 Dashboard", "dashboard");
        facilitiesBtn = createNavButton("🏢 Facilities", "facilities");
        bookingBtn = createNavButton("📅 Book Now", "booking");
        myBookingsBtn = createNavButton("📋 My Bookings", "my-bookings");

        navBox.getChildren().addAll(dashboardBtn, facilitiesBtn, bookingBtn, myBookingsBtn);

        // Admin button (only for admin users)
        if (currentUser.getRole().toString().equals("ADMIN")) {
            adminBtn = createNavButton("⚙️ Admin Panel", "admin");
            navBox.getChildren().add(adminBtn);
        }

        return navBox;
    }

    private Button createNavButton(String text, String pageId) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: rgba(255,255,255,0.8); " +
                    "-fx-font-size: 14px; -fx-padding: 12 20; -fx-background-radius: 8; " +
                    "-fx-font-weight: normal;");

        // Hover effect
        btn.setOnMouseEntered(e -> {
            if (!btn.getStyleClass().contains("active")) {
                btn.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: white; " +
                           "-fx-font-size: 14px; -fx-padding: 12 20; -fx-background-radius: 8; " +
                           "-fx-font-weight: normal;");
            }
        });

        btn.setOnMouseExited(e -> {
            if (!btn.getStyleClass().contains("active")) {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: rgba(255,255,255,0.8); " +
                           "-fx-font-size: 14px; -fx-padding: 12 20; -fx-background-radius: 8; " +
                           "-fx-font-weight: normal;");
            }
        });

        btn.setOnAction(e -> showPage(pageId));
        return btn;
    }

    private VBox createUserSection() {
        VBox userBox = new VBox(5);
        userBox.setAlignment(Pos.CENTER);
        userBox.setPadding(new javafx.geometry.Insets(20, 0, 20, 0));
        userBox.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 10; -fx-padding: 15; " +
                        "-fx-cursor: hand;");

        Label userLabel = new Label("👤 " + currentUser.getName());
        userLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        userLabel.setStyle("-fx-text-fill: white;");

        Label roleLabel = new Label(currentUser.getRole().toString());
        roleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        roleLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.7);");

        Label matricLabel = new Label("ID: " + currentUser.getMatricNo());
        matricLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        matricLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.6);");

        Label clickHint = new Label("Click to switch user");
        clickHint.setFont(Font.font("Arial", FontWeight.NORMAL, 9));
        clickHint.setStyle("-fx-text-fill: rgba(255,255,255,0.5);");

        // Make the entire user box clickable
        userBox.setOnMouseClicked(e -> showLoginDialog());

        userBox.getChildren().addAll(userLabel, roleLabel, matricLabel, clickHint);
        return userBox;
    }

    private void showPage(String pageId) {
        System.out.println("showPage called with pageId: " + pageId);
        // Reset all button styles
        resetButtonStyles();

        // Set active button style
        Node content = null;

        switch (pageId) {
            case "dashboard":
                content = dashboardPage;
                setActiveButton(dashboardBtn);
                break;
            case "facilities":
                content = facilitiesPage;
                setActiveButton(facilitiesBtn);
                break;
            case "facility-detail":
                if (facilityDetailPage != null && selectedFacility != null) {
                    facilityDetailPage.setFacility(selectedFacility);
                    content = facilityDetailPage;
                    // Don't set active button for detail pages
                } else {
                    content = facilitiesPage;
                    setActiveButton(facilitiesBtn);
                }
                break;
            case "booking":
                content = bookingPage;
                setActiveButton(bookingBtn);
                break;
            case "my-bookings":
                content = myBookingsPage;
                setActiveButton(myBookingsBtn);
                break;
            case "admin":
                if (adminPanelPage != null) {
                    content = adminPanelPage;
                    setActiveButton(adminBtn);
                } else {
                    // Fallback to dashboard if admin panel not available
                    content = dashboardPage;
                    setActiveButton(dashboardBtn);
                }
                break;
            default:
                content = dashboardPage;
                setActiveButton(dashboardBtn);
                break;
        }

        // Set center content
        setCenter(content);
        System.out.println("Page switched to: " + pageId);
    }

    private void navigateToFacilityDetail(model.Facility facility) {
        this.selectedFacility = facility;
        showPage("facility-detail");
    }

    public void refreshFacilitiesPage() {
        if (facilitiesPage != null) {
            facilitiesPage.refreshFacilityStatuses();
        }
    }

    public void refreshAllFacilityDisplays() {
        // Refresh facilities page
        if (facilitiesPage != null) {
            facilitiesPage.refreshFacilityStatuses();
        }

        // Refresh facility detail page if it's currently showing
        if (facilityDetailPage != null) {
            facilityDetailPage.refreshFacility();
        }

        // Refresh dashboard if it displays facilities
        if (dashboardPage != null && dashboardPage instanceof DashboardPage) {
            // DashboardPage might need refresh if it shows facility stats
            // For now, we'll implement this if needed
        }
    }

    private void resetButtonStyles() {
        dashboardBtn.getStyleClass().remove("active");
        facilitiesBtn.getStyleClass().remove("active");
        bookingBtn.getStyleClass().remove("active");
        myBookingsBtn.getStyleClass().remove("active");

        dashboardBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: rgba(255,255,255,0.8); " +
                            "-fx-font-size: 14px; -fx-padding: 12 20; -fx-background-radius: 8; " +
                            "-fx-font-weight: normal;");
        facilitiesBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: rgba(255,255,255,0.8); " +
                             "-fx-font-size: 14px; -fx-padding: 12 20; -fx-background-radius: 8; " +
                             "-fx-font-weight: normal;");
        bookingBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: rgba(255,255,255,0.8); " +
                          "-fx-font-size: 14px; -fx-padding: 12 20; -fx-background-radius: 8; " +
                          "-fx-font-weight: normal;");
        myBookingsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: rgba(255,255,255,0.8); " +
                             "-fx-font-size: 14px; -fx-padding: 12 20; -fx-background-radius: 8; " +
                             "-fx-font-weight: normal;");

        if (adminBtn != null) {
            adminBtn.getStyleClass().remove("active");
            adminBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: rgba(255,255,255,0.8); " +
                            "-fx-font-size: 14px; -fx-padding: 12 20; -fx-background-radius: 8; " +
                            "-fx-font-weight: normal;");
        }
    }

    private void setActiveButton(Button button) {
        button.getStyleClass().add("active");
        button.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; " +
                      "-fx-font-size: 14px; -fx-padding: 12 20; -fx-background-radius: 8; " +
                      "-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(255,255,255,0.3), 5, 0.3, 0, 0);");
    }

    private void showLoginDialog() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Switch User");
        dialog.setHeaderText("Login with different credentials to switch user");

        // Set the button types
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the matric number and password labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField matricField = new TextField();
        matricField.setPromptText("Matric Number (e.g., 0123456)");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Pre-fill with current user's matric number for convenience
        matricField.setText(currentUser.getMatricNo());

        grid.add(new Label("Matric Number:"), 0, 0);
        grid.add(matricField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);

        // Add some quick login options
        Label quickLoginLabel = new Label("Quick Login Options:");
        quickLoginLabel.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 5 0;");
        grid.add(quickLoginLabel, 0, 2, 2, 1);

        HBox quickButtons = new HBox(5);
        Button adminBtn = new Button("Admin (0...)");
        Button staffBtn = new Button("Staff (1...)");
        Button studentBtn = new Button("Student (2...)");
        Button postgradBtn = new Button("Postgrad (3...)");

        adminBtn.setOnAction(e -> matricField.setText("0123456"));
        staffBtn.setOnAction(e -> matricField.setText("1123456"));
        studentBtn.setOnAction(e -> matricField.setText("2123456"));
        postgradBtn.setOnAction(e -> matricField.setText("3123456"));

        quickButtons.getChildren().addAll(adminBtn, staffBtn, studentBtn, postgradBtn);
        grid.add(quickButtons, 0, 3, 2, 1);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the matric field by default
        matricField.requestFocus();

        // Convert the result to a User when the login button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                String matric = matricField.getText();
                String password = passwordField.getText();
                return AuthService.login(matric, password);
            }
            return null;
        });

        // Show the dialog and handle the result
        dialog.showAndWait().ifPresent(newUser -> {
            if (newUser != null) {
                switchUser(newUser);
            }
        });
    }

    private void switchUser(User newUser) {
        this.currentUser = newUser;

        // Reinitialize pages with new user
        dashboardPage = new DashboardPage(currentUser, this::showPage);
        facilitiesPage = new FacilitiesPage(currentUser, this::navigateToFacilityDetail);
        bookingPage = new BookingPage(currentUser, this::refreshFacilitiesPage);
        myBookingsPage = new MyBookingsPage(currentUser);
        facilityDetailPage = new FacilityDetailPage(currentUser, this::showPage);

        // Recreate admin panel if user is admin
        if (currentUser.getRole().toString().equals("ADMIN")) {
            adminPanelPage = new AdminPanelPage(currentUser, this::refreshAllFacilityDisplays);
        } else {
            adminPanelPage = null;
        }

        // Update sidebar to reflect new user
        updateSidebar();

        // Show dashboard by default
        showPage("dashboard");
    }

    private void updateSidebar() {
        VBox sidebar = createSidebar();
        setLeft(sidebar);
    }

    private void handleLogout() {
        // In a real application, you'd handle logout properly
        // For now, just exit the application
        System.exit(0);
    }
}