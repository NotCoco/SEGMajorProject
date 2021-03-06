package main.java.com.projectBackEnd.Services.User.Hibernate.Exceptions;
/**
 * This class models a custom exception thrown when a given user does not exist in the database
 */
public class UserNotExistException extends Exception {

    /**
     * Class constructor
     * @param message The message to be sent back to the client
     */
    public UserNotExistException(String message) {
        super(message);
    }
}
