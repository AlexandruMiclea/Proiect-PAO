package model.people;

import util.RandomString;

public class Person {
    private final String personID;
    private String username;
    private Integer hashedPassword;
    private String firstName;
    private String lastName;
    private String gender;
    private PersonType personType;

    public String getPersonID() {
        return personID;
    }
    public String getUsername() {
        return username;
    }

    public Boolean checkPassword(Integer hashedPassword) {
        return this.hashedPassword.equals(hashedPassword);
    }

    public PersonType getPersonType() {
        return personType;
    }

    // DB constructor
    public Person(String personID, String firstName, String lastName, String gender, String username, Integer hashedPassword, PersonType personType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personID = personID;
        this.gender = gender;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.personType = personType;
    }

    // register constructor TODO implement

    // todo repair tostring
    @Override
    public String toString() {
        return personType.toString() + ' ' + firstName + ' ' + lastName + " " + personType + " " + gender;
    }
}