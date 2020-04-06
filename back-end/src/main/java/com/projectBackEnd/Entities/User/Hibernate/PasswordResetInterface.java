package main.java.com.projectBackEnd.Entities.User.Hibernate;
import main.java.com.projectBackEnd.Entities.ResetLinks.EmailNotExistException;

/**
 * The methods invoked by API to reset a user password
 */
public interface PasswordResetInterface {

	void sendPasswordResetLink(String email)
			throws EmailNotExistException, ServerErrorException;

	void changePassword(String token,String password)
			throws TokenNotExistException, UserNotExistException, InvalidPasswordException;
}