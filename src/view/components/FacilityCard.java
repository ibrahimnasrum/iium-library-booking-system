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

public class FacilityCard extends VBox {

    private Facility facility;
    private Runnable onCardClicked;

    public FacilityCard(Facility facility) {
        this.facility = facility;
        setupCard();
    }

    private void setupCard() {
        setPrefSize(200, 250);
        setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");
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
        String statusColor = getStatusColor(facility.getStatus());
        statusLabel.setStyle("-fx-background-color: " + statusColor + "; -fx-text-fill: white; " +
                           "-fx-padding: 2 8 2 8; -fx-background-radius: 10; -fx-font-size: 11px;");

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
        setOnMouseEntered(e -> setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #3498db; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5;"));
        setOnMouseExited(e -> setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;"));

        getChildren().addAll(imageView, nameLabel, idLabel, statusLabel, capacityLabel, locationLabel);
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

    public Facility getFacility() {
        return facility;
    }

    public void setOnCardClicked(Runnable handler) {
        this.onCardClicked = handler;
    }
}