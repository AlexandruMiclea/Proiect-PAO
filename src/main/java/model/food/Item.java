package model.food;

import java.util.Map;

// an Item represents something that is sold as a whole -> a bottle of water, eating stuff, etc.
public class Item {
    private final String itemID;
    private final String itemName;
    private final Double price;

    public Item(String itemID, String itemName, Double price) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.price = price;
    }

    @Override
    public String toString() {
        return itemName + ": " + price + "$";
    }
}
