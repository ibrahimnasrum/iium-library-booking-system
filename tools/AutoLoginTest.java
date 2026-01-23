import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.pages.LoginPage;
import model.User;

public class AutoLoginTest extends Application {
    @Override
    public void start(Stage primaryStage) {
        System.out.println("AutoLoginTest: starting");
        LoginPage loginPage = new LoginPage(user -> {
            System.out.println("AutoLoginTest: login success -> " + (user != null ? user.getMatricNo() + " (" + user.getRole() + ")" : "null"));
            // exit after successful login
            System.exit(0);
        });

        Scene scene = new Scene(loginPage, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Trigger a quick student login after UI is visible
        javafx.application.Platform.runLater(() -> {
            loginPage.quickLoginAs("2123456", "password");
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}