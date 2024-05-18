package model.people;

import util.RandomString;

// The most abstract representation of a person
public class Person {
    private final String ID;
    private String username;
    private String hashedPassword;
    private String firstName;
    private String lastName;
    private String gender;
    private PersonType personType;

    public PersonType getPersonType() {
        return personType;
    }

    public Person(String firstName, String lastName, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ID = RandomString.getRandomString();
        this.gender = gender;
        this.personType = PersonType.CLIENT;
    }

    @Override
    public String toString() {
        return personType.toString() + ' ' + firstName + ' ' + lastName;
    }
}