package main.java.com.projectBackEnd.Entities.User.Micronaut;

public class UserBody{

	private String name;
	private String password;
	private String email;
	public UserBody(){}
	public UserBody(String email, String password,String name){
		this.email = email;
		this.password = password;
		this.name = name;	
	}
	public UserBody(String email, String password){
		this.email = email;
		this.password = password;
	}


	public String getEmail(){
		return email;
	}
	public String getPassword(){
		return password;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}


}
