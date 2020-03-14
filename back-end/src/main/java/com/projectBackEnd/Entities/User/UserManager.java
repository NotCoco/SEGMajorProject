package main.java.com.projectBackEnd.Entities.User;

import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;
import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import java.security.MessageDigest; 
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException; 
import java.util.List;
import java.math.BigInteger;

//import org.apache.commons.validator.routines.EmailValidator;





public class UserManager extends EntityManager implements UserManagerInterface {
	private static UserManagerInterface userManager;
	private static final int TIMEOUT = 3600; //amount of time for which session will be valid
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
	public void addUser(String email, String password) throws EmailExistsException,InvalidEmailException{
//		if(!EmailValidator.getInstance().isValid(email))
//			throw new InvalidEmailException("email: " + email + " is invalid");
		if(getAll().stream().filter(u->((User)u).getEmail().equals(email)).count() > 0)
			throw new EmailExistsException("email: " + email + " already exsists");
		User user = new User(email,hash(password));
		insertTuple(user);
	}
	public String verifyUser(String email,String password){
		if(getAll().stream().filter(u->(((User)u).getEmail().equals(email) && ((User)u).getPassword().equals(hash(password)))).count() > 0){
			return SessionManager.getSessionManager().getNewSession(email,TIMEOUT);
		}
		else
			return null;
			
	}
	public void changePassword(String email, String newPassword) throws UserNotExistException{
		List<User> users = getAll();
		User user = null;
		for(User u:users){
			if(u.getEmail().equals(email))
				user = u;
		}
		if(user == null)
			throw new UserNotExistException("there is no user with email: " + email);
		user.setPassword(hash(newPassword));
		update(user);
		
	}
	public void deleteUser(String email) throws UserNotExistException{
		List<User> users = getAll();
		boolean found = false;
		for(User u: users){
			if(u.getEmail().equals(email)){
				delete(u);
				found = true;
				break;
			}
		}
		if(!found)
			throw new UserNotExistException("there is no user with email: " + email);
	}
	public void changeEmail(String oldEmail, String newEmail) throws UserNotExistException { // to be tested
		List<User> users = getAll();
		User user = null;
		for(User u:users){
			if(u.getEmail().equals(oldEmail))
				user = u;
		}
		if(user == null)
			throw new UserNotExistException("there is no user with email: " + oldEmail);
		user.setEmail(newEmail);
		update(user);
	}
	public boolean verifyEmail(String email){ //to be tested
		return getAll().stream().filter(u->((User)u).getEmail().equals(email)).count() > 0;
	}
	private String hash(String in){
		try{
			MessageDigest alg = MessageDigest.getInstance("SHA-512"); 
			alg.reset();
			alg.update(in.getBytes(StandardCharsets.UTF_8));
			return String.format("%0128x", new BigInteger(1, alg.digest()));
		}
        catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 

		}
		
	}
	


}
