package main.java.com.projectBackEnd.Entities.User.Hibernate;

import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;
import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import java.security.MessageDigest; 
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException; 
import java.util.List;
import java.math.BigInteger;

import org.apache.commons.validator.routines.EmailValidator;
import java.util.List;




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
	public void addUser(String email, String password, String name) throws EmailExistsException,InvalidEmailException,IncorrectNameException,InvalidPasswordException{
		if(!EmailValidator.getInstance().isValid(email))
			throw new InvalidEmailException("email: " + email + " is invalid");
		if(getAll().stream().filter(u->((User)u).getEmail().equals(email)).count() > 0)
			throw new EmailExistsException("email: " + email + " already exsists");
		if(name == null || name.trim().isEmpty())
			throw new IncorrectNameException("incorrect name");
		if(password == null || password.trim().isEmpty())
			throw new InvalidPasswordException("invalid password");
	
		User user = new User(email,hash(password),name);
		insertTuple(user);
	}
	public String verifyUser(String email,String password){
		if(getAll().stream().filter(u->(((User)u).getEmail().equals(email) && ((User)u).getPassword().equals(hash(password)))).count() > 0){
			return SessionManager.getSessionManager().getNewSession(email,TIMEOUT);
		}
		else
			return null;
			
	}
	public void changePassword(String email, String newPassword) throws UserNotExistException,InvalidPasswordException{
		if(newPassword == null || newPassword.trim().isEmpty())
			throw new InvalidPasswordException("invalid password");	
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
	public void deleteUser(String email, String password) throws UserNotExistException{
		String token = verifyUser(email,password) ;
		if(token != null){
			List<User> users = getAll();
			boolean found = false;
			SessionManager.getSessionManager().terminateSession(token);
			for(User u: users){
				if(u.getEmail().equals(email)){
					delete(u);
					found = true;
					break;
				}
			}
			if(!found)
				throw new UserNotExistException("user details incorrect");
		}
		else{
				throw new UserNotExistException("user details incorrect");
		}
	}
	public void changeEmail(String oldEmail, String newEmail) throws UserNotExistException,EmailExistsException { 
		List<User> users = getAll();
		User user = null;
		for(User u:users){
			if(u.getEmail().equals(oldEmail))
				user = u;
			if(u.getEmail().equals(newEmail))
				throw new EmailExistsException("email: " + newEmail + " already exists");
		}
		if(user == null)
			throw new UserNotExistException("there is no user with email: " + oldEmail);
		user.setEmail(newEmail);
		update(user);
	}
	public void changeName(String email,String name) throws UserNotExistException,IncorrectNameException{
		List<User> users = getAll();
		User user = null;
		for(User u:users){
			if(u.getEmail().equals(email))
				user = u;
		}
		if(user == null)
			throw new UserNotExistException("there is no user with email: " + email);
		if(name == null || name.trim().isEmpty())
			throw new IncorrectNameException("incorrect name");
		user.setName(name);
		update(user);
	}
	public String getName(String email) throws UserNotExistException{
		List<User> users = getAll();
		for(User u:users){
			if(u.getEmail().equals(email))
				return u.getName();
		}
		throw new UserNotExistException("No such user");

	}
	public List<User> getUsers(){
		return getAll();
	}
	public boolean verifyEmail(String email){ 
		return (getAll().stream().filter(u->((User)u).getEmail().equals(email)).count() > 0);
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
