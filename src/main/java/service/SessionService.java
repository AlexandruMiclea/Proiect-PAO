package service;

import model.people.Person;
import model.people.PersonType;

public class SessionService {
    private static SessionService sessionService;
    private static Person loggedPerson;
    private SessionService(){};

    public static SessionService getSessionService() {
        if (SessionService.sessionService == null) {
            SessionService.sessionService = new SessionService();
        }
        return SessionService.sessionService;
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

    public static Person getLoggedPerson() {
        return loggedPerson;
    }

    public PersonType getLoggedPersonType() {
        if (loggedPerson != null) {
            return loggedPerson.getPersonType();
        }
        return null;
    }
}