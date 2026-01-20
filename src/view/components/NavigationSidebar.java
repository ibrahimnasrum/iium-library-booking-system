package view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import model.User;
import model.enums.Role;

public class NavigationSidebar extends VBox {

    private Runnable onDashboardSelected;
    private Runnable onFacilitiesSelected;
    private Runnable onMyBookingsSelected;
    private Runnable onAdminPanelSelected;

    public NavigationSidebar(User currentUser) {
        setupSidebar(currentUser);
    }

    private void setupSidebar(User currentUser) {
        setPrefWidth(200);
        setStyle("-fx-background-color: #2c3e50; -fx-padding: 20;");
        setSpacing(10);
        setAlignment(Pos.TOP_CENTER);

        // Title
        javafx.scene.control.Label title = new javafx.scene.control.Label("IIUM Library");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        title.setPadding(new Insets(0, 0, 20, 0));

        // Navigation buttons
        Button dashboardBtn = createNavButton("Dashboard");
        Button facilitiesBtn = createNavButton("Facilities");
        Button bookingsBtn = createNavButton("My Bookings");

        dashboardBtn.setOnAction(e -> {
            if (onDashboardSelected != null) onDashboardSelected.run();
        });

        facilitiesBtn.setOnAction(e -> {
            if (onFacilitiesSelected != null) onFacilitiesSelected.run();
        });

        bookingsBtn.setOnAction(e -> {
            if (onMyBookingsSelected != null) onMyBookingsSelected.run();
        });

        getChildren().add(title);
        getChildren().add(dashboardBtn);
        getChildren().add(facilitiesBtn);
        getChildren().add(bookingsBtn);

        // Admin panel for staff/admin
        if (currentUser != null && (currentUser.getRole() == Role.STAFF || currentUser.getRole() == Role.ADMIN)) {
            Button adminBtn = createNavButton("Admin Panel");
            adminBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
            adminBtn.setOnAction(e -> {
                if (onAdminPanelSelected != null) onAdminPanelSelected.run();
            });
            getChildren().add(adminBtn);
        }

        // Logout button
        Button logoutBtn = createNavButton("Logout");
        logoutBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> System.exit(0)); // Simple logout for demo
        getChildren().add(logoutBtn);
    }

    private Button createNavButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px;"));
        return button;
    }

    // Setters for navigation handlers
    public void setOnDashboardSelected(Runnable handler) {
        this.onDashboardSelected = handler;
    }

    public void setOnFacilitiesSelected(Runnable handler) {
        this.onFacilitiesSelected = handler;
    }

    public void setOnMyBookingsSelected(Runnable handler) {
        this.onMyBookingsSelected = handler;
    }

    public void setOnAdminPanelSelected(Runnable handler) {
        this.onAdminPanelSelected = handler;
    }
}