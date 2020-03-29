package main.java.com.projectBackEnd.Entities.User.Hibernate;
public class InvalidPasswordException extends Exception {
 
    public InvalidPasswordException(String message) {
        super(message);
    }
}
