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


	public boolean verifySession(String token) {

		List<Session> sessions = getAll();
		List<Session> correct = new ArrayList<Session>();

		for (Session s: sessions) if(s.getToken().equals(token)) correct.add(s);
	
		if (correct.size() == 1) {
			Session current = correct.get(0);
			Timestamp now = new Timestamp(System.currentTimeMillis());
			if(current.getTimeout().after(now)) return true;
			delete(current);		
		}

		return false;
	}

	public void terminateSession(String token) {
		if(getAll().stream().filter(s->((Session)s).getToken().equals(token)).count() == 1) {
			Session s = (Session)getByPrimaryKey(token);
			delete(s);	
		}	
	}

}
