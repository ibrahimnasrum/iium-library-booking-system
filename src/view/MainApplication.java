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

public class MainApplication extends Application {

    private Stage primaryStage;
    private User currentUser;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Auto-login as admin for testing
        currentUser = AuthService.login("1123456", "password");

        if (currentUser != null) {
            showMainLayout();
        } else {
            showLoginFailed();
        }
    }

    private void showMainLayout() {
        // Create the main layout with navigation
        MainLayout mainLayout = new MainLayout(currentUser);

        Scene scene = new Scene(mainLayout, 1400, 900);
        // scene.getStylesheets().add(getClass().getResource("/styles/theme.css").toExternalForm());
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
        retryBtn.setOnAction(e -> start(primaryStage));

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