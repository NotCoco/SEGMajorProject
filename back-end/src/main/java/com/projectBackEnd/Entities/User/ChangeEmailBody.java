package main.java.com.projectBackEnd.Entities.User;

public class ChangeEmailBody implements ChangeEmailBodyInterface{
	private String oldEmail;
	private String newEmail;
	public ChangeEmailBody(String oldEmail,String newEmail){
		this.oldEmail = oldEmail;
		this.newEmail = newEmail;
	}
	public ChangeEmailBody(){}
	public String getOldEmail(){
		return oldEmail;
	}
	public String getNewEmail(){
		return newEmail;
	}
	public void setOldEmail(String email){
		this.oldEmail = email;
	}
	public void setNewEmail(String email){
		this.newEmail = email;
	}
}
