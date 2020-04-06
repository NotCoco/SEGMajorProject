package main.java.com.projectBackEnd.Entities.User.Hibernate;

/**
 * This class models a custom exception thrown during validation checking when a user enters an invalid email
 */
public class InvalidEmailException extends Exception {

    /**
     * Class constructor
     * @param message The message to be sent back to the client
     */
    public InvalidEmailException(String message) {
        super(message); // call to Exception constructor
    }
}
