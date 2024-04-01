package people;

import food.items.Product;
import locations.Franchise;
import locations.Restaurant;
import food.items.Meal;

// a client is a person who places an order - be it online, in a franchise - dine-in or takeaway
public class Client extends Person {
    public Client(String firstName, String lastName, String ID, String gender) {
        super(firstName, lastName, ID, gender);
    }

    public void OrderMeal(Restaurant restaurant, Meal meal){

        //

        // see if restaurant makes meal
        if (restaurant.getMealsInCatalogue().contains(meal)){
            // find a franchise that can make this meal
            for (Franchise franchise : restaurant.getFranchiseList()) {
                for(Product product : meal.getItemList().keySet()) {
                    if (franchise.getKitchen().canMakeMeal(meal)) {
                        System.out.println("Order placed, franchise " + franchise.toString() + " has picked up order!");
                        return;
                    }
                }
            }
        } else {
            System.out.println("The given restaurant does not make this meal!");
        }
    }
}
