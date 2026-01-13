package GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.User;
import model.SessionManager;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) {
        
      
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        
        root.setStyle("-fx-background-color: #f4f4f4;"); 

      
        Label titleLabel = new Label("library reservation system");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #333333;");

      
        Label subLabel = new Label("Please sign in to continue");
        subLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 14px;");

        
        Label matricLabel = new Label("Matric Number");
    
        matricLabel.setMaxWidth(300); 
        
        TextField matricField = new TextField();
        matricField.setPromptText("e.g. 20241234");
        matricField.setMaxWidth(300);
        matricField.setPrefHeight(35);

       
        Label passLabel = new Label("Password");
        passLabel.setMaxWidth(300);
        
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");
        passField.setMaxWidth(300);
        passField.setPrefHeight(35);

     
        Button loginBtn = new Button("LOGIN");
        loginBtn.setMaxWidth(300);
        loginBtn.setPrefHeight(40);
        loginBtn.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-font-weight: bold;");
        
       
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

       
        loginBtn.setOnAction(e -> {
            String matric = matricField.getText();
            String pass = passField.getText();

            if (matric.isEmpty() || pass.isEmpty()) {
                errorLabel.setText("Please enter both Matric No and Password.");
            } else {
                // Authenticate user
                User user = User.login(matric, pass);
                if (user != null) {
                    // Save user to session
                    SessionManager.getInstance().setCurrentUser(user);
                    errorLabel.setText("Login successful!");
                    errorLabel.setStyle("-fx-text-fill: green;");
                    
                    // Open RoomListView
                    try {
                        RoomListView roomListView = new RoomListView();
                        Stage roomStage = new Stage();
                        Scene roomScene = new Scene(roomListView, 700, 500);
                        roomStage.setTitle("Library Room List - " + matric);
                        roomStage.setScene(roomScene);
                        roomStage.show();
                        
                        // Close login window
                        primaryStage.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        errorLabel.setText("Error opening room list.");
                        errorLabel.setStyle("-fx-text-fill: red;");
                    }
                } else {
                    errorLabel.setText("Invalid credentials.");
                    errorLabel.setStyle("-fx-text-fill: red;");
                }
            }
        });

       
        root.getChildren().addAll(
            titleLabel, 
            subLabel, 
            new Label(""),
            matricLabel, 
            matricField, 
            passLabel, 
            passField, 
            new Label(""), 
            loginBtn,
            errorLabel
        );

    
        Scene scene = new Scene(root, 400, 500);
        primaryStage.setTitle("Login Prototype");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
