package main.java.com.projectBackEnd.Entities.User.Hibernate;
public class TokenNotExistException extends Exception {
 
    public TokenNotExistException(String message) {
        super(message);
    }
}
