package main.java.com.projectBackEnd;
import io.micronaut.runtime.Micronaut;
import main.java.com.projectBackEnd.Services.User.Hibernate.UserManagerInterface;
import main.java.com.projectBackEnd.Services.User.Hibernate.UserManager;

/**
 * Main method : runs the server
 */
public class BackEndMain {

	public static void main(String []args){
		
		Micronaut.run(BackEndMain.class);
	}
}
