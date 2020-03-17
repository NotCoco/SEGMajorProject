package main.java.com.projectBackEnd.Entities.User;

public class ChangeEmailBody{
	public String oldEmail;
	public String newEmail;
	public String sessionToken;
	public ChangeEmailBody(String oldEmail,String newEmail, String sessionToken){
		this.oldEmail = oldEmail;
		this.newEmail = newEmail;
		this.sessionToken = sessionToken;
	}
	public ChangeEmailBody(){}

}
