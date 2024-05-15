package food.items;

import java.util.Set;

public class Desert extends Meal{
    private static final Double VAT = 0.24;

    public Desert(Double timeToPrepare) {
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

    @Override
    public String toString() {
        return super.toString();
    }
}
