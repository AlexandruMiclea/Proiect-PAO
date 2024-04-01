package food.items;

import java.util.HashMap;

// a meal is a finite product, I can purchase this from a restaurant that makes it
// somehow...

// TODO return particular get and set methods to show ingredients for those items, when making restaurant menus
public abstract class Meal {
    final Double timeToPrepare;

    @Override
    public String toString() {
        return "Meal{" +
                "timeToPrepare=" + timeToPrepare +
                ", componentList=" + componentList +
                '}';
    }

    public void setComponentList(HashMap<Product, Double> componentList) {
        this.componentList = componentList;
    }

    HashMap<Product, Double> componentList; // a K-V pair that illustrates that V much of K is in my meal

    Meal(Double timeToPrepare) {
        this.timeToPrepare = timeToPrepare;
    }

    public HashMap<Product, Double> getItemList() {
        return componentList;
    }
}