package main.java.com.projectBackEnd.Entities.User.Hibernate;
public class EmailExistsException extends Exception {
 
    public EmailExistsException(String message) {
        super(message);
    }
}
