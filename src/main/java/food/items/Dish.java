package food.items;

import java.util.Set;

// A dish is a finite product that requires multiple ingredients in order to produce.
public class Dish extends Meal{

    static final Double VAT = 0.19;

    public Dish(Double timeToPrepare) {
        super(timeToPrepare);
    }

    public void addIngredient(Product product, Double quantity) {
        this.componentList.put(product, quantity);
    }

    public void showIngredientList() {
        Set<Product> ingredients = componentList.keySet();
        for(Product product : ingredients) {
            System.out.println(product);
        }
    }



    public Double getPrice() {
        double ans = 0.0;
        for (Product productKey : componentList.keySet()){
            Double weight = componentList.get(productKey);
            // casting time
            if (productKey instanceof Ingredient) {
                ans += ((Ingredient) productKey).getPriceWithoutVAT(weight);
            } else if (productKey instanceof Beverage) {
                ans += ((Beverage) productKey).getPriceWithoutVAT(weight);
            }
        }
        return (1 + VAT) * ans;
    }
}