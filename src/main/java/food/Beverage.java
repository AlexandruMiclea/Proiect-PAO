package food;

final class Beverage extends Item{
    private Double size;
    private Boolean bHasAlcoohol;

    public Beverage(String name, Double price, Boolean bHasSugar, Double size, Boolean bHasAlcoohol) {
        super(name, price, bHasSugar);
        this.size = size;
        this.bHasAlcoohol = bHasAlcoohol;
    }

    @Override
    public String toString() {
        return "Beverage{" +
                "size=" + size +
                ", bHasAlcoohol=" + bHasAlcoohol +
                ", VAT=" + VAT +
                ", name='" + name + '\'' +
                '}';
    }
}