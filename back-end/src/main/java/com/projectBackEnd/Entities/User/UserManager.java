package main.java.com.projectBackEnd.Entities.User;

import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;
import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import java.security.MessageDigest; 
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException; 
import java.util.List;
import java.math.BigInteger;

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
	public void addUser(String username, String password) throws UsernameExistsException{
		if(getAll().stream().filter(u->((User)u).getUsername().equals(username)).count() > 0)
			throw new UsernameExistsException("username: " + username + "already exsists");
		User user = new User(username,hash(password));
		insertTuple(user);
	}
	public String verifyUser(String username,String password){
		if(getAll().stream().filter(u->(((User)u).getUsername().equals(username) && ((User)u).getPassword().equals(hash(password)))).count() > 0){
			return SessionManager.getSessionManager().getNewSession(username,TIMEOUT);
		}
		return null;
			
	}
	public void changePassword(String username, String newPassword) throws UserNotExistException{
		List<User> users = getAll();
		User user = null;
		for(User u:users){
			if(u.getUsername().equals(username))
				user = u;
		}
		if(user == null)
			throw new UserNotExistException("there is no user with username: " + username);
		user.setPassword(hash(newPassword));
		update(user);
		
	}
	public void deleteUser(String username) throws UserNotExistException{
		List<User> users = getAll();
		boolean found = false;
		for(User u: users){
			if(u.getUsername().equals(username)){
				delete(u);
				found = true;
				break;
			}
		}
		if(!found)
			throw new UserNotExistException("there is no user with username: " + username);
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
