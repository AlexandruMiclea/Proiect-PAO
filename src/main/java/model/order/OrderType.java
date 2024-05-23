package model.order;

public enum OrderType {
    DINE_IN("dine in"),
    TAKEAWAY("takeaway"),
    DELIVERY("delivery");
    private final String description;
    OrderType(String value){
        description = value;
    }

    @Override
    public String toString() {
        return description;
    }
}
