package test.java;

import main.java.com.projectBackEnd.Entities.User.Hibernate.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import java.math.BigInteger;

import main.java.com.projectBackEnd.HibernateUtility;
import main.java.com.projectBackEnd.EntityManager;

public class UserManagerTest{
	public static ConnectionLeakUtil connectionLeakUtil = null;
	public static UserManagerInterface userManager = null;

	@BeforeAll
    public static void setUpDatabase() {

		HibernateUtility.setResource("testhibernate.cfg.xml");
		userManager = UserManager.getUserManager();
		connectionLeakUtil = new ConnectionLeakUtil();

	}

	@AfterAll
	public static void assertNoLeaks() {
		HibernateUtility.shutdown();
		connectionLeakUtil.assertNoLeaks();
	}

	@BeforeEach
	public void setUp() {
		((EntityManager)userManager).deleteAll();
	}


	//replace with parametric maybe?
	@Test
	public void testAddUserInvalidEmail1() throws InvalidEmailException{


		assertThrows(InvalidEmailException.class,() -> {userManager.addUser("email","password5","name");});
		
	}

	@Test
	public void testAddUserInvalidEmail2() {

		assertThrows(InvalidEmailException.class,() -> {userManager.addUser("ema il@email.com","password5","name");});
		
	}
	@Test
	public void testAddUserInvalidEmail3() {

		assertThrows(InvalidEmailException.class,() -> {userManager.addUser("email@email.","password5","name");});
		
	}
	@Test
	public void testAddUserInvalidEmail4() {

		assertThrows(InvalidEmailException.class,() -> {userManager.addUser("@email.com","password5","name");});
		

	}
	@Test
	public void testAddUserInvalidEmail5() {
	

		assertThrows(InvalidEmailException.class,() -> {userManager.addUser("@email.","password5","name");});
		
	}

	@Test
	public void testAddUserIncorrectNameEmpty() {
		
		assertThrows(InvalidEmailException.class,() -> {userManager.addUser("@email.","password5","");});
		
	}
	@Test
	public void testAddUserIncorrectNameNull() throws IncorrectNameException{


		assertThrows(InvalidEmailException.class,() -> {userManager.addUser("@email.","password5",null);});
		
	}

	@Test
	public void testAddUser(){
		fill();
		try{
			userManager.addUser("user8@email.com","password8","name");
		}
		catch(EmailExistsException|InvalidEmailException|IncorrectNameException e){
			fail();
		}
		List<User> users = (List<User>)((EntityManager)userManager).getAll();
		assertEquals(users.size(),8);
		assertEquals(1,users.stream().filter(u->(u.getEmail().equals("user8@email.com") && u.getPassword().equals(hash("password8")))).count());
	}
	@Test
	public void testAddExistingUser()throws EmailExistsException{
		fill();
		assertThrows(EmailExistsException.class,() -> {userManager.addUser("user5@email.com","password5","name");});
		
	}
	@Test
	public void testVerifyUser(){
		fill();
		assertNull(userManager.verifyUser("not_in","not in"));
		assertNotNull(userManager.verifyUser("user6@email.com","password6"));
		assertNotNull(userManager.verifyUser("user6@email.com","password6"));
	}
	@Test
	public void testChangePassword() throws UserNotExistException{
		fill();
		userManager.changePassword("user1@email.com","password10");
		List<User> users = (List<User>)((EntityManager)userManager).getAll();
		assertEquals(1,users.stream().filter(u->(u.getEmail().equals("user1@email.com") && u.getPassword().equals(hash("password10")))).count());
		assertEquals(users.size(),7);
	}
	@Test
	public void testChangePasswordUserNotExsist() throws UserNotExistException{
		fill();
		userManager.changePassword("user20@email.com","password10");
		assertThrows(UserNotExistException.class,() -> {userManager.changePassword("user20@email.com","password10");});
	}
	@Test
	public void testDeleteUser(){
		fill();
		try{
			userManager.deleteUser("user1@email.com","password1");
			List<User> users = (List<User>)((EntityManager)userManager).getAll();
			assertEquals(0,users.stream().filter(u->(u.getEmail().equals("user1@email.com") && u.getPassword().equals(hash("password1")) == true)).count());
			assertEquals(users.size(),6);
		}
		catch(UserNotExistException e){
			fail();
		}
	}

	@Test
	public void testDeleteNotExistingUser() throws UserNotExistException{
		fill();

		assertThrows(UserNotExistException.class,() -> {userManager.deleteUser("user0@email.com","password0");});

	}
	@Test
	public void testDeleteWrongPasswordUser() throws UserNotExistException{
		fill();

		assertThrows(UserNotExistException.class,() -> {userManager.deleteUser("user1@email.com","password0");});

	}
	@Test
	public void testChangeEmailBadEmail() throws UserNotExistException,EmailExistsException{
		fill();

		assertThrows(UserNotExistException.class,() -> {userManager.changeEmail("user@email.com","user19@email.com");});

	}
	@Test
	public void testChangeEmailExisitngEmail() throws EmailExistsException,UserNotExistException{
		fill();
		
		userManager.changeEmail("user1@email.com","user5@email.com");
		assertThrows(EmailExistsException.class,() -> {userManager.changeEmail("user1@email.com","user5@email.com");});

	}
	@Test
	public void testChangeEmail(){
		fill();
		try{
		userManager.changeEmail("user1@email.com","user10@email.com");
		assertNotNull(userManager.verifyUser("user10@email.com","password1"));
		}
		catch(Exception e){
			fail();
		}	
	}
	@Test	
	public void testVerifyEmail(){
		fill();
		
		assertTrue(userManager.verifyEmail("user1@email.com"));
		assertFalse(userManager.verifyEmail("user0@email.com"));
		
	}

	@Test
	public void testChangeGetName() throws IncorrectNameException, UserNotExistException{
		fill();
		assertEquals("name",userManager.getName("user1@email.com"));
		userManager.changeName("user1@email.com", "new name");
		assertEquals("new name",userManager.getName("user1@email.com"));
	}
	
	@Test
	public void testChangeGetNameIncorrectUser() throws IncorrectNameException, UserNotExistException{

		assertThrows(UserNotExistException.class,() -> {userManager.changeName("user1@email.com", "new name");});
	}

	@Test
	public void testIncorrectNameEmpty() throws IncorrectNameException, UserNotExistException{
		fill();
		assertEquals("name",userManager.getName("user1@email.com"));
		assertThrows(UserNotExistException.class,() -> {userManager.changeName("user1@email.com", "");});
	}

	@Test
	public void testIncorrectNameNull() throws IncorrectNameException, UserNotExistException{
		fill();
		assertEquals("name",userManager.getName("user1@email.com"));
		assertThrows(UserNotExistException.class,() -> {userManager.changeName("user1@email.com", null);});

	}

	private String hash(String in){
		try{
			MessageDigest alg = MessageDigest.getInstance("SHA-512");
			alg.reset();
			alg.update(in.getBytes(StandardCharsets.UTF_8));
			return String.format("%0128x", new BigInteger(1, alg.digest()));
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);

		}

	}
	private ArrayList<User> getTestUsers(){
		ArrayList<User> users = new ArrayList<User>();
		users.add(new User("user1@email.com",hash("password1"),"name"));
		users.add(new User("user2@email.com",hash("password2"),"name"));
		users.add(new User("user3@email.com",hash("password3"),"name"));
		users.add(new User("user4@email.com",hash("password4"),"name"));
		users.add(new User("user5@email.com",hash("password5"),"name"));
		users.add(new User("user6@email.com",hash("password6"),"name"));
		users.add(new User("user7@email.com",hash("password7"),"name"));


		return users;

	}
	private void fill(){
		for(User u : getTestUsers()){
			((EntityManager)userManager).insertTuple(u);
		}
	}
}
