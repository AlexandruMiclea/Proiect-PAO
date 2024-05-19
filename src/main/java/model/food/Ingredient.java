package model.food;

// an ingredient is something used in preparing a dish / a desert. This should be a reference for a type of produce
// the quantity of said produce is determined in the dish recipe list
public class Ingredient {
    private final String ingredientID;
    private final String ingredientName;
    private Double pricePerHundred;
    private Double caloriesPerHundred;

    public String getIngredientName() {
        return ingredientName;
    }

    // DB
    public Ingredient(String ingredientID, String ingredientName, Double pricePerHundred, Double caloriesPerHundred) {
        this.ingredientID = ingredientID;
        this.ingredientName = ingredientName;
        this.pricePerHundred = pricePerHundred;
        this.caloriesPerHundred = caloriesPerHundred;
    }

    protected Integer getCalories(Double quantity) {
        return (int)((quantity * this.caloriesPerHundred) / 100);
    }

    @Override
    public String toString() {
        return "Nume: " + ingredientName +
                ", Calorii/100g: " + caloriesPerHundred +
                ", Pret/100g: " + pricePerHundred +
                ".";
    }
}