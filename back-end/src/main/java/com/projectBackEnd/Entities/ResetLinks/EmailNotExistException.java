package main.java.com.projectBackEnd.Entities.ResetLinks;

/**
 * This class models a custom exception thrown when an email is non-existing in the database
 */
public class EmailNotExistException extends Exception {
 

    /**
     * Class constructor
     * @param message The message to be sent back to the client
     */
    public EmailNotExistException(String message) {
        super(message);
    }
}
