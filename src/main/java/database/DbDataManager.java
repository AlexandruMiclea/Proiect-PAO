package database;

import model.food.Ingredient;
import model.food.Item;
import model.food.Meal;
import model.food.MealType;
import model.order.Order;
import model.order.OrderStatus;
import model.order.OrderType;
import model.people.Person;
import model.people.StringComparator;
import model.people.PersonType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DbDataManager {
    private Map<String, Person> persons;
    private Map<String, Order> orders;
    private Map<String, Ingredient> ingredients;
    private Map<String, Item> items;
    private Map<String, Meal> meals;
    private static DbDataManager dataManager;
    private static Connection connection;

    {
        this.persons = new TreeMap<String, Person>(new StringComparator());
        this.orders = new TreeMap<String, Order>(new StringComparator());
        this.ingredients = new TreeMap<String, Ingredient>(new StringComparator());
        this.items = new TreeMap<String, Item>(new StringComparator());
        this.meals = new TreeMap<String, Meal>(new StringComparator());
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
            System.out.println("Error: cannot get orders from database!");
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
            System.out.println("Error: cannot get orders from database!");
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
            System.out.println("Error: cannot get orders from database!");
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
            System.out.println("Error: cannot get orders from database!");
        }
    }
}
