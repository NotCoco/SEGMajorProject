package main.java.com.projectBackEnd.Entities.User.Hibernate;

/**
 * This class models a custom exception thrown when a method fails due to a non-specific server failure
 */
public class ServerErrorException extends Exception {

    /**
     * Class constructor
     * @param message The message to be sent back to the client
     */
    public ServerErrorException(String message) {
        super(message);
    }
}
