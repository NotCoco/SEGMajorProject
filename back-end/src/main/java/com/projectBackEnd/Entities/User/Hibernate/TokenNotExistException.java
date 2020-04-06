package main.java.com.projectBackEnd.Entities.User.Hibernate;

/**
 * This class models a custom exception thrown when an authentication token is invalid
 */
public class TokenNotExistException extends Exception {

    /**
     * Class constructor
     * @param message The message to be sent back to the client
     */
    TokenNotExistException(String message) {
        super(message);
    }
}