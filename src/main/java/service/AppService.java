package service;

import java.util.Scanner;

public class AppService {
    private static AppService service;
    private Boolean bRunApp = true;
    private final Scanner reader = new Scanner(System.in);
    private static ActionService actionService;
    private static SessionService sessionService;
    private AppService() {
        AppService.actionService = ActionService.getActionService();
        AppService.sessionService = SessionService.getSessionService();
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
            app.StartMenu();
        }
    }

    private void StartMenu() {
        System.out.println("1. Log in");
        System.out.println("2. Register");
        System.out.println("3. Enable/Disable logging service");
        System.out.println("4. Exit");

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
            case "log":
            case "logging":
                System.out.println("ceva");
                actionService.toggleFileWriter();
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

    private void LoginMenu() {
        System.out.println("Log in menu");
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

}