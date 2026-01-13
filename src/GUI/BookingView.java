package GUI;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import model.Room;
import model.Booking;
import model.User;
import model.SessionManager;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BookingView extends Application {

    // ===== Data =====
    private final ObservableList<RoomDisplay> rooms = FXCollections.observableArrayList();
    private final ObservableList<BookingDisplay> bookings = FXCollections.observableArrayList();
    private Room initialRoom;

    // ===== UI controls =====
    private ComboBox<RoomDisplay> roomCombo;
    private DatePicker startDatePicker;
    private ComboBox<String> startTimeCombo;
    private DatePicker endDatePicker;
    private ComboBox<String> endTimeCombo;

    private Label roomAvailabilityLabel;
    private Label statusLabel;

    private TableView<BookingDisplay> bookingTable;
    
    // Constructor to accept selected room
    public BookingView(Room selectedRoom) {
        this.initialRoom = selectedRoom;
    }
    
    // Default constructor for backward compatibility
    public BookingView() {
        this.initialRoom = null;
    }

    @Override
    public void start(Stage stage) {
        loadRoomsFromSession();
        loadUserBookings();

        // --- Form controls ---
        roomCombo = new ComboBox<>(rooms);
        roomCombo.setPrefWidth(260);
        roomCombo.setPromptText("Select room...");
        roomCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(RoomDisplay item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item.getRoomId() + " (" + item.getType() + ") - " + item.getAvailability());
            }
        });
        roomCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(RoomDisplay item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText("Select room...");
                else setText(item.getRoomId() + " (" + item.getType() + ")");
            }
        });
        
        // Pre-select the initial room if provided
        if (initialRoom != null) {
            for (RoomDisplay rd : rooms) {
                if (rd.getRoomId().equals(initialRoom.getRoomID())) {
                    roomCombo.setValue(rd);
                    break;
                }
            }
        }

        roomAvailabilityLabel = new Label("Availability: -");

        roomCombo.valueProperty().addListener((obs, oldV, newV) -> updateRoomAvailabilityLabel(newV));

        startDatePicker = new DatePicker(LocalDate.now());
        endDatePicker = new DatePicker(LocalDate.now());

        startTimeCombo = new ComboBox<>(FXCollections.observableArrayList(generateTimes()));
        endTimeCombo = new ComboBox<>(FXCollections.observableArrayList(generateTimes()));
        startTimeCombo.setPrefWidth(140);
        endTimeCombo.setPrefWidth(140);
        startTimeCombo.getSelectionModel().select("09:00");
        endTimeCombo.getSelectionModel().select("10:00");

        // --- Buttons ---
        Button bookNowBtn = new Button("Book Now");
        Button cancelBtn = new Button("Cancel Booking");

        bookNowBtn.setOnAction(e -> handleBookNow());
        cancelBtn.setOnAction(e -> handleCancelBooking());

        statusLabel = new Label("Ready.");
        statusLabel.setWrapText(true);

        // --- Bookings table ---
        bookingTable = new TableView<>(bookings);
        bookingTable.setPrefHeight(260);

        TableColumn<BookingDisplay, String> colId = new TableColumn<>("Booking ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colId.setPrefWidth(100);

        TableColumn<BookingDisplay, String> colRoom = new TableColumn<>("Room");
        colRoom.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colRoom.setPrefWidth(90);

        TableColumn<BookingDisplay, String> colStart = new TableColumn<>("Start");
        colStart.setCellValueFactory(new PropertyValueFactory<>("startFormatted"));
        colStart.setPrefWidth(170);

        TableColumn<BookingDisplay, String> colEnd = new TableColumn<>("End");
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endFormatted"));
        colEnd.setPrefWidth(170);

        TableColumn<BookingDisplay, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setPrefWidth(120);

        bookingTable.getColumns().addAll(colId, colRoom, colStart, colEnd, colStatus);

        // --- Layout ---
        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(10);

        int r = 0;
        form.add(new Label("Room:"), 0, r);
        form.add(roomCombo, 1, r);
        form.add(roomAvailabilityLabel, 2, r);

        r++;
        form.add(new Label("Start:"), 0, r);
        form.add(hbox(8, startDatePicker, startTimeCombo), 1, r);

        r++;
        form.add(new Label("End:"), 0, r);
        form.add(hbox(8, endDatePicker, endTimeCombo), 1, r);

        HBox buttons = hbox(10, bookNowBtn, cancelBtn);
        buttons.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(14,
                title("Library Booking System"),
                form,
                buttons,
                new Separator(),
                subtitle("My Bookings"),
                bookingTable,
                new Separator(),
                statusLabel
        );
        root.setPadding(new Insets(16));

        User currentUser = SessionManager.getInstance().getCurrentUser();
        String userInfo = currentUser != null ? currentUser.getMatricNo() : "Guest";
        
        Scene scene = new Scene(root, 760, 560);
        stage.setTitle("Booking System - " + userInfo);
        stage.setScene(scene);
        stage.show();
    }

    // ===== Handlers =====

    private void handleBookNow() {
        RoomDisplay selectedRoom = roomCombo.getValue();
        if (selectedRoom == null) {
            setStatus("Please select a room.");
            return;
        }
        if (!"Available".equals(selectedRoom.getAvailability())) {
            setStatus("Room " + selectedRoom.getRoomId() + " is not available.");
            return;
        }

        LocalDateTime start = combine(startDatePicker.getValue(), startTimeCombo.getValue());
        LocalDateTime end = combine(endDatePicker.getValue(), endTimeCombo.getValue());

        if (start == null || end == null) {
            setStatus("Please select valid start/end date & time.");
            return;
        }
        if (!end.isAfter(start)) {
            setStatus("End time must be after start time.");
            return;
        }

        // Find actual Room from session
        Room actualRoom = findRoomById(selectedRoom.getRoomId());
        if (actualRoom == null) {
            setStatus("Room not found.");
            return;
        }

        // Get current user
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            setStatus("No user logged in.");
            return;
        }

        // Make booking through User model
        boolean success = currentUser.makeBooking(actualRoom, start, end);
        if (success) {
            setStatus("Booked successfully for room " + actualRoom.getRoomID());
            
            // Refresh display
            loadRoomsFromSession();
            loadUserBookings();
            
            // Update combo box
            for (RoomDisplay rd : rooms) {
                if (rd.getRoomId().equals(actualRoom.getRoomID())) {
                    roomCombo.setValue(rd);
                    updateRoomAvailabilityLabel(rd);
                    break;
                }
            }
        } else {
            setStatus("Booking failed. Room may already be booked.");
        }
    }

    private void handleCancelBooking() {
        BookingDisplay selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setStatus("Please select a booking from the table to cancel.");
            return;
        }
        if (!"Active".equals(selected.getStatus())) {
            setStatus("This booking is already " + selected.getStatus() + ".");
            return;
        }

        // Get current user
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            setStatus("No user logged in.");
            return;
        }

        // Find the actual booking
        Booking actualBooking = null;
        for (Booking b : currentUser.getMyBookings()) {
            if (b.getBookingID().equals(selected.getBookingId())) {
                actualBooking = b;
                break;
            }
        }

        if (actualBooking != null) {
            boolean success = currentUser.cancelBooking(actualBooking);
            if (success) {
                setStatus("Cancelled booking " + actualBooking.getBookingID() + ". Room is available again.");
                
                // Refresh display
                loadRoomsFromSession();
                loadUserBookings();
            } else {
                setStatus("Failed to cancel booking.");
            }
        }
    }

    // ===== Helpers =====

    private void loadRoomsFromSession() {
        rooms.clear();
        for (Room r : SessionManager.getInstance().getAllRooms()) {
            rooms.add(new RoomDisplay(r.getRoomID(), r.getType(), r.getCapacity(), 
                                      r.getLocation(), r.getAvailabilityStatus()));
        }
    }
    
    private void loadUserBookings() {
        bookings.clear();
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            for (Booking b : currentUser.getMyBookings()) {
                bookings.add(new BookingDisplay(b.getBookingID(), b.getRoomID(), 
                                                b.getStartTime(), b.getEndTime(), b.getStatus()));
            }
        }
    }

    private Room findRoomById(String roomId) {
        for (Room r : SessionManager.getInstance().getAllRooms()) {
            if (r.getRoomID().equals(roomId)) return r;
        }
        return null;
    }

    private void updateRoomAvailabilityLabel(RoomDisplay room) {
        if (room == null) {
            roomAvailabilityLabel.setText("Availability: -");
        } else {
            roomAvailabilityLabel.setText("Availability: " + room.getAvailability());
            if ("Available".equals(room.getAvailability())) {
                roomAvailabilityLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            } else {
                roomAvailabilityLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }

    private void setStatus(String msg) {
        statusLabel.setText(msg);
    }

    private static List<String> generateTimes() {
        // 30-minute intervals
        List<String> times = new ArrayList<>();
        for (int h = 0; h < 24; h++) {
            for (int m = 0; m < 60; m += 30) {
                times.add(String.format("%02d:%02d", h, m));
            }
        }
        return times;
    }

    private static LocalDateTime combine(LocalDate date, String timeStr) {
        try {
            if (date == null || timeStr == null) return null;
            LocalTime t = LocalTime.parse(timeStr);
            return LocalDateTime.of(date, t);
        } catch (Exception e) {
            return null;
        }
    }

    private static String formatDT(LocalDateTime dt) {
        if (dt == null) return "-";
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dt.format(f);
    }

    private static Label title(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        return l;
    }

    private static Label subtitle(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        return l;
    }

    private static HBox hbox(double spacing, javafx.scene.Node... nodes) {
        HBox hb = new HBox(spacing, nodes);
        hb.setAlignment(Pos.CENTER_LEFT);
        return hb;
    }

    // ===== Display Models for JavaFX TableView =====

    public static class RoomDisplay {
        private final StringProperty roomId = new SimpleStringProperty();
        private final StringProperty type = new SimpleStringProperty();
        private final IntegerProperty capacity = new SimpleIntegerProperty();
        private final StringProperty location = new SimpleStringProperty();
        private final StringProperty availability = new SimpleStringProperty();

        public RoomDisplay(String roomId, String type, int capacity, String location, String availability) {
            this.roomId.set(roomId);
            this.type.set(type);
            this.capacity.set(capacity);
            this.location.set(location);
            this.availability.set(availability);
        }

        public String getRoomId() { return roomId.get(); }
        public String getType() { return type.get(); }
        public int getCapacity() { return capacity.get(); }
        public String getLocation() { return location.get(); }
        public String getAvailability() { return availability.get(); }

        @Override
        public String toString() {
            return getRoomId();
        }
    }

    public static class BookingDisplay {
        private final StringProperty bookingId = new SimpleStringProperty();
        private final StringProperty roomId = new SimpleStringProperty();
        private final ObjectProperty<LocalDateTime> start = new SimpleObjectProperty<>();
        private final ObjectProperty<LocalDateTime> end = new SimpleObjectProperty<>();
        private final StringProperty status = new SimpleStringProperty();

        public BookingDisplay(String bookingId, String roomId, LocalDateTime start, LocalDateTime end, String status) {
            this.bookingId.set(bookingId);
            this.roomId.set(roomId);
            this.start.set(start);
            this.end.set(end);
            this.status.set(status);
        }

        public String getBookingId() { return bookingId.get(); }
        public String getRoomId() { return roomId.get(); }
        public LocalDateTime getStart() { return start.get(); }
        public LocalDateTime getEnd() { return end.get(); }
        public String getStatus() { return status.get(); }
        
        public String getStartFormatted() {
            return formatDT(getStart());
        }
        
        public String getEndFormatted() {
            return formatDT(getEnd());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
