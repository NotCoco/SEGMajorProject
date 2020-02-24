package main.java.com.projectBackEnd.Entities.User;

import main.java.com.projectBackEnd.TableEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Entity
@Table(name = User.TABLENAME)
public class User implements TableEntity{
	public final static String TABLENAME = "Users";
	private final static String KEY = "id";
	private final static String USERNAME = "username";
	private final static String PASSWORD = "password";
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    	@Column(name = KEY)
   	private int primaryKey;
    	@Column(name = USERNAME)
    	@Type(type="text")
	private String username;
    	@Column(name = PASSWORD)
    	@Type(type="text")
	private String password;
	public User(String username, String password){
		this.username = username;
		this.password = password;
	}
	public User(){};
    	public Serializable getPrimaryKey() {
        	return primaryKey;
    	}
	public String getUsername(){
		return username;
	}
	public String getPassword(){
		return password;
	}
	public void setUsername(String username){
		this.username = username;
	}
	public void setPassword(String password){
		this.password = password;
	}

    	@Override
    	public String toString() {
        	return "User: " + this.primaryKey + ", " + this.username + ", " + this.password;
    	}

	public boolean equals(User user){
		return user.username == username;
	}
  @Override
    public TableEntity copy(TableEntity newCopy) {
        if(newCopy instanceof User){
            setUsername(((User) newCopy).getUsername());
            setPassword(((User) newCopy).getPassword());
        }
        return newCopy;
    }
}
