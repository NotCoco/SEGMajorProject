package test.java;

import main.java.com.projectBackEnd.*;
import main.java.com.projectBackEnd.Entities.User.User;
import main.java.com.projectBackEnd.Entities.User.UserManager;
import main.java.com.projectBackEnd.Entities.User.UserManagerInterface;
import main.java.com.projectBackEnd.Entities.User.UsernameExistsException;
import main.java.com.projectBackEnd.Entities.User.UserNotExistException;
import org.junit.*;
import java.security.MessageDigest; 
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException; 

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import java.math.BigInteger;

public class UserManagerTest {

    public static ConnectionLeakUtil connectionLeakUtil = null;    
	public static UserManagerInterface userManager = null;

	@BeforeClass
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
		userManager = UserManager.getUserManager();
        //connectionLeakUtil = new ConnectionLeakUtil();
    }


    @AfterClass
    public static void assertNoLeaks() {
        HibernateUtility.shutdown();
        //connectionLeakUtil.assertNoLeaks();
    }


    @After
    public void tearDown() {
		((EntityManager)userManager).deleteAll();
    }


	@Test
	public void testAddUser(){
		fill();
		try {
			userManager.addUser("user8","password8");
		}
		catch (UsernameExistsException e) {
			fail();
		}		
		List<User> users = (List<User>)((EntityManager)userManager).getAll();
		assertEquals(users.size(),8);
		assertEquals(1,users.stream().filter(u->(u.getUsername().equals("user8")
				&& u.getPassword().equals(hash("password8")))).count());
	}


	@Test(expected = UsernameExistsException.class)
	public void testAddExistingUser()throws UsernameExistsException{
		fill();
		userManager.addUser("user5","password5");
	}


	@Test
	public void testVerifyUser(){
		fill();
		assertNull(userManager.verifyUser("not_in","not in"));
		assertNotNull(userManager.verifyUser("user6","password6"));
	}


	@Test
	public void testChangePassword() throws UserNotExistException{
		fill();
		userManager.changePassword("user1","password10");
		List<User> users = (List<User>)((EntityManager)userManager).getAll();
		assertEquals(1,users.stream().filter(u->(u.getUsername().equals("user1")
				&& u.getPassword().equals(hash("password10")))).count());
		assertEquals(users.size(),7);
	}


	@Test(expected = UserNotExistException.class)
	public void testChangePasswordUserNotExsist() throws UserNotExistException{
		fill();
		userManager.changePassword("user20","password10");
	}


	@Test
	public void testDeleteUser(){
		fill();
		try {
			userManager.deleteUser("user1");
			List<User> users = (List<User>)((EntityManager)userManager).getAll();
			assertEquals(0,users.stream().filter(u->(u.getUsername().equals("user1")
					&& u.getPassword().equals(hash("password10")) == true)).count());
			assertEquals(users.size(),6);
		}
		catch (UserNotExistException e) {
			fail();
		}
	}


	@Test(expected = UserNotExistException.class)
	public void testDeleteNotExistingUser() throws UserNotExistException{
		fill();
		userManager.deleteUser("user0");
	}


	private String hash(String in) {

		try {
			MessageDigest alg = MessageDigest.getInstance("SHA-512"); 
			alg.reset();
			alg.update(in.getBytes(StandardCharsets.UTF_8));
			return String.format("%0128x", new BigInteger(1, alg.digest()));
		}
        catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e);
		}
	}


	private ArrayList<User> getTestUsers() {

		ArrayList<User> users = new ArrayList<User>();
		users.add(new User("user1",hash("password1")));
		users.add(new User("user2",hash("password2")));
		users.add(new User("user3",hash("password3")));
		users.add(new User("user4",hash("password4")));
		users.add(new User("user5",hash("password5")));
		users.add(new User("user6",hash("password6")));
		users.add(new User("user7",hash("password7")));

		return users;
	}


	private void fill(){
		for (User u : getTestUsers()){
			((EntityManager)userManager).insertTuple(u);
		}
	}

}
