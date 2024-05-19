package service;

import database.DbConnection;
import database.DbDataManager;
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
        if (AppService.dbConnection != null) {
            AppService.dataManager = DbDataManager.getDataManager(dbConnection);
        }
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
                        // todo create session manager
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
        System.out.println("Register menu");
    }

    private void DirectorMenu(){

    }

    private void ClientMenu() {

    }

    private void ManagerMenu(){

    }

    private void DeliveryDriverMenu() {

    }

    private void CookMenu() {

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
}