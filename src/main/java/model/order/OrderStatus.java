package model.order;

public enum OrderStatus {
    RECEIVED("has reached our preparing queue"),
    PREPARING("has been picked up by one of our cooks"),
    WAITING_FOR_PICKUP("is waiting for a driver to pick up your order for delivery"),
    IN_DELIVERY("is being delivered"),
    DONE("is waiting for you to pick it up");

    private final String description;
    private OrderStatus(String value){
        description = value;
    }

    @Override
    public String toString() {
        return description;
    }
}
