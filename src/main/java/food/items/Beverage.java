package food.items;

final class Beverage extends Item{
    Double size;
    Boolean bHasAlcoohol;

    public Beverage(String name, Double price, Boolean bHasSugar, Double size, Boolean bHasAlcoohol) {
        super(name, price, bHasSugar);
        this.size = size;
        this.bHasAlcoohol = bHasAlcoohol;
    }
}