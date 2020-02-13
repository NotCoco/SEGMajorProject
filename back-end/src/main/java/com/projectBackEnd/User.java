package main.java.com.projectBackEnd;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.*;



@Entity
@Table(name = User.TABLENAME)
public class User{

	public static final String TABLENAME = "user";

	public User(String username, String password){
		this.username = username;
		this.password = password;
	}
	@Id @GeneratedValue
	@Column(name="id",unique=true)
	private int id;
	
	@Column(name="username", nullable = false)
	private String username;

	@Column(name="password",nullable = false)
	private String password;

	public void setUsername(String username){
		this.username = username;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public void setId(int id){
		this.id = id;
	}
	public String getUsername(){
		return username;
	}
	public String getPassword(){
		return password;
	}
	public int getId(){
		return id;
	}


}
