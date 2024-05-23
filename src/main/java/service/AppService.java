package service;

import database.DbConnection;
import database.DbDataManager;
import model.food.Meal;
import model.location.Franchise;
import model.location.Restaurant;
import model.order.Order;
import model.order.OrderType;
import model.people.Person;
import model.people.PersonType;

import java.sql.Connection;
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
        System.out.println("1. Log out");
        System.out.println("2. Open resturant");
        System.out.println("3. Open resturant franchise");
        System.out.println("4. List owned restaurants");
        System.out.println("5. Appoint franchise manager");
        System.out.println("6. Exit");

        String ans = reader.nextLine();
        switch (ans.toLowerCase()){
            case "1":
            case "logout":
            case "log out":
                sessionService.LogOut();
                break;
            case "2":
            case "open restaurant":
                this.OpenRestaurant(SessionService.getLoggedPerson());
                break;
            case "3":
            case "open franchise":
                this.OpenFranchise();
                break;
            case "4":
            case "list":
            case "list owned":
                this.ListRestaurants(SessionService.getLoggedPerson());
                break;
            case "5":
            case "appoint":
            case "manager":
                this.AppointManager();
                break;
            case "6":
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
        System.out.println("4. Consult Restaurant menus");
        System.out.println("5. Exit");

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
                PlaceOrder(SessionService.getLoggedPerson());
                break;
            case "3":
            case "list":
            case "past":
            case "list past orders":
                this.PastOrders(SessionService.getLoggedPerson());
                break;
            case "4":
            case "consult":
            case "menu":
            case "restaurant menus":
                this.ConsultRestaurantMeals();
                break;
            case "5":
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

    private void PlaceOrder(Person person) {
        actionService.writeToLogFile("place_order");
        Restaurant choice = null;
        Meal choiceMeal = null;
        boolean bNumIsValid = false;
        while (!bNumIsValid) {
            System.out.println("These are the restaurants you can order from:");
            int idx = 1;
            String[] IDs = new String[dataManager.getRestaurants().size() + 2];
            for (Restaurant restaurant : dataManager.getRestaurants().values()){
                IDs[idx] = restaurant.getRestaurantID();
                System.out.println(idx++ + ". " + restaurant);
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

            if (value > 0 && value <= choice.getMealsInCatalogue().size()) {
                bNumIsValid = true;
                choiceMeal = dataManager.getMeals().get(IDs[value]);
            } else {
                System.out.println("Input a value that is in the correct range!");
            }
        }
        bNumIsValid = false;
        OrderType type = null;
        while (!bNumIsValid) {
            System.out.println("Choose order type:");
            System.out.println("1. Dine-in");
            System.out.println("2. Delivery");
            System.out.println("3. Takeaway");

            String ans = reader.nextLine().toLowerCase();
            switch (ans){
                case "dine-in":
                case "1":
                    bNumIsValid = true;
                    type = OrderType.DINE_IN;
                    break;
                case "delivery":
                case "2":
                    bNumIsValid = true;
                    type = OrderType.DELIVERY;
                    break;
                case "takeaway":
                case "3":
                    bNumIsValid = true;
                    type = OrderType.TAKEAWAY;
                    break;
                default:
                    System.out.println("Please input a valid choice!");
            }
        }
        System.out.println("Thank you for ordering!");
        Order order = new Order(type, choice, person);
        dataManager.addOrder(order);
    }

    private void OpenRestaurant(Person director) {
        actionService.writeToLogFile("open_restaurant");
        System.out.println("Name your restaurant: ");
        String ans = reader.nextLine();
        Restaurant restaurant = new Restaurant(ans, director);
        dataManager.addRestaurant(restaurant);
    }

    private void ListRestaurants(Person director){
        actionService.writeToLogFile("list_restaurants");
        System.out.println("The restaurants you own are the following:");
        for (Restaurant restaurant : dataManager.getRestaurants().values()){
            if (restaurant.getDirector().equals(director)) {
                System.out.println(restaurant.getRestaurantName());
            }
        }
    }

    private void OpenFranchise(){
        actionService.writeToLogFile("open_franchise");
        Restaurant choice;
        boolean bNumIsValid = false;
        while (!bNumIsValid) {
            System.out.println("These are the restaurants you own:");
            int idx = 1;
            String[] IDs = new String[dataManager.getRestaurants().size() + 2];
            for (Restaurant restaurant : dataManager.getRestaurants().values()){
                if (restaurant.getDirector().equals(SessionService.getLoggedPerson())) {
                    IDs[idx++] = restaurant.getRestaurantID();
                    System.out.println(restaurant.getRestaurantName());
                }
            }
            System.out.println("Select restaurant to extend (1->" + dataManager.getRestaurants().size() + "):");
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
                System.out.println("Where to create new franchise?");
                String ans2 = reader.nextLine();
                Franchise franchise = new Franchise(ans2, choice);
                dataManager.addFranchise(franchise);
                bNumIsValid = true;
            } else {
                System.out.println("Input a value that is in the correct range!");
            }
        }
    }

    private void AppointManager() {
        actionService.writeToLogFile("appoint_manager");
        Restaurant rChoice = null;
        Person mgrChoice = null;
        Franchise frChoice = null;
        int franchiseCnt = 0;
        int managerCnt = 0;

        boolean bNumIsValid = false;
        while (!bNumIsValid) {
            System.out.println("These are the restaurants you own:");
            int idx = 1;
            String[] IDs = new String[dataManager.getRestaurants().size() + 2];
            for (Restaurant restaurant : dataManager.getRestaurants().values()) {
                if (restaurant.getDirector().equals(SessionService.getLoggedPerson())) {
                    IDs[idx++] = restaurant.getRestaurantID();
                    System.out.println(restaurant.getRestaurantName());
                }
            }
            System.out.println("Select restaurant to extend (1->" + (idx - 1) + "):");
            String ans = reader.nextLine();

            int value;
            try {
                value = Integer.parseInt(ans);
            } catch (NumberFormatException e) {
                System.out.println("Please input a numeric value!");
                continue;
            }

            if (value > 0 && value <= idx - 1) {
                rChoice = dataManager.getRestaurants().get(IDs[value]);
                bNumIsValid = true;
            }
        }
        bNumIsValid = false;
        while (!bNumIsValid) {
            System.out.println("These are the franchises that belong to selected restaurant:");
            int idx = 1;
            String[] IDs = new String[dataManager.getFranchises().size() + 2];
            for (Franchise franchise : dataManager.getFranchises().values()) {
                if (franchise.getRestaurant().equals(rChoice)) {
                    franchiseCnt += 1;
                    IDs[idx++] = franchise.getFranchiseID();
                    System.out.println(franchise.getLocation());
                }
            }
            System.out.println("Select franchise (1->" + franchiseCnt + "):");
            String ans = reader.nextLine();

            int value;
            try {
                value = Integer.parseInt(ans);
            } catch (NumberFormatException e) {
                System.out.println("Please input a numeric value!");
                continue;
            }

            if (value > 0 && value <= franchiseCnt) {
                frChoice = dataManager.getFranchises().get(IDs[value]);
            } else {
                System.out.println("Input a value that is in the correct range!");
            }
            bNumIsValid = true;
        }
        bNumIsValid = false;
        while (!bNumIsValid) {
            System.out.println("These are the managers you can apoint:");
            int idx = 1;
            String[] IDs = new String[dataManager.getPersons().size() + 2];
            for (Person person : dataManager.getPersons().values()) {
                if (person.getPersonType() == PersonType.MANAGER) {
                    managerCnt += 1;
                    IDs[idx++] = person.getUsername();
                    System.out.println(person.getFirstName() + " " + person.getLastName());
                }
            }
            System.out.println("Select manager (1->" + managerCnt + "):");
            String ans = reader.nextLine();

            int value;
            try {
                value = Integer.parseInt(ans);
            } catch (NumberFormatException e) {
                System.out.println("Please input a numeric value!");
                continue;
            }

            if (value > 0 && value <= managerCnt) {
                mgrChoice = dataManager.getPersons().get(IDs[value]);
            } else {
                System.out.println("Input a value that is in the correct range!");
            }
            bNumIsValid = true;
        }

        assert(rChoice != null);
        assert(frChoice != null);
        assert(mgrChoice != null);
        frChoice.appointManager(mgrChoice);
        dataManager.updateFranchise(frChoice);
    }

    private void PastOrders(Person person) {
        for (Order order : dataManager.getOrders().values()) {
            if (order.getClient().equals(person)) {
                System.out.println(order);
            }
        }
    }

    private void ConsultRestaurantMeals() {
        Restaurant rChoice = null;
        boolean bNumIsValid = false;
        while (!bNumIsValid) {
            System.out.println("These are the restaurants you can order from:");
            int idx = 1;
            String[] IDs = new String[dataManager.getRestaurants().size() + 2];
            for (Restaurant restaurant : dataManager.getRestaurants().values()) {
                System.out.println(restaurant.getRestaurantName());
                IDs[idx++] = restaurant.getRestaurantID();
            }
            System.out.println("Select restaurant to consult (1->" + (idx - 1) + "):");
            String ans = reader.nextLine();

            int value;
            try {
                value = Integer.parseInt(ans);
            } catch (NumberFormatException e) {
                System.out.println("Please input a numeric value!");
                continue;
            }

            if (value > 0 && value <= idx - 1) {
                rChoice = dataManager.getRestaurants().get(IDs[value]);
                bNumIsValid = true;
            }
        }
        for (Meal meal : rChoice.getMealsInCatalogue()) {
            System.out.println(meal);
        }
    }
}