package main.java.com.projectBackEnd.Entities.User;
public class TokenNotExistException extends Exception {
 
    public TokenNotExistException(String message) {
        super(message);
    }
}
