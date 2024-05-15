package food.items;

import java.util.Map;

// a meal is a finite product, I can purchase this from a restaurant that makes it
// somehow...

// TODO return particular get and set methods to show ingredients for those items, when making restaurant menus
public abstract class Meal {
    final Double timeToPrepare;

    public void setComponentList(Map<Product, Double> componentList) {
        this.componentList = componentList;
    }

    Map<Product, Double> componentList; // a K-V pair that illustrates that V much of K is in my meal

    Meal(Double timeToPrepare) {
        this.timeToPrepare = timeToPrepare;
    }

    public Map<Product, Double> getItemList() {
        return componentList;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "timeToPrepare=" + timeToPrepare +
                ", componentList=" + componentList +
                '}';
    }
}