package location;

import people.Manager;

public class Franchise{
    private final Restaurant restaurant;
    private final Kitchen kitchen;
    private final Manager manager;
    private Boolean approvedForOpening;
    private String location;

    public String getLocation() {
        return location;
    }

    public Kitchen getKitchen() {
        return kitchen;
    }

    public Manager getManager() {
        return manager;
    }

    Franchise(Restaurant restaurant, String location, String MgrFName, String MgrLName, String MgrID, String MgrGender) {
        this.restaurant = restaurant;
        this.approvedForOpening = false;
        this.location = location;
        this.kitchen = new Kitchen();
        this.kitchen.setRecipes(restaurant.getMealsInCatalogue());
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
        return "Franchise{" +
                "restaurant=" + restaurant.getRestaurantName() +
                ", approvedForOpening=" + approvedForOpening +
                ", location='" + location + '\'' +
                ", kitchen=" + kitchen +
                ", manager=" + manager +
                '}';
    }
}