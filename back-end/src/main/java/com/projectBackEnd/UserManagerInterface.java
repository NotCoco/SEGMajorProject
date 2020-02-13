package main.java.com.projectBackEnd;
public interface UserManagerInterface{
	public void addUser(String username, String password);
	public boolean verifyUser(String username, String password);
	public void changePassword(String username,String newPassword);
}
