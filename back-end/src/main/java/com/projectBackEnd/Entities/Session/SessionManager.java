package main.java.com.projectBackEnd.Entities.Session;
import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;

import java.util.List;
import java.util.ArrayList;
import java.sql.Timestamp;

/**
 * SessionManager defines methods for Session objects to interact with the database.
 * This class extends the EntityManager.
 *
 * Inspired by :
 * https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-annotations-example/
 */
public class SessionManager extends EntityManager implements SessionManagerInterface {

	private static SessionManagerInterface sessionManager;

	private SessionManager() {
		super();
		setSubclass(Session.class);
		HibernateUtility.addAnnotation(Session.class);
		sessionManager = this;
	}

	/**
	 * @return sessionManager ; if none has been defined, create a new SessionManager
	 */
	public static SessionManagerInterface getSessionManager() {
		if(sessionManager != null) return sessionManager;
		else return new SessionManager();
	}


	public String getNewSession(String email, int timeout){
		Session s = new Session(email,timeout);
		insertTuple(s);
		return s.getToken();
	}

	public String getEmail(String token) throws NoSessionException{
		List<Session> sessions = getAll();
		for (Session s: sessions) if(s.getToken().equals(token)) return s.getEmail();
		throw new NoSessionException("no such session");
	}



	public boolean verifySession(String token) {
		if(token == null || token.length() != 26)
			return false;
		List<Session> sessions = getAll();
		Session current = null;
		Timestamp now;
		for (Session s: sessions){
			if(s.getToken().equals(token)){
				now = new Timestamp(System.currentTimeMillis());
				if(s.getTimeout().after(now)){
					return true;
				}
				else{	
					delete(s);
					return false;	
				}
			}
			
		}
		return false;
	}



	public void terminateSession(String token) {
		List<Session> sessions = getAll();
		try{
			String email = getEmail(token);
			deleteAllPast(email); // delete the unused session for sake of performance
		}
		catch(NoSessionException e){ // when there is no session to delete 
		}
		//delete current session
		for (Session s: sessions){
			if(s.getToken().equals(token)){
				delete(s);
			}
		}
		
	}
	
	public void deleteAllPast(String email){
		List<Session> sessions = getAll();
		Timestamp now;
		for (Session s: sessions){
			if(s.getEmail().equals(email)){
				now = new Timestamp(System.currentTimeMillis());
				if(!s.getTimeout().after(now)){
					delete(s);
				}
			
			}
		}
	}
	

}
