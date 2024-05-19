/*
package model.location;

import model.food.Ingredient;
import model.food.Meal;
import model.location.Franchise;

import java.util.List;
import java.util.Map;

// TODO make it so that this method can add ingredients, make dishes and deserts of those ingredients and
public class Kitchen {
    private String kitchenID;
    private Franchise franchise;
    // inventory list
    private Map<Ingredient, Double> listOf; // items available in the kitchen
    private List<Cook> cooks;

    public Boolean canMakeMeal(Meal meal){
        // first check if it is in the list of items to make
        if (!mealsToMake.contains(meal)){
            //System.out.println("");
            return false;
        }
        Map<Product, Double> recipe = meal.getItemList();
        for (Product product : recipe.keySet()) {
            if (availableItems.containsKey(product)){
                return availableItems.get(product) >= recipe.get(product);
            } else {
                return false;
            }
        }
        // TODO how do I get here?
        return null;
    }

     public void addItemToInventory(Product product, Double quantity) {
        // see if I already have what I need, then just increment
        if (this.availableItems.containsKey(product)){
            this.availableItems.replace(product, this.availableItems.get(product) + quantity);
            System.out.println("Added more of " + product.getName());
        }
        else { // create new entry
            this.availableItems.put(product, quantity);
            System.out.println("Added " + product.getName());
        }
    }
    public void setRecipes(List<Meal> meals) {
        this.mealsToMake = meals;
    }
}
*/
