package locations;

import food.Kitchen;

public class Franchise{
    Restaurant restaurant;
    private Boolean approvedForOpening;
    String location;

    public Kitchen getKitchen() {
        return kitchen;
    }

    Kitchen kitchen;

    public Manager getManager() {
        return manager;
    }

    Manager manager;

    @Override
    public String toString() {
        return "Franchise{" +
                "restaurant=" + restaurant.restaurantName +
                ", approvedForOpening=" + approvedForOpening +
                ", location='" + location + '\'' +
                ", kitchen=" + kitchen +
                ", manager=" + manager +
                '}';
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

    void setApprovedForOpening(){
        if (!this.approvedForOpening) {
            this.approvedForOpening = true;
        }
    }
}