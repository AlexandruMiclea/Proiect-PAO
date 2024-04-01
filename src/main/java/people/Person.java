package people;

// The most abstract representation of a person
public abstract class Person {
    String firstName;
    String lastName;

    // TODO regex validation for this
    final String ID;
    String gender;

    public Person(String firstName, String lastName, String ID, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ID = ID;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", ID='" + ID + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
