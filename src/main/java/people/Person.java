package people;

// The most abstract representation of a person
public class Person {
    private final String firstName;
    private final String lastName;
    private final String ID;
    private final String gender;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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
