package people;

import location.Franchise;
import location.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class Cook extends Person {
    public Cook(String firstName, String lastName, String ID, String gender) {
        super(firstName, lastName, ID, gender);
    }

    @Override
    public String toString() {
        String ans = super.toString() + " Cook{" +
                "kitchen=";
        ans += "}";

        return ans;
    }
}
