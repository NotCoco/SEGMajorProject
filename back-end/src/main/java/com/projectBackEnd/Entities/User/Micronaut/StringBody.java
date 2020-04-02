package main.java.com.projectBackEnd.Entities.User.Micronaut;

/**
 * This class is used to encapsulate a string to make high level string transactions easier with the micronaut framework
 */
public class StringBody{
	private String string;

	/**
	 * Empty constructor
	 */
	public StringBody(){}

	/**
	 * Main constructor
	 * @param string The central string to be stored in the class variable
	 */
	public StringBody(String string){
		this.string = string;
	}

	/**
	 * Gets the string
	 * @return The string
	 */
	public String getString(){
		return string;
	}

	/**
	 * Sets the string
	 * @param string The string
	 */
	public void setString(String string){
		this.string = string;
	}

}
