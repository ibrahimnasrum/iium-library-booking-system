package GUI;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Room;
import model.SessionManager;

public class RoomListView extends VBox {

    private final TableView<Room> table = new TableView<>();
    private final ObservableList<Room> masterData;

    public RoomListView() {
        // Layout
        setPadding(new Insets(15));
        setSpacing(10);

        // Data from SessionManager
        masterData = FXCollections.observableArrayList(SessionManager.getInstance().getAllRooms());

        // Table columns
        TableColumn<Room, String> idCol = new TableColumn<>("Room ID");
        idCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRoomID()));

        TableColumn<Room, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getType()));

        TableColumn<Room, String> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getCapacity())));

        TableColumn<Room, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLocation()));

        TableColumn<Room, String> statusCol = new TableColumn<>("Available");
        statusCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAvailabilityStatus()));
        statusCol.setCellFactory(tc -> new TableCell<Room, String>() {
            @Override
            protected void updateItem(String available, boolean empty) {
                super.updateItem(available, empty);
                if (empty || available == null) {
                    setText(null);
                } else {
                    setText(available);
                    if ("Available".equals(available)) {
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: red;");
                    }
                }
            }
        });

        table.getColumns().setAll(idCol, typeCol, capacityCol, locationCol, statusCol);
        table.setItems(masterData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Buttons
        Button btnAvailable = new Button("Show Available");
        Button btnRefresh = new Button("Refresh List");
        Button btnBook = new Button("Proceed to Booking");
        btnBook.setDisable(true);
        btnBook.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");

        // Enable booking only when a row is selected
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) ->
                btnBook.setDisable(newV == null)
        );

        // Actions
        btnAvailable.setOnAction(e -> showOnlyAvailable());
        btnRefresh.setOnAction(e -> refreshFromSession());

        btnBook.setOnAction(e -> {
            Room selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            BookingView bookingWindow = new BookingView(selected);
            Stage stage = new Stage();
            try {
                bookingWindow.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // UI
        Label title = new Label("IIUM Library Room List");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        HBox controls = new HBox(10, btnAvailable, btnRefresh, btnBook);

        getChildren().addAll(title, table, controls);
    }

    private void showOnlyAvailable() {
        table.setItems(masterData.filtered(room -> "Available".equals(room.getAvailabilityStatus())));
    }

    private void refreshFromSession() {
        masterData.setAll(SessionManager.getInstance().getAllRooms());
        table.setItems(masterData);
    }
}
