package model.food;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// a meal is a finite product, I can purchase this from a restaurant that makes it
// somehow...

// TODO return particular get and set methods to show ingredients for those items, when making restaurant menus
public class Meal {
    private final String mealID;
    private final String mealName;
    private final Double timeToPrepare;
    private final MealType mealType;
    private final Double price;
    Map<Ingredient, Double> ingredientList;

    public String getMealID() {
        return mealID;
    }

    public String getMealName() {
        return mealName;
    }

    public Double getTimeToPrepare() {
        return timeToPrepare;
    }

    public MealType getMealType() {
        return mealType;
    }

    public Double getPrice() {
        return price;
    }

    public Meal(String mealID, String mealName, Double timeToPrepare, MealType mealType, Double price) {
        this.mealID = mealID;
        this.mealName = mealName;
        this.timeToPrepare = timeToPrepare;
        this.mealType = mealType;
        this.price = price;
        this.ingredientList = new HashMap<>(); // TODO implement hashable for ingredient
    }

    public void addIngredient(Ingredient ingredient, Double quantity) {
        this.ingredientList.put(ingredient, quantity);
    }

    public Map<Ingredient, Double> getIngredientList() {
        return ingredientList;
    }


    @Override
    public String toString() {
        String ans = mealType + " " + mealName + ", prepare time: " + timeToPrepare + ", price: " + price + ".\n";
        ans += "Ingredient list:\n";
        for (Ingredient ingredient : ingredientList.keySet()){
            ans += "* " + ingredient.getIngredientName() + "\n";
        }
        ans = ans.substring(0, ans.length() - 1); // remove last '\n'
        return ans;
    }
}