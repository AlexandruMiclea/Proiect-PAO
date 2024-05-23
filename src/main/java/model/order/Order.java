package model.order;

//import model.location.Franchise;
import model.location.Restaurant;
import model.people.Person;
import model.people.PersonType;
import util.RandomString;

import java.time.Instant;
import java.util.Date;

public class Order {
    private final String orderID;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private Person deliveryDriver;
    private final Person client;
    private final Restaurant restaurant;
    private final Date orderDate;

    public String getOrderID() {
        return orderID;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Person getDeliveryDriver() {
        return deliveryDriver;
    }

    public Person getClient() {
        return client;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    // db ctor
    public Order(String orderID, OrderType orderType, OrderStatus orderStatus, Restaurant restaurant, Person deliveryDriver, Person client, Date orderDate) {
        this.orderID = orderID;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.restaurant = restaurant;
        this.deliveryDriver = deliveryDriver;
        this.client = client;
        this.orderDate = orderDate;
    }

    // placeOrder ctor
    public Order(OrderType orderType, Restaurant restaurant, Person client) {
        this.orderID = RandomString.getRandomString();
        this.orderType = orderType;
        this.orderStatus = OrderStatus.RECEIVED;
        this.restaurant = restaurant;
        this.client = client;
        this.orderDate = Date.from(Instant.now());
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