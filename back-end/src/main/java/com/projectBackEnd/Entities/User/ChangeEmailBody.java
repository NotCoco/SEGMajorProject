package main.java.com.projectBackEnd.Entities.User;

public class ChangeEmailBody{
	public String oldEmail;
	public String newEmail;
	public ChangeEmailBody(String oldEmail,String newEmail){
		this.oldEmail = oldEmail;
		this.newEmail = newEmail;
	}
	public ChangeEmailBody(){}

}
