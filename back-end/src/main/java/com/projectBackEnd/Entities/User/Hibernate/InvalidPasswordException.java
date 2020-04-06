package main.java.com.projectBackEnd.Entities.User.Hibernate;

/**
 * This class models a custom exception thrown during validation checking when a user enters an invalid password
 */
public class InvalidPasswordException extends Exception {
 
    InvalidPasswordException(String message) {
        super(message);
    }
}
