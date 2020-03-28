package main.java.com.projectBackEnd.Entities.User.Hibernate;
import main.java.com.projectBackEnd.Entities.ResetLinks.ResetLinkManager;
import main.java.com.projectBackEnd.Entities.ResetLinks.EmailNotExistException;

public class PasswordReset implements PasswordResetInterface{
public static PasswordResetInterface passwordResetManager;
	private PasswordReset(){
		passwordResetManager = this;
	}
	public static PasswordResetInterface getPasswordResetManager(){
		if(passwordResetManager != null)
			return passwordResetManager;
		else
			return new PasswordReset();
	}
	public void sendPasswordResetLink(String email) throws EmailNotExistException, ServerErrorException{
		String token = ResetLinkManager.getResetLinkManager().create(email);
		String title = "Password Reset Request";
		String content = "please click the link to reset the password " + token + "\n if you did not request password reset please ignore this message";
		if(SendMail.send(email, title, content))
			return;
		else
			throw new ServerErrorException("error has occured");
	
	}
	public void changePassword(String token, String password) throws TokenNotExistException,UserNotExistException,InvalidPasswordException{
		if(password == null || password.isEmpty())
			throw new InvalidPasswordException("invalid password");	
		String email = ResetLinkManager.getResetLinkManager().getEmail(token);
		if(ResetLinkManager.getResetLinkManager().exist(token)){
			ResetLinkManager.getResetLinkManager().delete(token);
			UserManager.getUserManager().changePassword(email,password);
		}
		else
			throw new TokenNotExistException("incorrect token");
	}
}
