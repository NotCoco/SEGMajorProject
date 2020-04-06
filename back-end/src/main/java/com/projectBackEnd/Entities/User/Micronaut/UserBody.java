package main.java.com.projectBackEnd.Entities.User.Micronaut;

/**
 * This class encapsulates a user entity, excluding the ID.
 */
public class UserBody{

	private String name;
	private String password;
	private String email;

	/**
	 * Default constructor
	 */
	public UserBody(){}

	/**
	 * Constructor with name setting
	 * @param email User email
	 * @param password User password
	 * @param name User name
	 */
	public UserBody(String email, String password,String name){
		this.email = email;
		this.password = password;
		this.name = name;	
	}

	/**
	 * Constructor without name setting
	 * @param email User email
	 * @param password User password
	 */
	public UserBody(String email, String password){
		this.email = email;
		this.password = password;
	}

	/**
	 * Gets the user email
	 * @return Email
	 */
	public String getEmail(){return email; }

	/**
	 * Gets the user password
	 * @return Password
	 */
	public String getPassword(){
		return password;
	}

	/**
	 * Sets the user email
	 * @param email Email to set
	 */
	public void setEmail(String email){
		this.email = email;
	}

	/**
	 * Sets the user password
	 * @param password Password to set
	 */
	public void setPassword(String password){
		this.password = password;
	}

	/**
	 * Gets the user name
	 * @return Name
	 */
	public String getName(){
		return name;
	}

	/**
	 * Sets the user name
	 * @param name Name to set
	 */
	public void setName(String name){
		this.name = name;
	}


}
