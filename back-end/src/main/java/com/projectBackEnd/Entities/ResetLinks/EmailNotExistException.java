package main.java.com.projectBackEnd.Entities.ResetLinks;
public class EmailNotExistException extends Exception {
 
    public EmailNotExistException(String message) {
        super(message);
    }
}
