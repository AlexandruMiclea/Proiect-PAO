package locations;

public class Order {
    static Integer orderID = 10000;
    enum Type {
        DINE_IN,
        FOR_TAKEAWAY,
        DELIVERY
    }
    enum Status {
        RECEIVED,
        CONFIRMED,
        PREPARING,
        RECEIVED_FOR_DELIVERY,
        IN_DELIVERY
    }

    Type orderType;
    Status orderStatus;

    Restaurant restaurant;
    Franchise franchise; // for now this will be given to the first franchise
    // TODO make a design pattern that assigns an order to a free location / nearest location etc.

    public Order(Type orderType, Status orderStatus) {
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.orderID += 1;
    }
}
