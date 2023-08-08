package om.mindhub.homebanking.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Client {
    @Id
    private int clientID;
    private String firstName;
    private String lastName;
    private String email;

    public Client() {
    }

    public Client(int clientID, String firstName, String lastName, String email) {
        this.clientID = clientID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getClientID() {
        return clientID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientID=" + clientID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
