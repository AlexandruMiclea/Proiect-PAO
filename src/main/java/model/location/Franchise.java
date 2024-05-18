/*
package model.location;

import model.people.Person;

public class Franchise{
    private final Restaurant restaurant;
    private Person manager;
    private String location;

    public String getLocation() {
        return location;
    }
    public Person getManager() {
        return manager;
    }

    Franchise(Restaurant restaurant, String location, String MgrFName, String MgrLName, String MgrID, String MgrGender) {
        this.restaurant = restaurant;
        this.location = location;
        this.manager = new Manager(MgrFName, MgrLName, MgrID, MgrGender);
    }

    Franchise(Restaurant restaurant, String location, Manager manager) {
        this.restaurant = restaurant;
        this.approvedForOpening = false;
        this.location = location;
        this.kitchen = new Kitchen();
        this.kitchen.setRecipes(restaurant.getMealsInCatalogue());
        this.manager = manager;
    }

    public void setApprovedForOpening(){
        if (!this.approvedForOpening) {
            this.approvedForOpening = true;
        }
    }

    @Override
    public String toString() {
        return "This franchise belongs to the " + restaurant.getRestaurantName() + " chain of restaurants. It is located at " + location + ". " + manager.toString();
    }
}*/
