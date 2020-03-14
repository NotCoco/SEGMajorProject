package main.java.com.projectBackEnd.Entities.User;

public class PasswordResetBody{
	public String password;
	public String token;
	public PasswordResetBody(String token, String password){
		this.token = token;
		this.password = password;
	}
}
