package order;

import location.Franchise;
import location.Restaurant;

public class Order {
    private static Integer orderCount = 1;
    private final Integer orderID = orderCount;

    public OrderType orderType;
    public OrderStatus orderStatus;

    Restaurant restaurant;
    Franchise franchise; // for now this will be given to the first franchise
    // TODO make a design pattern that assigns an order to a free location / nearest location etc.

    public Order(OrderType orderType, OrderStatus orderStatus) {
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        orderCount += 1;
    }

    public Integer getOrderID() {
        return orderID;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", orderType=" + orderType +
                ", orderStatus=" + orderStatus +
                ", restaurant=" + restaurant.getRestaurantName() +
                ", franchise=" + franchise.getLocation() +
                '}';
    }
}
