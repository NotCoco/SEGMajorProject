package main.java.com.projectBackEnd.Entities.User;

import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;


public class UserManager extends EntityManager implements UserManagerInterface {
	private static UserManagerInterface userManager;
    private UserManager() {
        super();
        setSubclass(User.class);
        HibernateUtility.addAnnotation(User.class);
		userManager = this;
    }
	public static UserManagerInterface getUserManager(){
		if(userManager != null)
			return userManager;
		else
			return new UserManager();
	}
	public void addUser(String username, String password) throws UsernameExistsException{

	}
	public String verifyUser(String username,String password){
		return null;
	}
	public void changePassword(String username, String newPassword){

	}
	public void deleteUser(String username) throws UserNotExistException{

	}


}
