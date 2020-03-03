package main.java.com.projectBackEnd.Entities.Session;
import main.java.com.projectBackEnd.EntityManager;
import main.java.com.projectBackEnd.HibernateUtility;

import java.util.List;
import java.util.ArrayList;
import java.sql.Timestamp;
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
		List<Session> sessions = getAll();
		List<Session> correct = new ArrayList<Session>();
		for(Session s: sessions){
			if(s.getToken().equals(token))
				correct.add(s);
		}		
	
		if(correct.size() == 1){
			Session current = correct.get(0);
			Timestamp now = new Timestamp(System.currentTimeMillis());
			if(current.getTimeout().after(now))
				return true;
			delete(current);		
		}
		return false;
	}
	public String getNewSession(String username,int timeout){
		Session s = new Session(username,timeout);
		insertTuple(s);
		return s.getToken();
	}
	public void terminateSession(String token){
		if(getAll().stream().filter(s->((Session)s).getToken().equals(token)).count() == 1)
		{
			Session s = (Session)getByPrimaryKey(token);
			delete(s);	
		}	
	}

}
