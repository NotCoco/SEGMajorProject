package main.java.com.projectBackEnd;
import io.micronaut.runtime.Micronaut;
import main.java.com.projectBackEnd.Services.User.Hibernate.UserManagerInterface;
import main.java.com.projectBackEnd.Services.User.Hibernate.UserManager;
import main.java.com.projectBackEnd.Services.User.Hibernate.Exceptions.*;

/**
 * Main method : runs the server
 */
public class BackEndMain {

	public static void main(String []args){
		
		if(UserManager.getUserManager().getUsers().size() == 0){
			try{
				UserManager.getUserManager().addUser("admin@admin.com","admin", "name");
			}
			catch(EmailExistsException|InvalidEmailException|IncorrectNameException|InvalidPasswordException e){

			}
		}
		Micronaut.run(BackEndMain.class);
	}
}
