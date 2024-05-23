package model.people;

public enum PersonType {

    CLIENT("Client"),
    ADMIN("Administrator"),
    MANAGER("Franchise Manager"),
    DIRECTOR("Restaurant Director"),
    DELIVERY_DRIVER("Delivery Driver"),
    COOK("Cook");
    private final String description;
    PersonType(String value){
        description = value;
    }

    @Override
    public String toString() {
        return description;
    }
}
