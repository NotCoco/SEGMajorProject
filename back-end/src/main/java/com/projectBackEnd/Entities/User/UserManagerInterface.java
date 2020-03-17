package main.java.com.projectBackEnd.Entities.User;

import java.io.Serializable;


public interface UserManagerInterface{
	public void addUser(String email,String password) throws EmailExistsException,InvalidEmailException;
	public String verifyUser(String email,String password);
	public void changePassword(String email, String newPassword) throws UserNotExistException;
	public void deleteUser(String email) throws UserNotExistException;
	public boolean verifyEmail(String email);
	public void changeEmail(String oldEmail, String newEmail) throws UserNotExistException,EmailExistsException;
}

