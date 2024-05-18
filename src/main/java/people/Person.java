package people;

// The most abstract representation of a person
public abstract class Person {
    private final String firstName;
    private final String lastName;
    private final String ID;
    private final String gender;

    public Person(String firstName, String lastName, String ID, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ID = ID;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Persoana cu numele de " + firstName + lastName + ", ID " + ID + ", gen " + gender + ".";
    }
}
