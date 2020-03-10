package main.java.com.projectBackEnd.Entities.User;
public class EmailExistsException extends Exception {
 
    public EmailExistsException(String message) {
        super(message);
    }
}
