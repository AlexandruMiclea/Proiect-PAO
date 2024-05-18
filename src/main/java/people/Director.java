package people;

import location.Franchise;
import location.Restaurant;
import java.util.List;
import java.util.ArrayList;

// A restaurant has a director who can approve
// if a new franchise is created
public class Director extends Person {
    private List<Restaurant> restaurantsInjurisdiction;
    public Director(String firstName, String lastName, String ID, String gender) {
        super(firstName, lastName, ID, gender);
        this.restaurantsInjurisdiction = new ArrayList<>();
    }
    public List<Restaurant> getRestaurantsInjurisdiction() {
        return restaurantsInjurisdiction;
    }
    public void addRestaurantInJurisdiction(Restaurant restaurant) {
        this.restaurantsInjurisdiction.add(restaurant);
    }

    public void approveFranchiseOpening(Franchise franchise) {
        // check if franchise is in my jurisdiction
        for (Restaurant restaurant : restaurantsInjurisdiction) {
            if(restaurant.getFranchiseList().contains(franchise)) {
                // I am the director of this franchise, approve opening!
                franchise.setApprovedForOpening();
                return;
            }
        }
        System.out.println("The franchise given as a parameter is not under this director's administration!");
    }

    @Override
    public String toString() {
        String ans = super.toString() + " Director{" +
                "restaurantsInjurisdiction=";

        for (Restaurant restaurant : restaurantsInjurisdiction) {
            ans += restaurant.getRestaurantName();
        }
        ans += "}";

        return ans;
    }
}