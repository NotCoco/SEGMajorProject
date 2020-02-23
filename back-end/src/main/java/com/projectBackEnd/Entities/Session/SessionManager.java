package main.java.com.projectBackEnd.Entities.Session;
import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;


public class SessionManager extends EntityManager implements SessionManagerInterface{
	private static SessionManagerInterface sessionManager;
	private SessionManager(){
	        super();
        	setSubclass(Session.class);
        	HibernateUtility.addAnnotation(Session.class);
		sessionManager = this;
	}
	public static SessionManagerInterface getSessionManager(){
		if(sessionManager != null)
			return sessionManager;
		else
			return new SessionManager();
	}
	public boolean verifySession(String token){
		return false;
	}
	public String getNewSession(String username,int timeout){
		return "";
	}
	public void terminateSession(String token){

	}

}
