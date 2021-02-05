package ac.sliet.complaintmanagement.Model;

public class ItemModel {
    String itemName;
    double itemQuantity;
    boolean newItem;

    public ItemModel(String itemName, double itemQuantity, boolean newItem) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.newItem = newItem;
    }

    public ItemModel(){

    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(double itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public boolean isNewItem() {
        return newItem;
    }

    public void setNewItem(boolean newItem) {
        this.newItem = newItem;
    }
}
