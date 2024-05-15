package food.items;

// an Item represents something that is sold as a whole -> a bottle of water, eating stuff, etc.
public class Item extends Product{
    private final Double price;

    Item(String name, Double price, Boolean bHasSugar) {
        super(name, bHasSugar);
        this.price = price;
    }

    public Double getPriceWithoutVAT(Double quantity) {
        return (1 - this.VAT) * ((this.price * quantity) / 100);
    }

    @Override
    public String toString() {
        return "Item{" +
                "price=" + price +
                ", VAT=" + VAT +
                ", name='" + name + '\'' +
                '}';
    }
}
