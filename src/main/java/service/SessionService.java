package service;

import model.people.Person;

public class SessionService {
    private static SessionService sessionService;
    private static Person loggedPerson;
    private SessionService() {

    }

    public static SessionService getSessionService() {
        if (SessionService.sessionService == null) {
            SessionService.sessionService = new SessionService();
        }
        return  SessionService.sessionService;
    }

    public void LogIn(Person person) {
        loggedPerson = person;
    }

    public void LogOut() {
        if (loggedPerson != null) {
            loggedPerson = null;
        } else {
            System.out.println("No one is logged in! Log out should not be available.");
        }
    }

    public String getLoggedPersonType() {
        if (loggedPerson != null) {
            return String.valueOf(loggedPerson.getClass());
        }
        return "unauthenticated";
    }
}