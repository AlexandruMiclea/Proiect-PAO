package model.location;

import model.people.Person;

public class Franchise{
    private final String franchiseID;
    private final Restaurant restaurant;
    private Person manager;
    private String location;

    public String getFranchiseID() {
        return franchiseID;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Person getManager() {
        return manager;
    }

    public String getLocation() {
        return location;
    }

    public Franchise(String franchiseID, Restaurant restaurant, Person manager, String location) {
        this.franchiseID = franchiseID;
        this.restaurant = restaurant;
        this.manager = manager;
        this.location = location;
    }

    @Override
    public String toString() {
        return "This franchise belongs to the " + restaurant.getRestaurantName() + " chain of restaurants. It is located at " + location + ". " + manager.toString();
    }
}
