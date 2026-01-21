package view.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.User;
import model.services.AuthService;

import java.util.function.Consumer;

public class LoginPage extends VBox {

    private TextField matricField;
    private PasswordField passwordField;
    private Button loginButton;
    private Label statusLabel;
    private Consumer<User> onLoginSuccess;

    // Quick login buttons made fields so they can be used in layout
    private Button adminBtn;
    private Button staffBtn;
    private Button studentBtn;
    private Button postgradBtn;

    public LoginPage(Consumer<User> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
        System.out.println("LoginPage: Constructor called - creating login screen");
        initializeComponents();
        setupLayout();
        System.out.println("LoginPage: Login screen setup complete");
    }

    private void initializeComponents() {
        // Matric number field
        matricField = new TextField();
        matricField.setPromptText("Enter IIUM Matric Number (e.g., 3123456)");
        matricField.setMaxWidth(300);
        matricField.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

        // Password field
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(300);
        passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

        // Login button
        loginButton = new Button("Login to IIUM Library System");
        loginButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 25;");
        loginButton.setOnAction(e -> handleLogin());

        // Status label
        statusLabel = new Label("");
        statusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");
        statusLabel.setVisible(false);

        // Quick login buttons for testing (assign to fields so layout can access them)
        postgradBtn = new Button("Postgrad (3...)");
        studentBtn = new Button("Student (2...) ");
        staffBtn = new Button("Staff (1...)");
        adminBtn = new Button("Admin (0...)");

        String quickBtnStyle = "-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 16; -fx-background-radius: 15;";

        postgradBtn.setStyle(quickBtnStyle);
        studentBtn.setStyle(quickBtnStyle);
        staffBtn.setStyle(quickBtnStyle);
        adminBtn.setStyle(quickBtnStyle);

        postgradBtn.setOnAction(e -> matricField.setText("3123456"));
        studentBtn.setOnAction(e -> matricField.setText("2123456"));
        staffBtn.setOnAction(e -> matricField.setText("1123456"));
        adminBtn.setOnAction(e -> matricField.setText("0123456"));

        // Handle Enter key in password field
        passwordField.setOnAction(e -> handleLogin());
    }

    private void setupLayout() {
        setSpacing(20);
        setPadding(new Insets(50));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #667eea 0%, #764ba2 100%);");

        // Header
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("🏛️ IIUM Library Booking System");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setStyle("-fx-text-fill: white;");

        Label subtitleLabel = new Label("Please login with your IIUM Matric Number");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        subtitleLabel.setStyle("-fx-text-fill: #ecf0f1;");

        headerBox.getChildren().addAll(titleLabel, subtitleLabel);

        // Login form
        VBox loginBox = new VBox(15);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(30));
        loginBox.setMaxWidth(400);
        loginBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.1, 0, 5);");

        Label matricLabel = new Label("IIUM Matric Number:");
        matricLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Quick login buttons row
        VBox quickLoginBox = new VBox(10);
        quickLoginBox.setAlignment(Pos.CENTER);

        Label quickLoginLabel = new Label("Quick Login (for testing):");
        quickLoginLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");

        HBox quickButtons = new HBox(10);
        quickButtons.setAlignment(Pos.CENTER);
        quickButtons.getChildren().addAll(adminBtn, staffBtn, studentBtn, postgradBtn);

        quickLoginBox.getChildren().addAll(quickLoginLabel, quickButtons);

        loginBox.getChildren().addAll(
            matricLabel, matricField,
            passwordLabel, passwordField,
            loginButton, statusLabel, quickLoginBox
        );

        // Footer
        VBox footerBox = new VBox(5);
        footerBox.setAlignment(Pos.CENTER);

        Label roleInfoLabel = new Label("User Roles:");
        roleInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        roleInfoLabel.setStyle("-fx-text-fill: #ecf0f1;");

        Label roleDetailsLabel = new Label("0=Admin • 1=Staff • 2=Student • 3=Postgraduate");
        roleDetailsLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        roleDetailsLabel.setStyle("-fx-text-fill: #bdc3c7;");

        footerBox.getChildren().addAll(roleInfoLabel, roleDetailsLabel);

        getChildren().addAll(headerBox, loginBox, footerBox);
    }

    private void handleLogin() {
        String matricNo = matricField.getText().trim();
        String password = passwordField.getText();

        // Validate input
        if (matricNo.isEmpty()) {
            showError("Please enter your IIUM Matric Number");
            return;
        }

        if (password.isEmpty()) {
            showError("Please enter your password");
            return;
        }

        // Attempt login
        User user = AuthService.login(matricNo, password);

        if (user != null) {
            System.out.println("Login successful for user: " + user.getMatricNo() + " (Role: " + user.getRole() + ")");
            statusLabel.setVisible(false);

            // Notify success
            if (onLoginSuccess != null) {
                onLoginSuccess.accept(user);
            }
        } else {
            showError("Invalid matric number or password. Please check your credentials.");
            System.out.println("Login failed for matric: " + matricNo);
        }
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");
        statusLabel.setVisible(true);
    }

    public void clearFields() {
        matricField.clear();
        passwordField.clear();
        statusLabel.setVisible(false);
    }
}