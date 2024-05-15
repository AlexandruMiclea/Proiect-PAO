package food.items;

// A base class which represents something - half a kilo of pork, a bottle of juice, etc.
public abstract class Product {
    final Double VAT;

    public String getName() {
        return name;
    }

    final String name;

    Product(String name, Boolean bHasSugar) {
        this.name = name;
        if (bHasSugar) {
            this.VAT = 0.24;
        } else {
            this.VAT = 0.19;
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "VAT=" + VAT +
                ", name='" + name + '\'' +
                '}';
    }
}