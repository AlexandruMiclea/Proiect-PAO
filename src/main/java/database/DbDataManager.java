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

// TODO all enum inserations in DB should be by .name(), not .toString()!
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

    private Map<String, String> personIDToUsername;

    {
        this.persons = new TreeMap<String, Person>(new StringComparator());
        this.orders = new TreeMap<String, Order>(new StringComparator());
        this.ingredients = new TreeMap<String, Ingredient>(new StringComparator());
        this.items = new TreeMap<String, Item>(new StringComparator());
        this.meals = new TreeMap<String, Meal>(new StringComparator());
        this.restaurants = new TreeMap<String, Restaurant>(new StringComparator());
        this.franchises = new TreeMap<String, Franchise>(new StringComparator());
        this.kitchens = new TreeMap<String, Kitchen>(new StringComparator());
        this.personIDToUsername = new TreeMap<>(new StringComparator());
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

    public Map<String, Restaurant> getRestaurants() {
        return restaurants;
    }

    public Map<String, Franchise> getFranchises() {
        return franchises;
    }

    public Map<String, Kitchen> getKitchens() {
        return kitchens;
    }

    private DbDataManager(Connection connection) {
        DbDataManager.connection = connection;
        readPersons();
        readRestaurants();
        readOrders();
        readIngredients();
        readItems();
        readMeals();
        readMealHasIngredients();
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

    // TODO map person id to username
    public void readPersons () {
        if (DbDataManager.connection != null){
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
                    this.personIDToUsername.put(personID, username);
                    this.persons.put(username, new Person(personID, firstName, lastName, gender, username, hashedPassword, personType));
                }
            } catch (SQLException e) {
                System.out.println("Error: cannot get persons from database!");
            }
        }
    }

    public void readOrders () {
        if (DbDataManager.connection != null) {
            try {
                Statement stmt = connection.createStatement();
                String sql = "select * from `Order`";
                ResultSet rez = stmt.executeQuery(sql);
                while (rez.next()) {
                    String orderID = rez.getString("orderID");
                    OrderType orderType = OrderType.valueOf(rez.getString("orderType"));
                    OrderStatus orderStatus = OrderStatus.valueOf(rez.getString("orderStatus"));
                    String restaurantID = rez.getString("restaurantID");
                    String deliveryDriverID = rez.getString("deliveryDriverID");
                    String clientID = rez.getString("clientID");
                    Date orderDate = rez.getDate("orderDate");

                    Person deliveryDriver;
                    try {
                        deliveryDriver = persons.get(this.personIDToUsername.get(deliveryDriverID));
                    } catch (NullPointerException e) {
                        deliveryDriver = null;
                    }

                    Person client = persons.get(this.personIDToUsername.get(clientID));
                    Restaurant restaurant = restaurants.get(restaurantID);
                    this.orders.put(orderID, new Order(orderID, orderType, orderStatus, restaurant, deliveryDriver, client, orderDate));
                }
            } catch (SQLException e) {
                System.out.println("Error: cannot get orders from database!");
            }
        }
    }

    public void readIngredients () {
        if (DbDataManager.connection != null) {
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
    }

    public void readItems () {
        if (DbDataManager.connection != null) {
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
    }

    public void readMeals () {
        if (DbDataManager.connection != null) {
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
    }

    public void readMealHasIngredients() {
        if (DbDataManager.connection != null) {
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
    }

    public void readRestaurants() {
        if (DbDataManager.connection != null) {
            try {
                Statement stmt = connection.createStatement();
                String sql = "select * from `Restaurant`";
                ResultSet rez = stmt.executeQuery(sql);
                while (rez.next()) {
                    String restaurantID = rez.getString("restaurantID");
                    String restaurantName = rez.getString("name");
                    String directorID = rez.getString("directorID");
                    Person director = this.persons.get(this.personIDToUsername.get(directorID));

                    this.restaurants.put(restaurantID, new Restaurant(restaurantID, restaurantName, director));
                }
            } catch (SQLException e) {
                System.out.println("Error: cannot get restaurants from database!");
            }
        }
    }

    public void readFranchises() {
        if (DbDataManager.connection != null) {
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
                    Person manager;try {
                        manager = this.persons.get(this.personIDToUsername.get(managerID));
                    } catch (NullPointerException e) {
                        manager = null;
                    }

                    this.franchises.put(franchiseID, new Franchise(franchiseID, restaurant, manager, location));
                }
            } catch (SQLException e) {
                System.out.println("Error: cannot get franchises from database!");
            }
        }
    }

    public void readKitchens() {
        if (DbDataManager.connection != null) {
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
    }

    public void readMealsInRestaurant() {
        if (DbDataManager.connection != null) {
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
    }

    public void readIngredientsInKitchen() {
        if (DbDataManager.connection != null) {
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
    }

    public void readCooksInKitchen() {
        if (DbDataManager.connection != null) {
            try {
                Statement stmt = connection.createStatement();
                String sql = "select * from `Ingredient_in_Kitchen`";
                ResultSet rez = stmt.executeQuery(sql);
                while (rez.next()) {
                    String cookID = rez.getString("cookID");
                    String kitchenID = rez.getString("kitchenID");
                    Boolean isBusy = rez.getBoolean("isBusy");
                    Person cook = this.persons.get(this.personIDToUsername.get(cookID));
                    Kitchen kitchen = this.kitchens.get(kitchenID);

                    kitchen.addCookToKitchen(cook, isBusy);
                }
            } catch (SQLException e) {
                System.out.println("Error: cannot get cooks in kitchen from database!");
            }
        }
    }

    public void addPerson (Person person) {
        this.persons.put(person.getUsername(), person);
        if (DbDataManager.connection != null) {
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
    }

    public void addOrder (Order order) {
        this.orders.put(order.getOrderID(), order);
        if (DbDataManager.connection != null) {
            try {
                String sql = "insert into `Order` values (?,?,?,?,?,?,?);";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, order.getOrderID());
                pstmt.setString(2, order.getOrderType().name());
                pstmt.setString(3, order.getOrderStatus().name());
                if (order.getDeliveryDriver() != null) {
                    pstmt.setString(4, order.getDeliveryDriver().getPersonID());
                } else {
                    pstmt.setString(4, null);
                }
                pstmt.setString(5, order.getRestaurant().getRestaurantID());
                pstmt.setString(6, order.getClient().getPersonID());
                pstmt.setDate(7, new java.sql.Date(System.currentTimeMillis()));
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot add order in database!");
            }
        }
    }
    // todo test
    public void addIngredient (Ingredient ingredient) {
        this.ingredients.put(ingredient.getIngredientID(), ingredient);
        if (DbDataManager.connection != null) {
            try {
                String sql = "insert into `Ingredient` values (?,?,?,?);";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, ingredient.getIngredientID());
                pstmt.setString(2, ingredient.getIngredientName());
                pstmt.setDouble(3, ingredient.getPricePerHundred());
                pstmt.setDouble(4, ingredient.getCaloriesPerHundred());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot add ingredient in database!");
            }
        }
    }
    // todo test
    public void addItem (Item item) {
        this.items.put(item.getItemID(), item);
        if (DbDataManager.connection != null) {
            try {
                String sql = "insert into `Item` values (?,?,?);";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, item.getItemID());
                pstmt.setString(2, item.getItemName());
                pstmt.setDouble(3, item.getPrice());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot add item in database!");
            }
        }
    }
    // todo test
    public void addMeal(Meal meal) {
        this.meals.put(meal.getMealID(), meal);
        if (DbDataManager.connection != null) {
            try {
                String sql = "insert into `Meal` values (?,?,?,?,?);";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, meal.getMealID());
                pstmt.setString(2, meal.getMealType().toString());
                pstmt.setString(3, meal.getMealName());
                pstmt.setDouble(4, meal.getPrice());
                pstmt.setDouble(5, meal.getTimeToPrepare());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot add meal in database!");
            }
        }
    }
    // todo test
    public void addRestaurant(Restaurant restaurant) {
        this.restaurants.put(restaurant.getRestaurantID(), restaurant);
        if (DbDataManager.connection != null) {
            try {
                String sql = "insert into `Restaurant` values (?,?,?);";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, restaurant.getRestaurantID());
                pstmt.setString(2, restaurant.getRestaurantName());
                pstmt.setString(3, restaurant.getDirector().getPersonID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot add restaurant in database!");
            }
        }
    }
    // todo test
    public void addFranchise(Franchise franchise) {
        this.franchises.put(franchise.getFranchiseID(), franchise);
        if (DbDataManager.connection != null) {
            try {
                String sql = "insert into `Franchise` values (?,?,?,?);";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, franchise.getFranchiseID());
                pstmt.setString(2, franchise.getRestaurant().getRestaurantID());
                try {
                    String id = franchise.getManager().getPersonID();
                    pstmt.setString(3, id);
                } catch (NullPointerException e) {
                    pstmt.setNull(3, Types.VARCHAR);
                }
                pstmt.setString(4, franchise.getLocation());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot add franchise in database!");
            }
        }
    }
    // todo test
    public void addKitchen(Kitchen kitchen) {
        this.kitchens.put(kitchen.getKitchenID(), kitchen);
        if (DbDataManager.connection != null) {
            try {
                String sql = "insert into `Kitchen` values (?,?);";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, kitchen.getKitchenID());
                pstmt.setString(2, kitchen.getFranchise().getFranchiseID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot add kitchen in database!");
            }
        }
    }
    // todo test
    public void addMealInRestaurant(Meal meal, Restaurant restaurant) {
        this.restaurants.get(restaurant.getRestaurantID()).addMealInCatalogue(meal);
        if (DbDataManager.connection != null) {
            try {
                String sql = "insert into `Meal_in_Restaurant` values (?,?);";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, restaurant.getRestaurantID());
                pstmt.setString(2, meal.getMealID());
                pstmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println("Error: cannot add meal in restaurant in database!");
            }
        }
    }
    // todo test
    public void addIngredientsInKitchen(Ingredient ingredient, Kitchen kitchen, Double quantity) {
        this.kitchens.get(kitchen.getKitchenID()).addIngredientToInventory(ingredient, quantity);
        if (DbDataManager.connection != null) {
            try {
                String sql = "insert into `Ingredient_in_Kitchen` values (?,?,?);";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, ingredient.getIngredientID());
                pstmt.setString(2, kitchen.getKitchenID());
                pstmt.setDouble(3, quantity);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot add ingredient in kitchen in database!");
            }
        }
    }

    // todo test
    public void addCooksInKitchen(Kitchen kitchen, Person cook, Boolean bIsBusy) {
        this.kitchens.get(kitchen.getKitchenID()).addCookToKitchen(cook, bIsBusy);
        if (DbDataManager.connection != null) {
            try {
                String sql = "insert into `Cook_in_Kitchen` values (?,?,?);";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, cook.getPersonID());
                pstmt.setString(2, kitchen.getKitchenID());
                pstmt.setBoolean(3, bIsBusy);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot add cook in kitchen in database!");
            }
        }
    }

    // todo test
    public void updatePerson (Person person) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "update Person set username=?, hashedPassword=?, firstName=?, lastname=?, gender=?, personType=? where personID = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, person.getUsername());
                pstmt.setInt(2, person.getHashedPassword());
                pstmt.setString(3, person.getFirstName());
                pstmt.setString(4, person.getLastName());
                pstmt.setString(5, person.getGender());
                pstmt.setString(6, person.getPersonType().toString());
                pstmt.setString(7, person.getPersonID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot update person in database!");
            }
        }
    }
    // todo test
    public void updateOrder (Order order) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "update `Order` set orderType=?, orderStatus=?, deliveryDriverID=?, restaurantID=?, clientID=?, orderDate=? where orderID=?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, order.getOrderType().toString());
                pstmt.setString(2, order.getOrderStatus().toString());
                if (order.getDeliveryDriver() != null) {
                    pstmt.setString(3, order.getDeliveryDriver().getPersonID());
                } else {
                    pstmt.setString(3, null);
                }
                pstmt.setString(4, order.getRestaurant().getRestaurantID());
                pstmt.setString(5, order.getClient().getPersonID());
                pstmt.setDate(6, new java.sql.Date(order.getOrderDate().getTime()));
                pstmt.setString(7, order.getOrderID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot update order in database!");
            }
        }
    }
    // todo test
    public void updateIngredient (Ingredient ingredient) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "update `Ingredient` set ingredientName=?, pricePerHundred=?, caloriesPerHundred=? where ingredientID = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, ingredient.getIngredientName());
                pstmt.setDouble(2, ingredient.getPricePerHundred());
                pstmt.setDouble(3, ingredient.getCaloriesPerHundred());
                pstmt.setString(4, ingredient.getIngredientID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot update ingredient in database!");
            }
        }
    }
    // todo test
    public void updateItem (Item item) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "update `Item` set itemName=?, price=? where itemID = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, item.getItemName());
                pstmt.setDouble(2, item.getPrice());
                pstmt.setString(3, item.getItemID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot update item in database!");
            }
        }
    }
    // todo test
    public void updateMeal(Meal meal) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "update `Meal` set mealType=?, mealName=?, price=?, timeToPrepare=? where mealID = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, meal.getMealType().toString());
                pstmt.setString(2, meal.getMealName());
                pstmt.setDouble(3, meal.getPrice());
                pstmt.setDouble(4, meal.getTimeToPrepare());
                pstmt.setString(5, meal.getMealID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot update meal in database!");
            }
        }
    }
    // todo test
    public void updateRestaurant(Restaurant restaurant) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "update `Restaurant` set name=?, directorID=? where restaurantID = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, restaurant.getRestaurantName());
                pstmt.setString(2, restaurant.getDirector().getPersonID());
                pstmt.setString(3, restaurant.getRestaurantID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot update restaurant in database!");
            }
        }
    }
    // todo test
    public void updateFranchise(Franchise franchise) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "update `Franchise` set restaurantID=?, managerID=?, location=? where franchiseID = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, franchise.getRestaurant().getRestaurantID());
                pstmt.setString(2, franchise.getManager().getPersonID());
                pstmt.setString(3, franchise.getLocation());
                pstmt.setString(4, franchise.getFranchiseID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot update franchise in database!");
            }
        }
    }
    // todo test
    public void updateKitchen(Kitchen kitchen) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "update `Kitchen` set franchiseID=? where kitchenID = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, kitchen.getFranchise().getFranchiseID());
                pstmt.setString(2, kitchen.getKitchenID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot update kitchen in database!");
            }
        }
    }
    // todo test
    public void updateIngredientsInKitchen(Ingredient ingredient, Kitchen kitchen, Double quantity) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "update `Ingredient_in_Kitchen` set quantity=? where kitchenID=? and ingredientID=?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setDouble(1, quantity);
                pstmt.setString(2, kitchen.getKitchenID());
                pstmt.setString(3, ingredient.getIngredientID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot update ingredients in kitchen in database!");
            }
        }
    }

    // todo test
    public void updateCooksInKitchen(Kitchen kitchen, Person cook, Boolean bIsBusy) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "update `Cook_in_Kitchen` set isBusy=? where kitchenID=? and cookID=?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setBoolean(1, bIsBusy);
                pstmt.setString(2, kitchen.getKitchenID());
                pstmt.setString(3, cook.getPersonID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot update cooks in kitchen in database!");
            }
        }
    }

    // I don't even want to know how deletion will look like
    // todo test
    public void deletePerson (Person person) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "delete from Person where personID=?;";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, person.getPersonID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot remove person from database!");
            }
        }
        this.persons.remove(person.getUsername(), person);
    }
    // todo test
    public void deleteOrder (Order order) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "delete from `Order` where orderID=?;";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, order.getOrderID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot remove order from database!");
            }
        }
        this.orders.remove(order.getOrderID(), order);
    }
    // todo test
    public void deleteIngredient (Ingredient ingredient) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "delete from `Ingredient` where ingredientID=?;";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, ingredient.getIngredientID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot remove ingredient from database!");
            }
        }
        this.ingredients.remove(ingredient.getIngredientID(), ingredient);
    }
    // todo test
    public void deleteItem (Item item) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "delete from `Item` where itemID=?;";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, item.getItemID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot remove item from database!");
            }
        }
        this.items.remove(item.getItemID(), item);
    }
    // todo test
    public void deleteMeal(Meal meal) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "delete from `Meal` where mealID=?;";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, meal.getMealID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot remove meal from database!");
            }
        }
        this.meals.remove(meal.getMealID(), meal);
    }
    // todo test
    public void deleteRestaurant(Restaurant restaurant) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "delete from `Restaurant` where restaurantID=?;";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, restaurant.getRestaurantID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot remove restaurant from database!");
            }
        }
        this.restaurants.remove(restaurant.getRestaurantID(), restaurant);
    }
    // todo test
    public void deleteFranchise(Franchise franchise) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "delete from `Franchise` where franchiseID=?;";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, franchise.getFranchiseID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot remove franchise from database!");
            }
        }
        this.franchises.remove(franchise.getFranchiseID(), franchise);
    }
    // todo test
    public void deleteKitchen(Kitchen kitchen) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "delete from `Kitchen` where kitchenID = ?;";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, kitchen.getKitchenID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot remove kitchen from database!");
            }
        }
        this.kitchens.remove(kitchen.getKitchenID(), kitchen);
    }
    // todo test
    public void deleteMealInRestaurant(Meal meal, Restaurant restaurant) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "delete from `Meal_in_Restaurant` where restaurantID=? and mealID=?;";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, restaurant.getRestaurantID());
                pstmt.setString(2, meal.getMealID());
                pstmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println("Error: cannot remove meal in restaurant from database!");
            }
        }
        this.restaurants.get(restaurant.getRestaurantID()).removeMealFromCatalogue(meal);
    }
    // todo test
    public void deleteIngredientsInKitchen(Ingredient ingredient, Kitchen kitchen) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "delete from `Ingredient_in_Kitchen` where kitchenID=? and ingredientID=?;";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, kitchen.getKitchenID());
                pstmt.setString(2, ingredient.getIngredientID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot remove ingredient in kitchen from database!");
            }
        }
        this.kitchens.get(kitchen.getKitchenID()).removeIngredientFromInventory(ingredient);
    }

    // todo test
    public void deleteCooksInKitchen(Kitchen kitchen, Person cook) {
        if (DbDataManager.connection != null) {
            try {
                String sql = "insert into `Cook_in_Kitchen` values (?,?,?);";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, cook.getPersonID());
                pstmt.setString(2, kitchen.getKitchenID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error: cannot remove cook in kitchen from database!");
            }
        }
        this.kitchens.get(kitchen.getKitchenID()).removeCookFromKitchen(cook);
    }
}