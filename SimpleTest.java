import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimpleTest extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("JavaFX Test - Click button to close");
        Button button = new Button("Close");

        button.setOnAction(e -> primaryStage.close());

        VBox root = new VBox(10, label, button);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("JavaFX Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}