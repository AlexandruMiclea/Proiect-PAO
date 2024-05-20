package service;

import database.DbConnection;
import database.DbDataManager;
import model.food.Meal;
import model.location.Restaurant;
import model.people.Person;
import model.people.PersonType;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AppService {
    private static AppService service;
    private Boolean bRunApp = true;
    private final Scanner reader = new Scanner(System.in);
    private static ActionService actionService;
    private static SessionService sessionService;
    private static Connection dbConnection;
    private static DbDataManager dataManager;
    private AppService() {
        AppService.actionService = ActionService.getActionService();
        AppService.sessionService = SessionService.getSessionService();
        AppService.dbConnection = DbConnection.getDbConnection();
        // even if a database connection cannot be established, I can still use the collections
        // provided by the dataManager class as a means to store data.
        AppService.dataManager = DbDataManager.getDataManager(dbConnection);
    }

    private static AppService getService() {
        if (AppService.service == null){
            AppService.service = new AppService();
        }
        return AppService.service;
    }

    public static void main(String[] args) {
        AppService app = AppService.getService();
        System.out.println("Welcome to Food Delivery app!");
        while (app.bRunApp){
            if (sessionService.getLoggedPersonType() == null) {
                app.StartMenu();
            } else {
                switch (sessionService.getLoggedPersonType()){
                    case PersonType.DELIVERY_DRIVER:
                        app.DeliveryDriverMenu();
                        break;
                    case PersonType.ADMIN:
                        app.AdminMenu();
                        break;
                    case PersonType.MANAGER:
                        app.ManagerMenu();
                        break;
                    case PersonType.COOK:
                        app.CookMenu();
                        break;
                    case PersonType.DIRECTOR:
                        app.DirectorMenu();
                        break;
                    default:
                        app.ClientMenu();
                        break;
                }
            }
        }
    }

    private void StartMenu() {
        System.out.println("1. Log in");
        System.out.println("2. Register");
        System.out.println("3. Exit");

        String ans = reader.nextLine();
        switch (ans.toLowerCase()){
            case "1":
            case "login":
            case "log in":
                LoginMenu();
                break;
            case "2":
            case "register":
            case "reg":
                RegisterMenu();
                break;
            case "3":
            case "exit":
            case "quit":
                bRunApp = false;
                actionService.closeFileWriter();
                break;
            default:
                System.out.println("Invalid input...");
                break;
        }
    }

    private void LoginMenu() {
        actionService.writeToLogFile("login");
        boolean bFoundUsername = false, bCorrectPassword = false;
        while (!bFoundUsername){
            System.out.println("Enter username: ");
            String usernameInput = reader.nextLine();
            Map<String, Person> persons = dataManager.getPersons();
            bFoundUsername = persons.containsKey(usernameInput);
            if (bFoundUsername) {
                Person queriedPerson = persons.get(usernameInput);
                while (!bCorrectPassword) {
                    System.out.println("Enter Password");
                    String passwordInput = reader.nextLine();
                    Integer hashedPassword = passwordInput.hashCode();
                    bCorrectPassword = queriedPerson.checkPassword(hashedPassword);
                    if (bCorrectPassword) {
                        sessionService.LogIn(queriedPerson);
                    } else {
                        System.out.println("Wrong password! Try again.");
                    }
                }
            } else {
                System.out.println("Username not found! Try again.");
            }
        }
    }

    private void RegisterMenu(){
        actionService.writeToLogFile("register");
        //public Person(String firstName, String lastName, String gender, String username, String Password)
        System.out.println("Enter first name: ");
        String _firstname = reader.nextLine();
        System.out.println("Enter last name: ");
        String _lastname = reader.nextLine();
        System.out.println("Enter gender: ");
        String _gender = reader.nextLine();
        String _username;
        String _password;
        boolean bUniqueUsername = false;
        while (!bUniqueUsername) {
            System.out.println("Enter username: ");
            _username = reader.nextLine();
            bUniqueUsername = !dataManager.getPersons().containsKey(_username);
            if (bUniqueUsername){
                // ok, get password
                boolean bPasswordIsCorrect = false;
                while(!bPasswordIsCorrect){
                    System.out.println("Enter password: ");
                    _password = reader.nextLine();
                    String passwordConfirm;
                    System.out.println("Enter password again for confirmation: ");
                    passwordConfirm = reader.nextLine();
                    bPasswordIsCorrect = _password.equals(passwordConfirm);
                    if (bPasswordIsCorrect) {
                        // create user
                        Person newUser = new Person(_firstname, _lastname, _gender, _username, _password);
                        dataManager.addPerson(newUser);
                    } else {
                        System.out.println("Passwords do not match! Try again.");
                    }
                }
            } else {
                System.out.println("Username already exists! Choose another username!");
            }
        }
    }

    private void DirectorMenu(){
        System.out.println("1. Log in");
        System.out.println("2. Register");
        System.out.println("3. Exit");

        String ans = reader.nextLine();
        switch (ans.toLowerCase()){
            case "1":
            case "login":
            case "log in":
                LoginMenu();
                break;
            case "2":
            case "register":
            case "reg":
                RegisterMenu();
                break;
            case "3":
            case "exit":
            case "quit":
                bRunApp = false;
                actionService.closeFileWriter();
                break;
            default:
                System.out.println("Invalid input...");
                break;
        }
    }

    private void ClientMenu() {
        System.out.println("1. Log out");
        System.out.println("2. Place order");
        System.out.println("3. List past orders");
        System.out.println("4. Exit");

        String ans = reader.nextLine();
        switch (ans.toLowerCase()){
            case "1":
            case "logout":
            case "log out":
                sessionService.LogOut();
                break;
            case "2":
            case "order":
            case "place order":
                // TODO add place order function
                placeOrder(SessionService.getLoggedPerson());
                break;
            case "3":
            case "list":
            case "past":
            case "list past orders":
                // TODO add list past orders function
                break;
            case "4":
            case "exit":
            case "quit":
                bRunApp = false;
                actionService.closeFileWriter();
                break;
            default:
                System.out.println("Invalid input...");
                break;
        }
    }

    private void ManagerMenu(){
        System.out.println("1. Log in");
        System.out.println("2. Register");
        System.out.println("3. Exit");

        String ans = reader.nextLine();
        switch (ans.toLowerCase()){
            case "1":
            case "login":
            case "log in":
                LoginMenu();
                break;
            case "2":
            case "register":
            case "reg":
                RegisterMenu();
                break;
            case "3":
            case "exit":
            case "quit":
                bRunApp = false;
                actionService.closeFileWriter();
                break;
            default:
                System.out.println("Invalid input...");
                break;
        }
    }

    private void DeliveryDriverMenu() {
        System.out.println("1. Log in");
        System.out.println("2. Register");
        System.out.println("3. Exit");

        String ans = reader.nextLine();
        switch (ans.toLowerCase()){
            case "1":
            case "login":
            case "log in":
                LoginMenu();
                break;
            case "2":
            case "register":
            case "reg":
                RegisterMenu();
                break;
            case "3":
            case "exit":
            case "quit":
                bRunApp = false;
                actionService.closeFileWriter();
                break;
            default:
                System.out.println("Invalid input...");
                break;
        }
    }

    private void CookMenu() {
        System.out.println("1. Log in");
        System.out.println("2. Register");
        System.out.println("3. Exit");

        String ans = reader.nextLine();
        switch (ans.toLowerCase()){
            case "1":
            case "login":
            case "log in":
                LoginMenu();
                break;
            case "2":
            case "register":
            case "reg":
                RegisterMenu();
                break;
            case "3":
            case "exit":
            case "quit":
                bRunApp = false;
                actionService.closeFileWriter();
                break;
            default:
                System.out.println("Invalid input...");
                break;
        }
    }

    private void AdminMenu(){
        System.out.println("1. Log out");
        System.out.println("2. Enable/Disable logging service (currently " + (actionService.getWriteStatus() ? "disabled" : "enabled")+ ")");
        System.out.println("3. Exit");

        String ans = reader.nextLine();
        switch (ans.toLowerCase()){
            case "1":
            case "logout":
            case "log out":
                sessionService.LogOut();
                break;
            case "2":
            case "log":
            case "logging":
                if (!actionService.getWriteStatus()){
                    actionService.toggleFileWriter();
                }else {
                    actionService.toggleFileWriter();
                }
                break;
            case "3":
            case "exit":
            case "quit":
                bRunApp = false;
                actionService.closeFileWriter();
                break;
            default:
                System.out.println("Invalid input...");
                break;
        }
    }

    private void placeOrder(Person person) {
        actionService.writeToLogFile("place_order");
        Restaurant choice = null;
        String _restaurantId;
        boolean bNumIsValid = false;
        while (!bNumIsValid) {
            System.out.println("These are the restaurants you can order from:");
            int idx = 1;
            String[] IDs = new String[dataManager.getRestaurants().size() + 2];
            for (Restaurant restaurant : dataManager.getRestaurants().values()){
                IDs[idx] = restaurant.getRestaurantID();
                System.out.println(idx++ + ". " + restaurant.toString());
            }
            System.out.println("Select restaurant to order from (1->" + dataManager.getRestaurants().size() + "):");
            String ans = reader.nextLine();

            int value;
            try {
                value = Integer.parseInt(ans);
            } catch (NumberFormatException e) {
                System.out.println("Please input a numeric value!");
                continue;
            }

            if (value > 0 && value <= dataManager.getRestaurants().size()) {
                choice = dataManager.getRestaurants().get(IDs[value]);
                _restaurantId = IDs[value];

                System.out.println("Choose meal to order (1->" + choice.getMealsInCatalogue().size() + "):");
                for (Meal meal : choice.getMealsInCatalogue()){
                    System.out.println(meal);
                }
                bNumIsValid = true;
            } else {
                System.out.println("Input a value that is in the correct range!");
            }
        }
        bNumIsValid = false;
        while (!bNumIsValid) {
            System.out.println("These are the meals you can order:");
            int idx = 1;
            String[] IDs = new String[choice.getMealsInCatalogue().size() + 2];
            for (Meal meal : choice.getMealsInCatalogue()){
                IDs[idx] = meal.getMealID();
                System.out.println(idx++ + ". " + meal.toString());
            }
            System.out.println("Select meal to order from (1->" + choice.getMealsInCatalogue().size() + "):");
            String ans = reader.nextLine();

            int value;
            try {
                value = Integer.parseInt(ans);
            } catch (NumberFormatException e) {
                System.out.println("Please input a numeric value!");
                continue;
            }

            if (value > 0 && value <= dataManager.getRestaurants().size()) {
                bNumIsValid = true;
                choice = dataManager.getRestaurants().get(IDs[value]);
                _restaurantId = IDs[value];

                System.out.println("Choose meal to order (1->" + choice.getMealsInCatalogue().size() + "):");
                for (Meal meal : choice.getMealsInCatalogue()){
                    System.out.println(meal);
                }

            } else {
                System.out.println("Input a value that is in the correct range!");
            }
        }
    }
}