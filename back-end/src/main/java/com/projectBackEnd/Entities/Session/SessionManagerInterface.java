package main.java.com.projectBackEnd.Entities.Session;

public interface SessionManagerInterface{
	public String getNewSession(String email,int timeout);
	public boolean verifySession(String token);
	public void terminateSession(String token);
}
	
