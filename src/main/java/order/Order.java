package order;

import location.Franchise;
import location.Restaurant;

public class Order {
    private static Integer orderCount = 1;
    private final Integer orderID = orderCount;
    public OrderType orderType;
    public OrderStatus orderStatus;
    Restaurant restaurant;
    Franchise franchise;

    public Order(OrderType orderType) {
        this.orderType = orderType;
        this.orderStatus = OrderStatus.RECEIVED;
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
