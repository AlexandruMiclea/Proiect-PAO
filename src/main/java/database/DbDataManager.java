package database;

import model.food.Ingredient;
import model.food.Item;
import model.food.Meal;
import model.food.MealType;
import model.location.Franchise;
import model.location.Kitchen;
import model.location.Restaurant;
import model.order.Order;
import model.order.OrderStatus;
import model.order.OrderType;
import model.people.Person;
import model.people.StringComparator;
import model.people.PersonType;

import java.sql.*;
import java.time.Instant;
import java.util.*;
import java.util.Date;

public class DbDataManager {
    private static DbDataManager dataManager;
    private static Connection connection;

    private Map<String, Person> persons;
    private Map<String, Order> orders;
    private Map<String, Ingredient> ingredients;
    private Map<String, Item> items;
    private Map<String, Meal> meals;
    private Map<String, Restaurant> restaurants;
    private Map<String, Franchise> franchises;
    private Map<String, Kitchen> kitchens;

    {
        this.persons = new TreeMap<String, Person>(new StringComparator());
        this.orders = new TreeMap<String, Order>(new StringComparator());
        this.ingredients = new TreeMap<String, Ingredient>(new StringComparator());
        this.items = new TreeMap<String, Item>(new StringComparator());
        this.meals = new TreeMap<String, Meal>(new StringComparator());
        this.restaurants = new TreeMap<String, Restaurant>(new StringComparator());
        this.franchises = new TreeMap<String, Franchise>(new StringComparator());
        this.kitchens = new TreeMap<String, Kitchen>(new StringComparator());
    }

    public Map<String,Person> getPersons() {
        return this.persons;
    }

    public Map<String, Order> getOrders() {
        return orders;
    }
    public Map<String, Ingredient> getIngredients() {
        return ingredients;
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public Map<String, Meal> getMeals() {
        return meals;
    }

    protected DbDataManager(Connection connection) {
        DbDataManager.connection = connection;
        readPersons();
        readOrders();
        readIngredients();
        readItems();
        readMeals();
        readMealHasIngredients();
        readRestaurants();
        readFranchises();
        readKitchens();
        readMealsInRestaurant();
        readCooksInKitchen();
        readIngredientsInKitchen();
    }

    public static DbDataManager getDataManager(Connection connection){
        if (dataManager == null) {
            dataManager = new DbDataManager(connection);
        }
        return dataManager;
    }

    public void readPersons () {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from Person";
            ResultSet rez = stmt.executeQuery(sql);
            while (rez.next()) {
                String personID = rez.getString("personID");
                String username = rez.getString("username");
                Integer hashedPassword = rez.getInt("hashedPassword");
                String firstName = rez.getString("firstName");
                String lastName = rez.getString("lastName");
                String gender = rez.getString("gender");
                PersonType personType = PersonType.valueOf(rez.getString("personType"));
                this.persons.put(personID, new Person(personID, firstName, lastName, gender, username, hashedPassword, personType));
            }
        } catch (SQLException e) {
            System.out.println("Error: cannot get persons from database!");
        }
    }

    public void readOrders () {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from `Order`";
            ResultSet rez = stmt.executeQuery(sql);
            while (rez.next()) {
                String orderID = rez.getString("orderID");
                OrderType orderType = OrderType.valueOf(rez.getString("orderType"));
                OrderStatus orderStatus = OrderStatus.valueOf(rez.getString("orderStatus"));
                String deliveryDriverID = rez.getString("deliveryDriverID");
                String clientID = rez.getString("clientID");
                Date orderDate = rez.getDate("orderDate");

                Person deliveryDriver = persons.get(deliveryDriverID);
                Person client = persons.get(clientID);
                this.orders.put(orderID, new Order(orderID, orderType, orderStatus, deliveryDriver, client, orderDate));
            }
        } catch (SQLException e) {
            System.out.println("Error: cannot get orders from database!");
        }
    }

    public void readIngredients () {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from `Ingredient`";
            ResultSet rez = stmt.executeQuery(sql);
            while (rez.next()) {
                String ingredientID = rez.getString("ingredientID");
                String ingredientName = rez.getString("ingredientName");
                Double pricePerHundred = rez.getDouble("pricePerHundred");
                Double caloriesPerHundred = rez.getDouble("caloriesPerHundred");
                this.ingredients.put(ingredientID, new Ingredient(ingredientID, ingredientName, pricePerHundred, caloriesPerHundred));
            }
        } catch (SQLException e) {
            System.out.println("Error: cannot get ingredients from database!");
        }
    }

    public void readItems () {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from `Item`";
            ResultSet rez = stmt.executeQuery(sql);
            while (rez.next()) {
                String itemID = rez.getString("itemID");
                String itemName = rez.getString("itemName");
                Double price = rez.getDouble("price");
                this.items.put(itemID, new Item(itemID, itemName, price));
            }
        } catch (SQLException e) {
            System.out.println("Error: cannot get items from database!");
        }
    }

    public void readMeals () {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from `Meal`";
            ResultSet rez = stmt.executeQuery(sql);
            while (rez.next()) {
                String mealID = rez.getString("mealID");
                MealType mealType = MealType.valueOf(rez.getString("mealType"));
                String mealName = rez.getString("mealName");
                Double price = rez.getDouble("price");
                Double timeToPrepare = rez.getDouble("timeToPrepare");

                this.meals.put(mealID, new Meal(mealID, mealName, timeToPrepare, mealType, price));
            }
        } catch (SQLException e) {
            System.out.println("Error: cannot get meals from database!");
        }
    }

    public void readMealHasIngredients() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from `Meal_has_Ingredient`";
            ResultSet rez = stmt.executeQuery(sql);
            while (rez.next()) {
                String ingredientID = rez.getString("ingredientID");
                String mealID = rez.getString("mealID");
                Double quantity = rez.getDouble("quantity");

                Meal mealToUpdate = this.meals.get(mealID);
                Ingredient ingredientToAdd = this.ingredients.get(ingredientID);
                mealToUpdate.addIngredient(ingredientToAdd, quantity);
            }
        } catch (SQLException e) {
            System.out.println("Error: cannot get meal ingredients from database!");
        }
    }

    public void readRestaurants() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from `Restaurant`";
            ResultSet rez = stmt.executeQuery(sql);
            while (rez.next()) {
                String restaurantID = rez.getString("restaurantID");
                String restaurantName = rez.getString("name");
                String directorID = rez.getString("directorID");
                Person director = this.persons.get(directorID);

                this.restaurants.put(restaurantID, new Restaurant(restaurantID, restaurantName, director));
            }
        } catch (SQLException e) {
            System.out.println("Error: cannot get restaurants from database!");
        }
    }

    public void readFranchises() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from `Franchise`";
            ResultSet rez = stmt.executeQuery(sql);
            while (rez.next()) {
                String franchiseID = rez.getString("franchiseID");
                String restaurantID = rez.getString("restaurantID");
                String managerID = rez.getString("managerID");
                String location = rez.getString("location");
                Restaurant restaurant = this.restaurants.get(restaurantID);
                Person manager = this.persons.get(managerID);

                this.franchises.put(franchiseID, new Franchise(franchiseID, restaurant, manager, location));
            }
        } catch (SQLException e) {
            System.out.println("Error: cannot get franchises from database!");
        }
    }

    public void readKitchens() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from `Kitchen`";
            ResultSet rez = stmt.executeQuery(sql);
            while (rez.next()) {
                String kitchenID = rez.getString("kitchenID");
                String franchiseID = rez.getString("franchiseID");
                Franchise franchise = this.franchises.get(franchiseID);

                this.kitchens.put(kitchenID, new Kitchen(kitchenID, franchise));
            }
        } catch (SQLException e) {
            System.out.println("Error: cannot get kitchens from database!");
        }
    }

    public void readMealsInRestaurant() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from `Meal_in_Restaurant`";
            ResultSet rez = stmt.executeQuery(sql);
            while (rez.next()) {
                String restaurantID = rez.getString("restaurantID");
                String mealID = rez.getString("mealID");
                Meal meal = this.meals.get(mealID);
                Restaurant restaurant = this.restaurants.get(restaurantID);

                restaurant.addMealInCatalogue(meal);
            }
        } catch (SQLException e) {
            System.out.println("Error: cannot get meals in restaurant from database!");
        }
    }

    public void readIngredientsInKitchen() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from `Ingredient_in_Kitchen`";
            ResultSet rez = stmt.executeQuery(sql);
            while (rez.next()) {
                String ingredientID = rez.getString("ingredientID");
                String kitchenID = rez.getString("kitchenID");
                Double quantity = rez.getDouble("quantity");

                Ingredient ingredient = this.ingredients.get(ingredientID);
                Kitchen kitchen = this.kitchens.get(kitchenID);
                kitchen.addIngredientToInventory(ingredient, quantity);
            }
        } catch (SQLException e) {
            System.out.println("Error: cannot get ingredients in kitchen from database!");
        }
    }

    public void readCooksInKitchen() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "select * from `Ingredient_in_Kitchen`";
            ResultSet rez = stmt.executeQuery(sql);
            while (rez.next()) {
                String cookID = rez.getString("cookID");
                String kitchenID = rez.getString("kitchenID");
                Boolean isBusy = rez.getBoolean("isBusy");
                Person cook = this.persons.get(cookID);
                Kitchen kitchen = this.kitchens.get(kitchenID);

                kitchen.addCookToKitchen(cook, isBusy);
            }
        } catch (SQLException e) {
            System.out.println("Error: cannot get cooks in kitchen from database!");
        }
    }

    public void addPerson (Person person) {
        this.persons.put(person.getPersonID(), person);
        try {
            String sql = "insert into Person values (?,?,?,?,?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, person.getPersonID());
            pstmt.setString(2, person.getUsername());
            pstmt.setInt(3, person.getHashedPassword());
            pstmt.setString(4, person.getFirstName());
            pstmt.setString(5, person.getLastName());
            pstmt.setString(6, person.getGender());
            pstmt.setString(7, person.getPersonType().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot add person in database!");
        }
    }
    // todo test
    public void addOrder (Order order) {
        this.orders.put(order.getOrderID(), order);
        try {
            String sql = "insert into `Order` values (?,?,?,?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, order.getOrderID());
            pstmt.setString(2, order.getOrderType().toString());
            pstmt.setString(3, order.getOrderStatus().toString());
            if (order.getDeliveryDriver() != null){
                pstmt.setString(4, order.getDeliveryDriver().getPersonID());
            } else {
                pstmt.setString(4, null);
            }
            pstmt.setString(5, order.getClient().getPersonID());
            pstmt.setDate(6, new java.sql.Date(System.currentTimeMillis()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get orders from database!");
        }
    }
    // todo test
    public void addIngredient (Ingredient ingredient) {
        this.ingredients.put(ingredient.getIngredientID(), ingredient);
        try {
            String sql = "insert into `Ingredient` values (?,?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, ingredient.getIngredientID());
            pstmt.setString(2, ingredient.getIngredientName());
            pstmt.setDouble(3, ingredient.getPricePerHundred());
            pstmt.setDouble(3, ingredient.getCaloriesPerHundred());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get ingredients from database!");
        }
    }
    // todo test
    public void addItem (Item item) {
        this.items.put(item.getItemID(), item);
        try {
            String sql = "insert into `Item` values (?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, item.getItemID());
            pstmt.setString(2, item.getItemName());
            pstmt.setDouble(3, item.getPrice());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get items from database!");
        }
    }
    // todo test
    public void addMeal(Meal meal) {
        this.meals.put(meal.getMealID(), meal);
        try {
            String sql = "insert into `Meal` values (?,?,?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, meal.getMealID());
            pstmt.setString(2, meal.getMealName());
            pstmt.setDouble(3, meal.getTimeToPrepare());
            pstmt.setString(4, meal.getMealType().toString());
            pstmt.setDouble(5, meal.getTimeToPrepare());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get meals from database!");
        }
    }
    // todo test
    public void addRestaurant(Restaurant restaurant) {
        this.restaurants.put(restaurant.getRestaurantID(), restaurant);
        try {
            String sql = "insert into `Restaurant` values (?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, restaurant.getRestaurantID());
            pstmt.setString(2, restaurant.getRestaurantName());
            pstmt.setString(3, restaurant.getDirector().getPersonID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get restaurants from database!");
        }
    }
    // todo test
    public void addFranchise(Franchise franchise) {
        this.franchises.put(franchise.getFranchiseID(), franchise);
        try {
            String sql = "insert into `Franchise` values (?,?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, franchise.getFranchiseID());
            pstmt.setString(2, franchise.getRestaurant().getRestaurantID());
            pstmt.setString(3, franchise.getManager().getPersonID());
            pstmt.setString(4, franchise.getLocation());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get franchises from database!");
        }
    }
    // todo test
    public void addKitchen(Kitchen kitchen) {
        this.kitchens.put(kitchen.getKitchenID(), kitchen);
        try {
            String sql = "insert into `Kitchen` values (?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, kitchen.getKitchenID());
            pstmt.setString(2, kitchen.getFranchise().getFranchiseID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get kitchens from database!");
        }
    }
    // todo test
    public void addMealInRestaurant(Meal meal, Restaurant restaurant) {
        this.restaurants.get(restaurant.getRestaurantID()).addMealInCatalogue(meal);
        try {
            String sql = "insert into `Meal_in_Restaurant` values (?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, restaurant.getRestaurantID());
            pstmt.setString(2, meal.getMealID());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error: cannot get meals in restaurant from database!");
        }
    }
    // todo test
    public void addIngredientsInKitchen(Ingredient ingredient, Kitchen kitchen, Double quantity) {
        this.kitchens.get(kitchen.getKitchenID()).addIngredientToInventory(ingredient, quantity);
        try {
            String sql = "insert into `Ingredient_in_Kitchen` values (?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, ingredient.getIngredientID());
            pstmt.setString(2, kitchen.getKitchenID());
            pstmt.setDouble(3, quantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get ingredients in kitchen from database!");
        }
    }

    // todo test
    public void addCooksInKitchen(Kitchen kitchen, Person cook, Boolean bIsBusy) {
        this.kitchens.get(kitchen.getKitchenID()).addCookToKitchen(cook, bIsBusy);
        try {
            String sql = "insert into `Cook_in_Kitchen` values (?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, cook.getPersonID());
            pstmt.setString(2, kitchen.getKitchenID());
            pstmt.setBoolean(3, bIsBusy);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get cooks in kitchen from database!");
        }
    }

    // todo test
    public void updatePerson (Person person) {
        this.persons.put(person.getPersonID(), person);
        try {
            String sql = "insert into Person values (?,?,?,?,?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, person.getPersonID());
            pstmt.setString(2, person.getUsername());
            pstmt.setInt(3, person.getHashedPassword());
            pstmt.setString(4, person.getFirstName());
            pstmt.setString(5, person.getLastName());
            pstmt.setString(6, person.getGender());
            pstmt.setString(7, person.getPersonType().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot add person in database!");
        }
    }
    // todo test
    public void updateOrder (Order order) {
        this.orders.put(order.getOrderID(), order);
        try {
            String sql = "insert into `Order` values (?,?,?,?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, order.getOrderID());
            pstmt.setString(2, order.getOrderType().toString());
            pstmt.setString(3, order.getOrderStatus().toString());
            if (order.getDeliveryDriver() != null){
                pstmt.setString(4, order.getDeliveryDriver().getPersonID());
            } else {
                pstmt.setString(4, null);
            }
            pstmt.setString(5, order.getClient().getPersonID());
            pstmt.setDate(6, new java.sql.Date(System.currentTimeMillis()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get orders from database!");
        }
    }
    // todo test
    public void updateIngredient (Ingredient ingredient) {
        this.ingredients.put(ingredient.getIngredientID(), ingredient);
        try {
            String sql = "insert into `Ingredient` values (?,?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, ingredient.getIngredientID());
            pstmt.setString(2, ingredient.getIngredientName());
            pstmt.setDouble(3, ingredient.getPricePerHundred());
            pstmt.setDouble(3, ingredient.getCaloriesPerHundred());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get ingredients from database!");
        }
    }
    // todo test
    public void updateItem (Item item) {
        this.items.put(item.getItemID(), item);
        try {
            String sql = "insert into `Item` values (?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, item.getItemID());
            pstmt.setString(2, item.getItemName());
            pstmt.setDouble(3, item.getPrice());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get items from database!");
        }
    }
    // todo test
    public void updateMeal(Meal meal) {
        this.meals.put(meal.getMealID(), meal);
        try {
            String sql = "insert into `Meal` values (?,?,?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, meal.getMealID());
            pstmt.setString(2, meal.getMealName());
            pstmt.setDouble(3, meal.getTimeToPrepare());
            pstmt.setString(4, meal.getMealType().toString());
            pstmt.setDouble(5, meal.getTimeToPrepare());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get meals from database!");
        }
    }
    // todo test
    public void updateRestaurant(Restaurant restaurant) {
        this.restaurants.put(restaurant.getRestaurantID(), restaurant);
        try {
            String sql = "insert into `Restaurant` values (?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, restaurant.getRestaurantID());
            pstmt.setString(2, restaurant.getRestaurantName());
            pstmt.setString(3, restaurant.getDirector().getPersonID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get restaurants from database!");
        }
    }
    // todo test
    public void updateFranchise(Franchise franchise) {
        this.franchises.put(franchise.getFranchiseID(), franchise);
        try {
            String sql = "insert into `Franchise` values (?,?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, franchise.getFranchiseID());
            pstmt.setString(2, franchise.getRestaurant().getRestaurantID());
            pstmt.setString(3, franchise.getManager().getPersonID());
            pstmt.setString(4, franchise.getLocation());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get franchises from database!");
        }
    }
    // todo test
    public void updateKitchen(Kitchen kitchen) {
        this.kitchens.put(kitchen.getKitchenID(), kitchen);
        try {
            String sql = "insert into `Kitchen` values (?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, kitchen.getKitchenID());
            pstmt.setString(2, kitchen.getFranchise().getFranchiseID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get kitchens from database!");
        }
    }
    // todo test
    public void updateMealInRestaurant(Meal meal, Restaurant restaurant) {
        this.restaurants.get(restaurant.getRestaurantID()).addMealInCatalogue(meal);
        try {
            String sql = "insert into `Meal_in_Restaurant` values (?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, restaurant.getRestaurantID());
            pstmt.setString(2, meal.getMealID());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error: cannot get meals in restaurant from database!");
        }
    }
    // todo test
    public void updateIngredientsInKitchen(Ingredient ingredient, Kitchen kitchen, Double quantity) {
        this.kitchens.get(kitchen.getKitchenID()).addIngredientToInventory(ingredient, quantity);
        try {
            String sql = "insert into `Ingredient_in_Kitchen` values (?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, ingredient.getIngredientID());
            pstmt.setString(2, kitchen.getKitchenID());
            pstmt.setDouble(3, quantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get ingredients in kitchen from database!");
        }
    }

    // todo test
    public void updateCooksInKitchen(Kitchen kitchen, Person cook, Boolean bIsBusy) {
        this.kitchens.get(kitchen.getKitchenID()).addCookToKitchen(cook, bIsBusy);
        try {
            String sql = "insert into `Cook_in_Kitchen` values (?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, cook.getPersonID());
            pstmt.setString(2, kitchen.getKitchenID());
            pstmt.setBoolean(3, bIsBusy);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: cannot get cooks in kitchen from database!");
        }
    }
}