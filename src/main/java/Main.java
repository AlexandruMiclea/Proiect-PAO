import food.items.Dish;
import food.items.Meal;
import locations.Franchise;
import locations.Manager;
import locations.Restaurant;
import people.Client;
import locations.Director;

public class Main {
    public static void main(String[] args) {
        Client c1 = new Client("Alex","Miclea","5030419440014", "M");
        Client c2 = new Client("Alexandra","Mocklea","4030419440014", "F");
        Client c4 = new Client("Jane","Doe","0000000000000", "F");
        Client c3 = new Client("John","Doe","0000000000000", "M");

        // create franchise *
        // approve franchise opening *
        // place order
        // confirm order -> take order to preparing
        // when order is prepared -> assign driver to deliver
        //

        Restaurant jerrysPizza = new Restaurant("Jerry's Pizza", "Jean", "Mustafa", "54389578423859", "M");
        Restaurant prestoPizza = new Restaurant("Presto Pizza", "Constantin", "Moraru", "54389578423859", "M");
        Franchise jp1 = jerrysPizza.createFranchise("Ferentari 72", "Jean", "Mustafa", "485378935", "M");
        Manager jpManager = jp1.getManager();
        Franchise pp1 = prestoPizza.createFranchise("Ferentari 96", jpManager);
        Director jerrysPizzaDirector = jerrysPizza.getDirector();
        jerrysPizzaDirector.approveFranchiseOpening(jp1);
        jerrysPizzaDirector.approveFranchiseOpening(pp1); // should be an error there is

        System.out.println(jerrysPizza.toString());
        System.out.println(jerrysPizzaDirector.toString());
        System.out.println(jp1.toString());

        jerrysPizza.addMealInCatalogue("src/main/resources/Ingredients_Cheesecake.csv", "desert");
        jerrysPizza.addMealInCatalogue("src/main/resources/Ingredients_Pizza.csv", "dish");
        jerrysPizza.addMealInCatalogue("src/main/resources/Ingredients_Pasta.csv", "dish");
        jerrysPizza.addMealInCatalogue("src/main/resources/Ingredients_Steak.csv", "dish");
        jerrysPizza.addMealInCatalogue("src/main/resources/Ingredients_Stew.csv", "dish");

        System.out.println(jerrysPizza.getMealsInCatalogue());


    }
}