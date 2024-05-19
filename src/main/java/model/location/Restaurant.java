package model.location;

import model.food.*;
import model.people.Person;

import java.io.*;
import java.util.*;

public class Restaurant {
    private final String restaurantID;
    private final String restaurantName;
    private Map<Ingredient, Double> availableItems; // Item in kitchen, quantity
    private List<Meal> mealsInCatalogue;
    Person director;


    public Restaurant(String restaurantName, String directorFName, String directorLName, String directorID, String directorGender) {
        this.restaurantName = restaurantName;
        this.director = new Director(directorFName, directorLName, directorID, directorGender);
        this.director.addRestaurantInJurisdiction(this);
        this.franchiseList = new ArrayList<Franchise>();
        this.availableItems = new HashMap<Product, Double>();
        this.mealsInCatalogue = new ArrayList<Meal>();
        //this.orderList = new OrderList();
    }

    public Restaurant(String restaurantName, Director director) {
        this.restaurantName = restaurantName;
        this.director = director;
        this.franchiseList = new ArrayList<Franchise>();
        this.availableItems = new HashMap<Product, Double>();
        this.mealsInCatalogue = new ArrayList<Meal>();
        //this.orderList = new OrderList();
    }

    public Franchise createFranchise(String location, String MgrFName, String MgrLName, String MgrID, String MgrGender) {
        Franchise newFranchise = new Franchise(this, location, MgrFName, MgrLName, MgrID, MgrGender);
        this.franchiseList.add(newFranchise);
        return newFranchise;
    }

    public Franchise createFranchise(String location, Manager manager) {
        Franchise newFranchise = new Franchise(this, location, manager);
        this.franchiseList.add(newFranchise);
        return newFranchise;
    }

    // Interactive method
    public void addMealInCatalogue(String CSVPath, String classType) {
        Map<Product, Double> readCompontents = new HashMap<Product, Double>();
        String[] params;
        // make the ingredient list and give it to the dish/desert
        try {
            File CSVFile = new File(CSVPath);
            Scanner scan = new Scanner(CSVFile);
            while (scan.hasNextLine()) {
                params = scan.nextLine().split(",");
                readCompontents.put(new Ingredient(params[0], Objects.equals(params[1], "true"), Double.parseDouble(params[2]), Integer.parseInt(params[3])), Double.parseDouble(params[4]));
            }

            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println("Bad CSV path given!");
            return;
        }

        // for each line make an ingredient and add it here
        switch (classType.toLowerCase()) {
            case "dish":
                Dish dish = new Dish(0.20);
                dish.setComponentList(readCompontents);
                this.mealsInCatalogue.add(dish);
                System.out.println(readCompontents);
                break;
            case "desert":
                Desert desert = new Desert(0.20);
                desert.setComponentList(readCompontents);
                this.mealsInCatalogue.add(desert);
                break;
            default:
                System.out.println("Wrong input! Try again!");
                break;
        }
    }

    public void setMealsInCatalogue(List<Meal> mealsInCatalogue) {
        this.mealsInCatalogue = mealsInCatalogue;
    }

    public void setMealsForFranchises() {
        for(Franchise franchise : franchiseList) {
            franchise.getKitchen().setRecipes(this.mealsInCatalogue);
        }
    }

    public List<Meal> getMealsInCatalogue() {
        return mealsInCatalogue;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "restaurantName='" + restaurantName + '\'' +
                ", availableItems=" + availableItems +
                ", franchiseList=" + franchiseList +
                ", mealsInCatalogue=" + mealsInCatalogue +
                ", director=" + director +
                '}';
    }
}
