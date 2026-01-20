package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.User;
import view.pages.*;

public class MainLayout extends BorderPane {

    private User currentUser;

    // Pages
    private DashboardPage dashboardPage;
    private FacilitiesPage facilitiesPage;
    private BookingPage bookingPage;
    private MyBookingsPage myBookingsPage;
    private AdminPanelPage adminPanelPage;

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
        facilitiesPage = new FacilitiesPage(currentUser);
        bookingPage = new BookingPage(currentUser);
        myBookingsPage = new MyBookingsPage(currentUser);

        // Only create admin panel if user is admin
        if (currentUser.getRole().toString().equals("ADMIN")) {
            adminPanelPage = new AdminPanelPage(currentUser);
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
        myBookingsBtn = createNavButton("📋 My Bookings", "mybookings");

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
        userBox.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 10; -fx-padding: 15;");

        Label userLabel = new Label("👤 " + currentUser.getName());
        userLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        userLabel.setStyle("-fx-text-fill: white;");

        Label roleLabel = new Label(currentUser.getRole().toString());
        roleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        roleLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.7);");

        userBox.getChildren().addAll(userLabel, roleLabel);
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
            case "booking":
                content = bookingPage;
                setActiveButton(bookingBtn);
                break;
            case "mybookings":
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

    private void handleLogout() {
        // In a real application, you'd handle logout properly
        // For now, just exit the application
        System.exit(0);
    }
}