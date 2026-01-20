package view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.Facility;
import model.enums.FacilityStatus;
import model.enums.ReservationPrivilege;

public class FacilityCard extends VBox {

    private Facility facility;
    private Runnable onCardClicked;
    private VBox contentBox;

    public FacilityCard(Facility facility) {
        this.facility = facility;
        setupCard();
    }

    private void setupCard() {
        setPrefSize(180, 140); // Increased height from 120 to 140 to accommodate status
        setAlignment(Pos.CENTER); // Center the content in the StackPane

        // Create content VBox
        contentBox = new VBox(4); // Reduced spacing from 8 to 4
        contentBox.setSpacing(4);
        contentBox.setPadding(new Insets(10)); // Reduced padding from 15 to 10
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPrefSize(180, 140); // Make sure it fills the entire card

        // Add colorful background based on facility type with very thin black outline and enhanced shadow
        String backgroundColor = getFacilityTypeColor(facility.getType());
        contentBox.setStyle("-fx-background-color: " + backgroundColor + "; -fx-background-radius: 15; -fx-border-color: #000000; -fx-border-width: 0.5; -fx-border-radius: 15; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0.4, 0, 6);");

        // Facility icon at the top
        Label iconLabel = new Label(getFacilityIcon(facility.getType()));
        iconLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #000000; -fx-font-weight: bold;");
        iconLabel.setAlignment(Pos.CENTER);

        // Facility name (centered, black text, better typography)
        Label nameLabel = new Label(facility.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #000000; -fx-text-alignment: center;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(150);

        // Facility type (smaller, dark gray text)
        Label typeLabel = new Label(facility.getType().toString().replace("_", " "));
        typeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #000000; -fx-text-alignment: center; -fx-font-style: italic; -fx-font-weight: bold;");
        typeLabel.setWrapText(true);
        typeLabel.setMaxWidth(150);

        // Status badge - positioned at bottom center
        Label statusLabel = new Label(getStatusDisplayText(facility.getStatus()));
        statusLabel.getStyleClass().add(getStatusBadgeClass(facility.getStatus()));
        statusLabel.setStyle(statusLabel.getStyle() + "; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0.5, 0, 1);");

        contentBox.getChildren().addAll(iconLabel, nameLabel, typeLabel, statusLabel);

        // Add the content box to the VBox
        getChildren().add(contentBox);
        setOnMouseClicked(e -> {
            if (onCardClicked != null) {
                onCardClicked.run();
            }
        });

        // Enhanced hover effects
        setOnMouseEntered(e -> contentBox.setStyle("-fx-background-color: " + backgroundColor + "; -fx-background-radius: 15; -fx-border-color: #333333; -fx-border-width: 1; -fx-border-radius: 15; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 16, 0.5, 0, 8);"));
        setOnMouseExited(e -> contentBox.setStyle("-fx-background-color: " + backgroundColor + "; -fx-background-radius: 15; -fx-border-color: #000000; -fx-border-width: 0.5; -fx-border-radius: 15; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0.4, 0, 6);"));
    }

    private String getStatusColor(FacilityStatus status) {
        switch (status) {
            case AVAILABLE: return "#27ae60"; // Green
            case BOOKED: return "#e74c3c"; // Red
            case MAINTENANCE: return "#f39c12"; // Orange
            case TEMPORARILY_CLOSED: return "#95a5a6"; // Gray
            case RESERVED: return "#9b59b6"; // Purple
            default: return "#95a5a6"; // Gray
        }
    }

    private String getStatusBadgeClass(FacilityStatus status) {
        switch (status) {
            case AVAILABLE: return "badge-available";
            case BOOKED: return "badge-booked";
            case TEMPORARILY_CLOSED: return "badge-closed";
            case MAINTENANCE: return "badge-maint";
            case RESERVED: return "badge-available"; // Use available for reserved
            default: return "badge-closed";
        }
    }

    private String getPrivilegeBadgeClass(ReservationPrivilege privilege) {
        switch (privilege) {
            case OPEN: return "badge-open";
            case STUDENT_ONLY: return "badge-open"; // Use open color for students
            case STAFF_ONLY: return "badge-staff";
            case POSTGRADUATE_ONLY: return "badge-postgrad";
            case BOOK_VENDORS_ONLY: return "badge-vendor";
            case LIBRARY_USE_ONLY: return "badge-staff"; // Use staff color for library use
            case SPECIAL_NEEDS_ONLY: return "badge-open"; // Use open color for special needs
            default: return "badge-open";
        }
    }

    private String getStatusDisplayText(model.enums.FacilityStatus status) {
        switch (status) {
            case AVAILABLE: return "✓ AVAILABLE";
            case BOOKED: return "🔒 BOOKED";
            case TEMPORARILY_CLOSED: return "⛔ CLOSED";
            case MAINTENANCE: return "🔧 MAINTENANCE";
            case RESERVED: return "📅 RESERVED";
            default: return "❓ UNKNOWN";
        }
    }

    public Facility getFacility() {
        return facility;
    }

    public void updateStatus(model.enums.FacilityStatus newStatus) {
        facility.setStatus(newStatus);
        // Find and update the status label (it's the last child in contentBox)
        if (contentBox.getChildren().size() > 0) {
            javafx.scene.Node lastNode = contentBox.getChildren().get(contentBox.getChildren().size() - 1);
            if (lastNode instanceof Label) {
                Label statusLabel = (Label) lastNode;
                statusLabel.setText(getStatusDisplayText(newStatus));
                // Clear existing style classes and add the new one
                statusLabel.getStyleClass().clear();
                statusLabel.getStyleClass().add(getStatusBadgeClass(newStatus));
                statusLabel.setStyle(statusLabel.getStyle() + "; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0.5, 0, 1);");
            }
        }
    }

    public void setOnCardClicked(Runnable handler) {
        this.onCardClicked = handler;
    }

    private String getFacilityIcon(model.enums.FacilityType type) {
        switch (type) {
            case ROOM: return "🏠";
            case STUDY_AREA: return "📚";
            case COMPUTER_LAB: return "💻";
            case AUDITORIUM: return "🎭";
            case EXHIBITION_AREA: return "🎨";
            case VIEWING_ROOM: return "📺";
            case DISCUSSION_ROOM: return "💬";
            case CARREL_ROOM: return "👤";
            case RESEARCH_ROOM: return "🔬";
            case MULTI_PURPOSE_ROOM: return "🏢";
            case SPECIAL_NEEDS_ROOM: return "♿";
            default: return "🏢";
        }
    }

    private String getFacilityTypeColor(model.enums.FacilityType type) {
        switch (type) {
            case ROOM: return "#87CEEB"; // Sky blue
            case STUDY_AREA: return "#FFB6C1"; // Light pink
            case COMPUTER_LAB: return "#98FB98"; // Pale green
            case AUDITORIUM: return "#FFA07A"; // Light salmon
            case EXHIBITION_AREA: return "#DDA0DD"; // Plum
            case VIEWING_ROOM: return "#B0E0E6"; // Powder blue
            case DISCUSSION_ROOM: return "#FFFFE0"; // Light yellow
            case CARREL_ROOM: return "#F0E68C"; // Khaki
            case RESEARCH_ROOM: return "#D8BFD8"; // Thistle
            case MULTI_PURPOSE_ROOM: return "#FFC0CB"; // Pink
            case SPECIAL_NEEDS_ROOM: return "#AFEEEE"; // Pale turquoise
            default: return "#F0F8FF"; // Alice blue
        }
    }
}