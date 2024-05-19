package model.location;

import model.food.Ingredient;
import model.food.Meal;
import model.people.Person;

import java.util.List;
import java.util.Map;

// TODO make it so that this method can add ingredients, make dishes and deserts of those ingredients and
public class Kitchen {
    private String kitchenID;
    private Franchise franchise;
    private Map<Ingredient, Double> inventory;
    // todo for each cook add an "can cook order" property or smth
    private Map<Person, Boolean> cooks;

    public String getKitchenID() {
        return kitchenID;
    }

    public Franchise getFranchise() {
        return franchise;
    }

    public Map<Ingredient, Double> getInventory() {
        return inventory;
    }

    public Map<Person, Boolean> getCooks() {
        return cooks;
    }

    public Kitchen(String kitchenID, Franchise franchise) {
        this.kitchenID = kitchenID;
        this.franchise = franchise;
    }

    public Boolean canMakeMeal(Meal meal){
        Map<Ingredient, Double> recipe = meal.getIngredientList();
        for (Ingredient ingredient : recipe.keySet()) {
            if (inventory.containsKey(ingredient)){
                return inventory.get(ingredient) >= recipe.get(ingredient);
            } else {
                return false;
            }
        }
        return null;
    }

    public void addIngredientToInventory(Ingredient ingredient, Double quantity) {
        // see if I already have what I need, then just increment
        if (this.inventory.containsKey(ingredient)){
            this.inventory.replace(ingredient, this.inventory.get(ingredient) + quantity);
            System.out.println("Added more of " + ingredient.getIngredientName());
        }
        else { // create new entry
            this.inventory.put(ingredient, quantity);
            System.out.println("Added " + ingredient.getIngredientName());
        }
    }

    public void addCookToKitchen(Person cook, Boolean isBusy) {
        // see if I already have what I need, then just increment
        if (this.cooks.containsKey(cook)){
            this.cooks.replace(cook, isBusy);
        }
        else { // create new entry
            this.cooks.put(cook, isBusy);
        }
    }
}