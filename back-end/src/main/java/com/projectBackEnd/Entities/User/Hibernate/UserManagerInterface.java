package main.java.com.projectBackEnd.Entities.User.Hibernate;

import java.io.Serializable;
import java.util.List;

/**
 *  Methods used by UserManager for database queries.
 */
public interface UserManagerInterface {

	public void addUser(String email,String password, String name)
			throws EmailExistsException,InvalidEmailException,IncorrectNameException,InvalidPasswordException;

	public String verifyUser(String email,String password);

	public boolean verifyEmail(String email);

	public void changePassword(String email, String newPassword)
			throws UserNotExistException,InvalidPasswordException;

	public void changeEmail(String oldEmail, String newEmail)
			throws UserNotExistException,EmailExistsException;

	public void changeName(String email, String name)
			throws UserNotExistException, IncorrectNameException;

	public String getName(String email)
			throws UserNotExistException;

	public List<User> getUsers();

	public void deleteUser(String email, String password)
			throws UserNotExistException;

}

