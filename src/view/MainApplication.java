package view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import model.services.AuthService;
import view.pages.LoginPage;

public class MainApplication extends Application {

    private Stage primaryStage;
    private User currentUser;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("IIUM Library Booking System");

        // Show login screen as the first layer
        showLoginPage();
    }

    private void showLoginPage() {
        LoginPage loginPage = new LoginPage(this::onLoginSuccess);

        Scene scene = new Scene(loginPage, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void onLoginSuccess(User user) {
        this.currentUser = user;
        showMainLayout();
    }

    private void showMainLayout() {
        // Create the main layout with navigation
        MainLayout mainLayout = new MainLayout(currentUser, this::showLoginPage);

        Scene scene = new Scene(mainLayout, 1400, 900);
        scene.getStylesheets().add(getClass().getResource("/styles/theme.css").toExternalForm());
        primaryStage.setTitle("IIUM Library Booking System - " + currentUser.getName());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showLoginFailed() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new javafx.geometry.Insets(20));

        Label label = new Label("Login Failed - Please check credentials");
        label.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");

        Button retryBtn = new Button("Retry");
        retryBtn.setOnAction(e -> showLoginPage());

        root.getChildren().addAll(label, retryBtn);

        Scene scene = new Scene(root, 400, 200);
        primaryStage.setTitle("Login Failed");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}