package model.food;

public enum MealType {
    BEVERAGE("Beverage"),
    DESERT("Desert"),
    DISH("Dish");    private final String description;
    private MealType(String value){
        description = value;
    }

    @Override
    public String toString() {
        return description;
    }
}