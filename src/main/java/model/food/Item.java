package model.food;

import java.util.Map;

// an Item represents something that is sold as a whole -> a bottle of water, eating stuff, etc.
public class Item {
    private final String itemName;
    private final Double price;

    Item(String itemName, Double price) {
        this.itemName = itemName;
        this.price = price;
    }

    @Override
    public String toString() {
        return itemName + ": " + price + "$";
    }
}
