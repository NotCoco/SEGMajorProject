package test.java;

import main.java.com.projectBackEnd.*;
import main.java.com.projectBackEnd.Entities.User.User;
import main.java.com.projectBackEnd.Entities.User.UserManager;
import main.java.com.projectBackEnd.Entities.User.UserManagerInterface;
import main.java.com.projectBackEnd.Entities.User.EmailExistsException;
import main.java.com.projectBackEnd.Entities.User.UserNotExistException;
import main.java.com.projectBackEnd.Entities.User.InvalidEmailException;
import org.junit.*;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import java.math.BigInteger;

public class UserManagerTest{
	public static ConnectionLeakUtil connectionLeakUtil = null;
	public static UserManagerInterface userManager = null;

	@BeforeClass
    public static void setUpDatabase() {
		HibernateUtility.setResource("testhibernate.cfg.xml");
		userManager = UserManager.getUserManager();
		connectionLeakUtil = new ConnectionLeakUtil();

	}

	@AfterClass
	public static void assertNoLeaks() {
		HibernateUtility.shutdown();
		connectionLeakUtil.assertNoLeaks();
	}

	@Before
	public void setUp() {
		((EntityManager)userManager).deleteAll();
	}


	//replace with parametric maybe?
	@Test(expected = InvalidEmailException.class)
	public void testAddUserInvalidEmail1() throws InvalidEmailException{
		try{
			userManager.addUser("email","password5");
		}
		catch(EmailExistsException e){
			fail();
		}
	}

	@Test(expected = InvalidEmailException.class)
	public void testAddUserInvalidEmail2() throws InvalidEmailException{
		try{
			userManager.addUser("ema il@email.com","password5");
		}
		catch(EmailExistsException e){
			fail();
		}
	}
	@Test(expected = InvalidEmailException.class)
	public void testAddUserInvalidEmail3() throws InvalidEmailException{
		try{
			userManager.addUser("email@email.","password5");
		}
		catch(EmailExistsException e){
			fail();
		}
	}
	@Test(expected = InvalidEmailException.class)
	public void testAddUserInvalidEmail4() throws InvalidEmailException{
		try{
			userManager.addUser("@email.com","password5");
		}
		catch(EmailExistsException e){
			fail();
		}
	}
	@Test(expected = InvalidEmailException.class)
	public void testAddUserInvalidEmail5() throws InvalidEmailException{
		try{
			userManager.addUser("@email.","password5");
		}
		catch(EmailExistsException e){
			fail();
		}
	}
	@Test
	public void testAddUser(){
		fill();
		try{
			userManager.addUser("user8@email.com","password8");
		}
		catch(EmailExistsException|InvalidEmailException e){
			fail();
		}
		List<User> users = (List<User>)((EntityManager)userManager).getAll();
		assertEquals(users.size(),8);
		assertEquals(1,users.stream().filter(u->(u.getEmail().equals("user8@email.com") && u.getPassword().equals(hash("password8")))).count());
	}
	@Test(expected = EmailExistsException.class)
	public void testAddExistingUser()throws EmailExistsException{
		fill();
		try{
			userManager.addUser("user5@email.com","password5");
		}
		catch(InvalidEmailException e){
			fail();
		}
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
	@Test(expected = UserNotExistException.class)
	public void testChangePasswordUserNotExsist() throws UserNotExistException{
		fill();
		userManager.changePassword("user20@email.com","password10");
	}
	@Test
	public void testDeleteUser(){
		fill();
		try{
			userManager.deleteUser("user1@email.com");
			List<User> users = (List<User>)((EntityManager)userManager).getAll();
			assertEquals(0,users.stream().filter(u->(u.getEmail().equals("user1@email.com") && u.getPassword().equals(hash("password10")) == true)).count());
			assertEquals(users.size(),6);
		}
		catch(UserNotExistException e){
			fail();
		}
	}

	@Test(expected = UserNotExistException.class)
	public void testDeleteNotExistingUser() throws UserNotExistException{
		fill();
		userManager.deleteUser("user0@email.com");

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
		users.add(new User("user1@email.com",hash("password1")));
		users.add(new User("user2@email.com",hash("password2")));
		users.add(new User("user3@email.com",hash("password3")));
		users.add(new User("user4@email.com",hash("password4")));
		users.add(new User("user5@email.com",hash("password5")));
		users.add(new User("user6@email.com",hash("password6")));
		users.add(new User("user7@email.com",hash("password7")));


		return users;

	}
	private void fill(){
		for(User u : getTestUsers()){
			((EntityManager)userManager).insertTuple(u);
		}
	}
}