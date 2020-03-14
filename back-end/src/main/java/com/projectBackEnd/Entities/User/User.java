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
	private final static String EMAIL = "email";
	private final static String PASSWORD = "password";
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    	@Column(name = KEY)
   	private int primaryKey;
    	@Column(name = EMAIL)
    	@Type(type="text")
	private String email;
    	@Column(name = PASSWORD)
    	@Type(type="text")
	private String password;
	public User(String email, String password){
		this.email = email;
		this.password = password;
	}
	public User(){};
    	public Serializable getPrimaryKey() {
        	return primaryKey;
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

    	@Override
    	public String toString() {
        	return "User: " + this.primaryKey + ", " + this.email + ", " + this.password;
    	}

	public boolean equals(User user){
		return user.email == email;
	}
  @Override
    public TableEntity copy(TableEntity newCopy) {
        if(newCopy instanceof User){
            setEmail(((User) newCopy).getEmail());
            setPassword(((User) newCopy).getPassword());
        }
        return newCopy;
    }
}
