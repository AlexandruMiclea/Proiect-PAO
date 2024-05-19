package model.location;

import model.food.*;
import model.people.Person;

import java.io.*;
import java.util.*;

public class Restaurant {
    private final String restaurantID;
    private final String restaurantName;
    private List<Meal> mealsInCatalogue;
    // todo add order list here for managing
    Person director;

    public String getRestaurantID() {
        return restaurantID;
    }

    public List<Meal> getMealsInCatalogue() {
        return mealsInCatalogue;
    }

    public Person getDirector() {
        return director;
    }

    {
        this.mealsInCatalogue = new ArrayList<>();
    }
    public Restaurant(String restaurantID, String restaurantName, Person director) {
        this.restaurantID = restaurantID;
        this.restaurantName = restaurantName;
        this.director = director;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void addMealInCatalogue(Meal meal) {
        this.mealsInCatalogue.add(meal);
    }

    public void removeMealFromCatalogue(Meal meal) {
        this.mealsInCatalogue.remove(meal);
    }

    @Override
    public String toString() {
        String ans = "Restaurant " + restaurantName + ", " + director.toString() + ", serves the following meals:\n";
        for (Meal meal : mealsInCatalogue) {
            ans += meal.toString() + '\n';
        }
        ans = ans.substring(0, ans.length() - 1);
        return ans;
    }
}
