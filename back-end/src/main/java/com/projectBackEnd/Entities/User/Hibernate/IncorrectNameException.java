package main.java.com.projectBackEnd.Entities.User.Hibernate;
/**
 * This class models a custom exception thrown during validation checking when a user enters an incorrect name
 */
public class IncorrectNameException extends Exception {
    /**
     * Class constructor
     * @param message The message to be sent back to the client
     */
    public IncorrectNameException(String message) {
        super(message); // call to Exception constructor
    }
}
