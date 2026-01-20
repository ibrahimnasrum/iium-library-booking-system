package model;

public class Equipment {
    private String name;
    private int quantity;
    private String description;

    public Equipment(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
        this.description = "";
    }

    public Equipment(String name, int quantity, String description) {
        this.name = name;
        this.quantity = quantity;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return quantity > 1 ? name + " (x" + quantity + ")" : name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Equipment equipment = (Equipment) obj;
        return name.equals(equipment.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}