package people;

import location.Franchise;
import people.Person;

public class DeliveryDriver extends Person {
    private Franchise deliversFor;
    public DeliveryDriver(String firstName, String lastName, String ID, String gender) {
        super(firstName, lastName, ID, gender);
    }

    public void pickupOrder() {

    }
}
