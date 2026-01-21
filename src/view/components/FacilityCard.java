package view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
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
        setPrefSize(220, 140); // Increased width from 200 to 220 to accommodate longer names
        setAlignment(Pos.CENTER); // Center the content in the StackPane

        // Create content VBox
        contentBox = new VBox(4); // Reduced spacing from 8 to 4
        contentBox.setSpacing(4);
        contentBox.setPadding(new Insets(10)); // Reduced padding from 15 to 10
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPrefSize(220, 140); // Make sure it fills the entire card

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
        nameLabel.setMaxWidth(190);

        // Level/Floor information (extracted from location)
        String levelInfo = getLevelInfo(facility.getLocation());
        Label levelLabel = new Label(levelInfo);
        levelLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666666; -fx-text-alignment: center; -fx-font-weight: normal;");
        levelLabel.setWrapText(true);
        levelLabel.setMaxWidth(190);
        levelLabel.setVisible(!levelInfo.isEmpty()); // Hide if no level info
        levelLabel.setManaged(!levelInfo.isEmpty()); // Remove from layout if no level info

        // Status badge - positioned at bottom center with left offset and enhanced glow
        Label statusLabel = new Label(getStatusDisplayText(facility.getStatus()));
        statusLabel.getStyleClass().add(getStatusBadgeClass(facility.getStatus()));
        String glowColor = getStatusGlowColor(facility.getStatus());
        statusLabel.setStyle(statusLabel.getStyle() + "; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 25; -fx-effect: dropshadow(gaussian, " + glowColor + ", 25, 0.9, 0, 0), dropshadow(gaussian, rgba(255,255,255,0.8), 15, 0.6, 0, 0), dropshadow(gaussian, rgba(0,0,0,0.8), 8, 0.8, 0, 4), innershadow(gaussian, rgba(255,255,255,0.8), 6, 0, 0, 2), innershadow(gaussian, " + glowColor + ", 3, 0, 0, 1); -fx-translate-x: -6;");

        // Create a container for the status to allow precise positioning
        HBox statusContainer = new HBox();
        statusContainer.setAlignment(Pos.CENTER);
        statusContainer.getChildren().add(statusLabel);

        contentBox.getChildren().addAll(iconLabel, nameLabel, levelLabel, statusContainer);

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

    private String getStatusGlowColor(FacilityStatus status) {
        switch (status) {
            case AVAILABLE: return "#27ae60"; // Green glow
            case BOOKED: return "#e74c3c"; // Red glow
            case MAINTENANCE: return "#3498db"; // Blue glow
            case TEMPORARILY_CLOSED: return "#f1c40f"; // Yellow glow
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
        // Find and update the status label (it's inside the statusContainer which is the last child of contentBox)
        if (contentBox.getChildren().size() > 0) {
            javafx.scene.Node lastNode = contentBox.getChildren().get(contentBox.getChildren().size() - 1);
            if (lastNode instanceof HBox) {
                HBox statusContainer = (HBox) lastNode;
                if (statusContainer.getChildren().size() > 0 && statusContainer.getChildren().get(0) instanceof Label) {
                    Label statusLabel = (Label) statusContainer.getChildren().get(0);
                    statusLabel.setText(getStatusDisplayText(newStatus));
                    // Clear existing style classes and add the new one
                    statusLabel.getStyleClass().clear();
                    statusLabel.getStyleClass().add(getStatusBadgeClass(newStatus));
                    String glowColor = getStatusGlowColor(newStatus);
                    statusLabel.setStyle(statusLabel.getStyle() + "; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 25; -fx-effect: dropshadow(gaussian, " + glowColor + ", 25, 0.9, 0, 0), dropshadow(gaussian, rgba(255,255,255,0.8), 15, 0.6, 0, 0), dropshadow(gaussian, rgba(0,0,0,0.8), 8, 0.8, 0, 4), innershadow(gaussian, rgba(255,255,255,0.8), 6, 0, 0, 2), innershadow(gaussian, " + glowColor + ", 3, 0, 0, 1); -fx-translate-x: -6;");
                }
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

    private String getLevelInfo(String location) {
        if (location == null || location.trim().isEmpty()) {
            return "";
        }

        String lowerLocation = location.toLowerCase();

        // Check for level/floor patterns - return the full location if it contains level/floor
        if (lowerLocation.contains("level") || lowerLocation.contains("floor")) {
            return location.trim();
        }

        // Check for numeric level indicators (like "L1", "1st Floor", etc.)
        if (lowerLocation.matches(".*\\b[lg](\\d+)\\b.*") ||
            lowerLocation.matches(".*\\blevel\\s*(\\d+)\\b.*") ||
            lowerLocation.matches(".*\\bfloor\\s*(\\d+)\\b.*")) {
            return extractLevelNumber(location);
        }

        // If no level info found, return empty string (label will be hidden)
        return "";
    }

    private String extractLevelNumber(String location) {
        // Try to extract level number from common patterns
        String[] patterns = {
            "(?i)level\\s*(\\d+)",
            "(?i)floor\\s*(\\d+)",
            "(?i)[lg](\\d+)",
            "(?i)(\\d+)(?:st|nd|rd|th)\\s*floor"
        };

        for (String pattern : patterns) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(location);
            if (m.find()) {
                String level = m.group(1);
                return "Level " + level;
            }
        }

        return "";
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