package main.java.com.projectBackEnd;
import io.micronaut.runtime.Micronaut;
import main.java.com.projectBackEnd.Services.User.Hibernate.UserManagerInterface;
import main.java.com.projectBackEnd.Services.User.Hibernate.UserManager;
import main.java.com.projectBackEnd.Services.User.Hibernate.Exceptions.*;

/**
 * Main method : runs the server
 */
public class BackEndMain {

	/**
	 * When invoked, this method will cause the Micronaut endpoints to be deployed.
	 * @param args Arguments for command line running.
	 */
	public static void main(String []args){
		Micronaut.run(BackEndMain.class);
	}
}
