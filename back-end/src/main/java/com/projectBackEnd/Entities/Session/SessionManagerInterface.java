package main.java.com.projectBackEnd.Entities.Session;

/**
 *  Methods used by SessionManager for database queries.
 */

public interface SessionManagerInterface {

	public String getNewSession(String username,int timeout);
	public boolean verifySession(String token);
	public void terminateSession(String token);

}
	
