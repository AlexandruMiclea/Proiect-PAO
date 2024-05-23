package model.location;

import model.people.Person;
import util.RandomString;

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

    public Franchise(String location, Restaurant restaurant) {
        this.location = location;
        this.franchiseID = RandomString.getRandomString();
        this.restaurant = restaurant;
    }

    public void appointManager(Person manager) {
        this.manager = manager;
    }

    @Override
    public String toString() {
        return "This franchise belongs to the " + restaurant.getRestaurantName() + " chain of restaurants. It is located at " + location + ". " + manager.toString();
    }
}
