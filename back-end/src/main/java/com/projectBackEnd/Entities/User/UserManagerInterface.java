package main.java.com.projectBackEnd.Entities.User;


import main.java.com.projectBackEnd.TableEntity;

import java.io.Serializable;


public interface UserManagerInterface{
	public void addUser(String username,String password) throws UsernameExistsException;
	public String verifyUser(String username,String password);
	public void changePassword(String username, String newPassword);
	public void deleteUser(String username) throws UserNotExistException;
}

