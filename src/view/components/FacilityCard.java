package view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import model.Facility;
import model.enums.FacilityStatus;
import model.enums.ReservationPrivilege;

public class FacilityCard extends VBox {

    private Facility facility;
    private Runnable onCardClicked;

    public FacilityCard(Facility facility) {
        this.facility = facility;
        setupCard();
    }

    private void setupCard() {
        setPrefSize(200, 250);
        getStyleClass().add("card");
        setSpacing(10);
        setPadding(new Insets(15));
        setAlignment(Pos.TOP_CENTER);

        // Image placeholder (would load actual image in real implementation)
        ImageView imageView = new ImageView();
        try {
            // Try to load image from facility's imagePath
            if (facility.getImagePath() != null && !facility.getImagePath().isEmpty()) {
                Image image = new Image(facility.getImagePath(), 150, 100, true, true);
                imageView.setImage(image);
            } else {
                // Default placeholder
                Image placeholder = new Image("https://via.placeholder.com/150x100/3498db/ffffff?text=" +
                    facility.getType().toString().charAt(0), 150, 100, false, true);
                imageView.setImage(placeholder);
            }
        } catch (Exception e) {
            // Fallback placeholder
            Image placeholder = new Image("https://via.placeholder.com/150x100/95a5a6/ffffff?text=No+Image",
                150, 100, false, true);
            imageView.setImage(placeholder);
        }
        imageView.setFitWidth(150);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        // Facility name
        Label nameLabel = new Label(facility.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(180);

        // Facility ID
        Label idLabel = new Label(facility.getId());
        idLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");

        // Status badge
        Label statusLabel = new Label(facility.getStatus().toString());
        statusLabel.getStyleClass().add(getStatusBadgeClass(facility.getStatus()));

        // Privilege badge
        Label privilegeLabel = new Label(getPrivilegeDisplayText(facility.getPrivilege()));
        privilegeLabel.getStyleClass().add(getPrivilegeBadgeClass(facility.getPrivilege()));

        // Badges container
        HBox badgesContainer = new HBox(8);
        badgesContainer.getChildren().addAll(statusLabel, privilegeLabel);
        badgesContainer.setAlignment(Pos.CENTER);

        // Capacity and location
        Label capacityLabel = new Label("Capacity: " + facility.getCapacity());
        capacityLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        Label locationLabel = new Label(facility.getLocation());
        locationLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        locationLabel.setWrapText(true);
        locationLabel.setMaxWidth(180);

        // Click handler
        setOnMouseClicked(e -> {
            if (onCardClicked != null) {
                onCardClicked.run();
            }
        });

        // Hover effects
        setOnMouseEntered(e -> getStyleClass().add("card-hover"));
        setOnMouseExited(e -> getStyleClass().remove("card-hover"));

        getChildren().addAll(imageView, nameLabel, idLabel, badgesContainer, capacityLabel, locationLabel);
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

    private String getPrivilegeDisplayText(ReservationPrivilege privilege) {
        switch (privilege) {
            case OPEN: return "OPEN";
            case STUDENT_ONLY: return "STUDENT";
            case STAFF_ONLY: return "STAFF";
            case POSTGRADUATE_ONLY: return "POSTGRAD";
            case BOOK_VENDORS_ONLY: return "VENDOR";
            case LIBRARY_USE_ONLY: return "LIBRARY";
            case SPECIAL_NEEDS_ONLY: return "SPECIAL";
            default: return "OPEN";
        }
    }

    public Facility getFacility() {
        return facility;
    }

    public void setOnCardClicked(Runnable handler) {
        this.onCardClicked = handler;
    }
}