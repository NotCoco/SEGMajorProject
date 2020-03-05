package main.java.com.projectBackEnd.Entities.User;
public class UsernameExistsException extends Exception {
 
    public UsernameExistsException(String message) {
        super(message);
    }
}
