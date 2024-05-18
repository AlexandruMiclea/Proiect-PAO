package model.food;

import java.util.HashMap;
import java.util.Map;

// a meal is a finite product, I can purchase this from a restaurant that makes it
// somehow...

// TODO return particular get and set methods to show ingredients for those items, when making restaurant menus
public class Meal {
    private final Double timeToPrepare;
    private final MealType mealType;
    private final String mealName;
    private final Double price;
    Map<Ingredient, Double> ingredientList;

    public Meal(String mealName, Double timeToPrepare, MealType mealType, Double price) {
        this.mealName = mealName;
        this.timeToPrepare = timeToPrepare;
        this.mealType = mealType;
        this.price = price;
        this.ingredientList = new HashMap<>(); // TODO implement hashable for ingredient
    }

    public Map<Ingredient, Double> getItemList() {
        return ingredientList;
    }
}