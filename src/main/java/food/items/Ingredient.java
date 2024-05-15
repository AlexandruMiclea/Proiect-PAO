package food.items;

// an ingredient is something used in preparing a dish / a desert. This should be a reference for a type of produce
// the quantity of said produce is determined in the dish recipe list
public class Ingredient extends Product{

    private final Double pricePerHundred;
    private final Integer caloriesPerHundred;

    public Ingredient(String name, Boolean bHasSugar, Double pricePerHundred, Integer caloriesPerHundred) {
        super(name, bHasSugar);
        this.pricePerHundred = pricePerHundred;
        this.caloriesPerHundred = caloriesPerHundred;
    }

    protected Integer getCalories(Double quantity) {
        return (int)((quantity * this.caloriesPerHundred) / 100);
    }

    @Override
    public String toString() {
        return "Nume: " + name +
                ", Calorii/100g: " + caloriesPerHundred +
                ", Pret/100g: " + pricePerHundred +
                ".";
    }

    public Double getPriceWithoutVAT(Double quantity) {
        return (1 - this.VAT) * ((this.pricePerHundred * quantity) / 100);
    }

}
