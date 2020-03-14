package main.java.com.projectBackEnd.Entities.User;
import main.java.com.projectBackEnd.Entities.ResetLinks.ResetLinkManager;
import main.java.com.projectBackEnd.Entities.ResetLinks.EmailNotExistException;

public interface PasswordResetInterface{
	public void sendPasswordResetLink(String email) throws EmailNotExistException, ServerErrorException;
	public void changePassword(String token,String password) throws TokenNotExistException,UserNotExistException;
}
