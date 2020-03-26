package test.java;

import main.java.com.projectBackEnd.*;
import main.java.com.projectBackEnd.Entities.ResetLinks.*;
import main.java.com.projectBackEnd.Entities.User.UserManager;
import main.java.com.projectBackEnd.Entities.User.UserManagerInterface;
import main.java.com.projectBackEnd.Entities.User.EmailExistsException;
import main.java.com.projectBackEnd.Entities.User.InvalidEmailException;
import org.junit.*;
import static org.junit.Assert.*;


public class TestResetLinkManager{
        public static ConnectionLeakUtil connectionLeakUtil = null;
        public static ResetLinkManagerInterface linkManager = null;
        public static UserManagerInterface userManager = null;;
        @BeforeClass
        public static void setUpDatabase() {
                HibernateUtility.setResource("testhibernate.cfg.xml");
                linkManager = ResetLinkManager.getResetLinkManager();
		userManager = UserManager.getUserManager();
                //connectionLeakUtil = new ConnectionLeakUtil();

        }

        @AfterClass
        public static void assertNoLeaks() {
                HibernateUtility.shutdown();
                //connectionLeakUtil.assertNoLeaks();
        }

        @Before
        public void setUp() {
                ((EntityManager)linkManager).deleteAll();                
		((EntityManager)userManager).deleteAll();
        }
        @Test
        public void testCreate(){
                fill();
		try{
			String a = linkManager.create("test@test.com");
			assertNotEquals("",a);
			assertNotNull(a);
        	}
		catch(EmailNotExistException e){
			fail();
		}
	}
        @Test(expected = EmailNotExistException.class)
        public void testCreateEmailNotExist() throws EmailNotExistException{
                fill();
		String a = linkManager.create("test2@test.com");
        }
        @Test
        public void testGetEmail(){
                fill();
		try{
			String a = linkManager.create("test@test.com");
			assertEquals("test@test.com",linkManager.getEmail(a));
			assertNull(linkManager.getEmail(a+1));
			assertNull(linkManager.getEmail(a+"c"));
			assertNull(linkManager.getEmail(""));
			assertNull(linkManager.getEmail("123"));
			assertNull(linkManager.getEmail(null));
        	}
		catch(EmailNotExistException e){
			fail();
		}
        }
       @Test
        public void testExist(){
                fill();
		try{
			String a = linkManager.create("test@test.com");
			assertTrue(linkManager.exist(a));
			assertFalse(linkManager.exist(a+1));
			assertFalse(linkManager.exist(""));
			assertFalse(linkManager.exist(null));
			assertFalse(linkManager.exist("refwubhybuhwsfw"));
			assertFalse(linkManager.exist("fds"));
        	}
		catch(EmailNotExistException e){
			fail();
		}
        }
        public void fill(){
                try{
                        userManager.addUser("test@test.com","pass","name");
                        userManager.addUser("test1@test.com","pass","name");
                }
                catch(EmailExistsException|InvalidEmailException e){
			System.out.println(e);                        
			fail();
                }
        }

}

		
