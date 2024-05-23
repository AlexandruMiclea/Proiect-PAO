package model.people;

import util.RandomString;

import java.util.Objects;

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

    public Integer getHashedPassword() {
        return hashedPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
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

    // User register ctor
    public Person(String firstName, String lastName, String gender, String username, String Password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personID = RandomString.getRandomString();
        this.gender = gender;
        this.username = username;
        this.hashedPassword = Password.hashCode();
        this.personType = PersonType.CLIENT;
    }

    // register constructor TODO implement

    // equals


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(personID, person.personID);
    }

    // todo repair tostring
    @Override
    public String toString() {
        return personType.toString() + ' ' + firstName + ' ' + lastName + " " + personType + " " + gender;
    }
}