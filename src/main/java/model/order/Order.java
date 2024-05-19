package model.order;

//import model.location.Franchise;
import model.people.Person;
import model.people.PersonType;

import java.util.Date;

public class Order {
    private final String orderID;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private Person deliveryDriver;
    private final Person client;
    private final Date orderDate;

    // db ctor
    public Order(String orderID, OrderType orderType, OrderStatus orderStatus, Person deliveryDriver, Person client, Date orderDate) {
        this.orderID = orderID;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.deliveryDriver = deliveryDriver;
        this.client = client;
        this.orderDate = orderDate;
    }

    public void assignDeliveryDriver(Person person){
        if (orderType == OrderType.DELIVERY && person.getPersonType() == PersonType.DELIVERY_DRIVER){
            this.deliveryDriver = person;
        } else {
            if (orderType != OrderType.DELIVERY){
                System.out.println("ERROR: assigned delivery driver to wrong order type.");
            }
            if (person.getPersonType() != PersonType.DELIVERY_DRIVER){
                System.out.println("ERROR: assigned wrong type of person to order.");
            }
        }
    }
    @Override
    public String toString() {
        String ans = "Order no: " +  orderID + "/" + orderDate.toString() + " " + orderStatus + ". Type of order: " + orderType + ". Order placed by " + client.toString() +  ".";
        if (deliveryDriver != null && orderType == OrderType.DELIVERY) {
            ans += " Order will be delivered by: " + deliveryDriver.toString() + ".";
        }
        return ans;
    }
}