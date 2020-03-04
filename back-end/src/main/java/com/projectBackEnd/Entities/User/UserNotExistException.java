package main.java.com.projectBackEnd.Entities.User;
public class UserNotExistException extends Exception {
 
    public UserNotExistException(String message) {
        super(message);
    }
}
