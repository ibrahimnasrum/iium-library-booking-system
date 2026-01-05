package com.mycompany.mavenproject1;

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

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BookingView extends Application {

    // ===== Mock data (since we're ignoring integration) =====
    private final ObservableList<Room> rooms = FXCollections.observableArrayList();
    private final ObservableList<Booking> bookings = FXCollections.observableArrayList();
    private final AtomicInteger bookingIdCounter = new AtomicInteger(1001);

    // ===== UI controls =====
    private ComboBox<Room> roomCombo;
    private DatePicker startDatePicker;
    private ComboBox<String> startTimeCombo;
    private DatePicker endDatePicker;
    private ComboBox<String> endTimeCombo;

    private Label roomAvailabilityLabel;
    private Label statusLabel;

    private TableView<Booking> bookingTable;

    @Override
    public void start(Stage stage) {
        seedRooms();

        // --- Form controls ---
        roomCombo = new ComboBox<>(rooms);
        roomCombo.setPrefWidth(260);
        roomCombo.setPromptText("Select room...");
        roomCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Room item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item.getRoomId() + " (" + item.getType() + ") - " + (item.isAvailable() ? "Available" : "Booked"));
            }
        });
        roomCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Room item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText("Select room...");
                else setText(item.getRoomId() + " (" + item.getType() + ")");
            }
        });

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

        TableColumn<Booking, Integer> colId = new TableColumn<>("Booking ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colId.setPrefWidth(100);

        TableColumn<Booking, String> colRoom = new TableColumn<>("Room");
        colRoom.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colRoom.setPrefWidth(90);

        TableColumn<Booking, String> colStart = new TableColumn<>("Start");
        colStart.setCellValueFactory(c -> new SimpleStringProperty(formatDT(c.getValue().getStart())));
        colStart.setPrefWidth(170);

        TableColumn<Booking, String> colEnd = new TableColumn<>("End");
        colEnd.setCellValueFactory(c -> new SimpleStringProperty(formatDT(c.getValue().getEnd())));
        colEnd.setPrefWidth(170);

        TableColumn<Booking, String> colStatus = new TableColumn<>("Status");
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
                title("Booking (GUI Only)"),
                form,
                buttons,
                new Separator(),
                subtitle("My Bookings"),
                bookingTable,
                new Separator(),
                statusLabel
        );
        root.setPadding(new Insets(16));

        Scene scene = new Scene(root, 760, 560);
        stage.setTitle("BookingView - Ibrahim Part (GUI Only)");
        stage.setScene(scene);
        stage.show();
    }

    // ===== Handlers =====

    private void handleBookNow() {
        Room selectedRoom = roomCombo.getValue();
        if (selectedRoom == null) {
            setStatus("Please select a room.");
            return;
        }
        if (!selectedRoom.isAvailable()) {
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

        // OPTIONAL: simple collision check (same room + overlaps with BOOKED bookings)
        if (hasOverlap(selectedRoom.getRoomId(), start, end)) {
            setStatus("This room already has a booking that overlaps the selected time.");
            return;
        }

        // "User.makeBooking(...)" would be called here in real integration.
        Booking newBooking = new Booking(
                bookingIdCounter.getAndIncrement(),
                selectedRoom.getRoomId(),
                start,
                end,
                "BOOKED"
        );
        bookings.add(newBooking);

        // Update room availability (minimum interaction requirement)
        selectedRoom.setAvailable(false);
        refreshRoomCombo();

        updateRoomAvailabilityLabel(selectedRoom);

        setStatus("Booked successfully: #" + newBooking.getBookingId() + " for room " + selectedRoom.getRoomId());
    }

    private void handleCancelBooking() {
        Booking selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setStatus("Please select a booking from the table to cancel.");
            return;
        }
        if (!"BOOKED".equals(selected.getStatus())) {
            setStatus("This booking is already " + selected.getStatus() + ".");
            return;
        }

        // "User.cancelBooking(...)" would be called here in real integration.
        selected.setStatus("CANCELLED");
        bookingTable.refresh();

        // Make room available again (minimum interaction requirement)
        Room room = findRoomById(selected.getRoomId());
        if (room != null) {
            room.setAvailable(true);
            refreshRoomCombo();

            if (roomCombo.getValue() != null && roomCombo.getValue().getRoomId().equals(room.getRoomId())) {
                updateRoomAvailabilityLabel(room);
            }
        }

        setStatus("Cancelled booking #" + selected.getBookingId() + ". Room is available again.");
    }

    // ===== Helpers =====

    private void seedRooms() {
        rooms.addAll(
                new Room("R101", "Lecture", 60, "Block A", true),
                new Room("R102", "Lab", 30, "Block A", true),
                new Room("R201", "Meeting", 12, "Block B", true),
                new Room("R202", "Seminar", 40, "Block B", true)
        );
    }

    private Room findRoomById(String roomId) {
        for (Room r : rooms) {
            if (Objects.equals(r.getRoomId(), roomId)) return r;
        }
        return null;
    }

    private boolean hasOverlap(String roomId, LocalDateTime start, LocalDateTime end) {
        for (Booking b : bookings) {
            if (!"BOOKED".equals(b.getStatus())) continue;
            if (!Objects.equals(b.getRoomId(), roomId)) continue;

            // Overlap check: start < existingEnd AND end > existingStart
            if (start.isBefore(b.getEnd()) && end.isAfter(b.getStart())) return true;
        }
        return false;
    }

    private void updateRoomAvailabilityLabel(Room room) {
        if (room == null) {
            roomAvailabilityLabel.setText("Availability: -");
        } else {
            roomAvailabilityLabel.setText("Availability: " + (room.isAvailable() ? "Available" : "Booked"));
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

    private void refreshRoomCombo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // ===== Models =====

    public static class Room {
        private final StringProperty roomId = new SimpleStringProperty();
        private final StringProperty type = new SimpleStringProperty();
        private final IntegerProperty capacity = new SimpleIntegerProperty();
        private final StringProperty location = new SimpleStringProperty();
        private final BooleanProperty available = new SimpleBooleanProperty(true);

        public Room(String roomId, String type, int capacity, String location, boolean available) {
            this.roomId.set(roomId);
            this.type.set(type);
            this.capacity.set(capacity);
            this.location.set(location);
            this.available.set(available);
        }

        public String getRoomId() { return roomId.get(); }
        public String getType() { return type.get(); }
        public int getCapacity() { return capacity.get(); }
        public String getLocation() { return location.get(); }
        public boolean isAvailable() { return available.get(); }

        public void setAvailable(boolean v) { available.set(v); }

        @Override
        public String toString() {
            return getRoomId();
        }
    }

    public static class Booking {
        private final IntegerProperty bookingId = new SimpleIntegerProperty();
        private final StringProperty roomId = new SimpleStringProperty();
        private final ObjectProperty<LocalDateTime> start = new SimpleObjectProperty<>();
        private final ObjectProperty<LocalDateTime> end = new SimpleObjectProperty<>();
        private final StringProperty status = new SimpleStringProperty();

        public Booking(int bookingId, String roomId, LocalDateTime start, LocalDateTime end, String status) {
            this.bookingId.set(bookingId);
            this.roomId.set(roomId);
            this.start.set(start);
            this.end.set(end);
            this.status.set(status);
        }

        public int getBookingId() { return bookingId.get(); }
        public String getRoomId() { return roomId.get(); }
        public LocalDateTime getStart() { return start.get(); }
        public LocalDateTime getEnd() { return end.get(); }
        public String getStatus() { return status.get(); }

        public void setStatus(String s) { status.set(s); }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
