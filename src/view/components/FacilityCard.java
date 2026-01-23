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
        setPrefSize(180, 160); // Reverted to original size for better screen density
        setAlignment(Pos.CENTER); // Center the content in the StackPane

        // Create content VBox
        contentBox = new VBox(3); // Reduced spacing from 4 to 3 for better fit
        contentBox.setSpacing(3);
        contentBox.setPadding(new Insets(10)); // Reduced padding from 15 to 10
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPrefSize(180, 160); // Make sure it fills the entire card

        // Add colorful background based on facility type with very thin black outline and enhanced shadow
        String backgroundColor = getFacilityTypeColor(facility.getType());
        contentBox.setStyle("-fx-background-color: " + backgroundColor + "; -fx-background-radius: 15; -fx-border-color: #000000; -fx-border-width: 0.5; -fx-border-radius: 15; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0.4, 0, 6);");

        // Facility icon at the top
        Label iconLabel = new Label(getFacilityIcon(facility.getType()));
        iconLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #000000; -fx-font-weight: bold;");
        iconLabel.setAlignment(Pos.CENTER);

        // Facility name (centered, black text, better typography) - remove redundant type info
        String displayName = cleanFacilityName(facility.getName(), facility.getType()).toUpperCase();
        Label nameLabel = new Label(displayName);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #000000; -fx-text-alignment: center;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(160); // Reverted to original width for compact display
        nameLabel.setPrefHeight(Label.USE_COMPUTED_SIZE);

        // Level information
        Label levelLabel = new Label("📍 " + facility.getLocation());
        levelLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #e74c3c; -fx-text-alignment: center; -fx-font-weight: bold;");
        levelLabel.setWrapText(true);
        levelLabel.setMaxWidth(160); // Reverted to original width for compact display

        // Privilege badge (show only if not OPEN)
        Label privilegeLabel = null;
        if (facility.getPrivilege() != ReservationPrivilege.OPEN) {
            privilegeLabel = new Label(getPrivilegeDisplayText(facility.getPrivilege()));
            privilegeLabel.getStyleClass().add(getPrivilegeBadgeClass(facility.getPrivilege()));
            privilegeLabel.setStyle(privilegeLabel.getStyle() + "; -fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 4 8; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0.3, 0, 1);");
        }

        // Status badge - positioned at bottom center
        Label statusLabel = new Label(getStatusDisplayText(facility.getStatus()));
        statusLabel.getStyleClass().add(getStatusBadgeClass(facility.getStatus()));
        statusLabel.setStyle(statusLabel.getStyle() + "; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, " + getStatusGlowColor(facility.getStatus()) + ", 8, 0.8, 0, 0), dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0.5, 0, 1);");

        if (privilegeLabel != null) {
            contentBox.getChildren().addAll(iconLabel, nameLabel, levelLabel, privilegeLabel, statusLabel);
        } else {
            contentBox.getChildren().addAll(iconLabel, nameLabel, levelLabel, statusLabel);
        }

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

    private String getStatusGlowColor(model.enums.FacilityStatus status) {
        switch (status) {
            case AVAILABLE: return "#27ae60"; // Green glow
            case BOOKED: return "#e74c3c"; // Red glow
            case MAINTENANCE: return "#f39c12"; // Orange glow
            case TEMPORARILY_CLOSED: return "#95a5a6"; // Gray glow
            case RESERVED: return "#9b59b6"; // Purple glow
            default: return "#95a5a6"; // Gray glow
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

    private String getPrivilegeDisplayText(ReservationPrivilege privilege) {
        switch (privilege) {
            case STUDENT_ONLY: return "👨‍🎓 STUDENTS";
            case STAFF_ONLY: return "👔 STAFF";
            case POSTGRADUATE_ONLY: return "🎓 POSTGRAD";
            case SPECIAL_NEEDS_ONLY: return "♿ ACCESSIBLE";
            case BOOK_VENDORS_ONLY: return "📚 VENDORS";
            case LIBRARY_USE_ONLY: return "🏛️ LIBRARY";
            default: return "";
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
                statusLabel.setStyle(statusLabel.getStyleClass().toString() + "; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, " + getStatusGlowColor(newStatus) + ", 8, 0.8, 0, 0), dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0.5, 0, 1);");
            }
        }
    }

    public void setOnCardClicked(Runnable handler) {
        this.onCardClicked = handler;
    }

    private String cleanFacilityName(String name, model.enums.FacilityType type) {
        if (name == null) return "";

        String cleanName = name.trim();

        // Remove common redundant prefixes
        cleanName = cleanName.replaceAll("(?i)^IIUM\\s+", "").trim();
        cleanName = cleanName.replaceAll("(?i)^Library\\s+", "").trim();

        // For facilities with parenthetical details, keep the core name + details
        // but avoid redundancy with the type label below
        String typeString = type.toString().replace("_", " ").toLowerCase();

        // For CARREL_ROOM, if name contains "Carrel Room", just remove "Room" to avoid duplication
        if (typeString.equals("carrel room") && cleanName.toLowerCase().contains("carrel room")) {
            cleanName = cleanName.replaceAll("(?i)room", "").trim();
        }
        // For COMPUTER_LAB, if name contains "Computer Lab", just remove "Lab" to avoid duplication
        else if (typeString.equals("computer lab") && cleanName.toLowerCase().contains("computer lab")) {
            cleanName = cleanName.replaceAll("(?i)lab", "").trim();
        }
        // For DISCUSSION_ROOM, if name contains "Discussion Room", just remove "Room" to avoid duplication
        else if (typeString.equals("discussion room") && cleanName.toLowerCase().contains("discussion room")) {
            cleanName = cleanName.replaceAll("(?i)room", "").trim();
        }
        // For other room types, remove "Room" only if it's redundant with the type
        else if (typeString.contains("room") && cleanName.toLowerCase().endsWith(" room")) {
            cleanName = cleanName.substring(0, cleanName.length() - 5).trim();
        }
        // For areas, remove "Area" only if it's redundant with the type
        else if (typeString.contains("area") && cleanName.toLowerCase().endsWith(" area")) {
            cleanName = cleanName.substring(0, cleanName.length() - 5).trim();
        }

        // If the name becomes too short or empty, use the original
        if (cleanName.length() < 2) {
            return name;
        }

        return cleanName.trim();
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