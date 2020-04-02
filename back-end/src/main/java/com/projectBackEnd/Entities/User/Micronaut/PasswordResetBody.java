package main.java.com.projectBackEnd.Entities.User.Micronaut;

/**
 * This class encapsulates the operations involved in resetting a password
 */
public class PasswordResetBody{
	private String password;
	private String token;// token used for user validation

	/**
	 * Main constructor
	 * @param token Validation token
	 * @param password Password to reset
	 */
	public PasswordResetBody(String token, String password){
		this.token = token;
		this.password = password;
	}

	/**
	 * Empty constructor
	 */
	public PasswordResetBody(){}

	/**
	 * Gets the user password
	 * @return The password
	 */
	public String getPassword(){
		return password;
	}

	/**
	 * Gets the authentication token
	 * @return The token
	 */
	public String getToken(){
		return token;
	}

	/**
	 * Sets the user password
	 * @param password The new password
	 */
	public void setPassword(String password){this.password = password; }

	/**
	 * Sets the token value
	 * @param token The new token
	 */
	public void setToken(String token){
		this.token= token;
	}
}
