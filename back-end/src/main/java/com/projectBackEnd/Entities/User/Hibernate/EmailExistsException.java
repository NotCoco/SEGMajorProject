package main.java.com.projectBackEnd.Entities.User.Hibernate;
/**
 * This class models a custom exception thrown during validation checking when a user enters an already existing email
 */
public class EmailExistsException extends Exception {
    
    /**
     * Class constructor
     * @param message The message to be sent back to the client
     */
    EmailExistsException(String message) {
        super(message); // call to Exception constructor
    }
}
