package test.java;

import main.java.com.projectBackEnd.*;
import main.java.com.projectBackEnd.Entities.ResetLinks.*;
import main.java.com.projectBackEnd.Entities.User.Hibernate.UserManager;
import main.java.com.projectBackEnd.Entities.User.Hibernate.UserManagerInterface;
import main.java.com.projectBackEnd.Entities.User.Hibernate.EmailExistsException;
import main.java.com.projectBackEnd.Entities.User.Hibernate.InvalidEmailException;
import main.java.com.projectBackEnd.Entities.User.Hibernate.IncorrectNameException;
import main.java.com.projectBackEnd.Entities.User.Hibernate.InvalidPasswordException;
import org.junit.jupiter.api.*;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class to extensively unit test interactions between the reset link manager and the link entity in the database.
 */
public class TestResetLinkManager{
    public static ConnectionLeakUtil connectionLeakUtil = null;
    public static ResetLinkManagerInterface linkManager = null;
    public static UserManagerInterface userManager = null;

    /**
     * Prior to running, database information is set and a singleton manager is created for testing.
     */
    @BeforeAll
    public static void setUpDatabase() {
            HibernateUtility.setResource("testhibernate.cfg.xml");
            linkManager = ResetLinkManager.getResetLinkManager();
    userManager = UserManager.getUserManager();
            //connectionLeakUtil = new ConnectionLeakUtil();

    }

    /**
     * After the test, the factory is shut down and the LeakUtil can tell us whether any connections leaked.
     */
    @AfterAll
    public static void assertNoLeaks() {
            HibernateUtility.shutdown();
            //connectionLeakUtil.assertNoLeaks();
    }

    /**
     * Prior to each test, we'll delete all the users in the users table.
     */
    @BeforeEach
    public void setUp() {
        ((EntityManager)linkManager).deleteAll();
        ((EntityManager)userManager).deleteAll();
    }

    /**
     * Tests that the manager is able to create a new token for a link being reset
     */
    @Test
    public void testCreate(){
        fill();
        try {
            String a = linkManager.create("test@test.com");
            assertNotEquals("",a);
            assertNotNull(a);
        } catch (EmailNotExistException e){
            fail();
        }
    }

    /**
     * Attempts to create a token for an email which does not exist, expects and exception to be thrown
     * @throws EmailNotExistException The exception to be thrown
     */
    @Test
    public void testCreateEmailNotExist() throws EmailNotExistException{
        fill();
        assertThrows(EmailNotExistException.class,() -> {linkManager.create("test2@test.com");});
    }

    /**
     * Tests that the manager is able to retrieve an email via it's token, also tests that retrieval attempts
     * with an invalid token are unsuccessful, expects success
     */
    @Test
    public void testGetEmail(){
        fill();
        try {
            String a = linkManager.create("test@test.com");
            assertEquals("test@test.com",linkManager.getEmail(a));
            assertNull(linkManager.getEmail(a+1));
            assertNull(linkManager.getEmail(a+"c"));
            assertNull(linkManager.getEmail(""));
            assertNull(linkManager.getEmail("123"));
            assertNull(linkManager.getEmail(null));
        } catch(EmailNotExistException e){
            fail();
        }
    }

    /**
     * Tests that the manager is able to dictate whether link exists depending on the token provided, invalid tokens should
     * return false, expects success
     */
    @Test
    public void testExist(){
        fill();
        try {
            String a = linkManager.create("test@test.com");
            assertTrue(linkManager.exist(a));
            assertFalse(linkManager.exist(a+1));
            assertFalse(linkManager.exist(""));
            assertFalse(linkManager.exist(null));
            assertFalse(linkManager.exist("refwubhybuhwsfw"));
            assertFalse(linkManager.exist("fds"));
        } catch(EmailNotExistException e){
            fail();
        }
    }

    /**
     * Quality of life method for filling the database with users to initialise links with
     */
    private void fill(){
        try{
            userManager.addUser("test@test.com","pass","name");
            userManager.addUser("test1@test.com","pass","name");
        } catch(EmailExistsException|InvalidEmailException|IncorrectNameException|InvalidPasswordException e){
            e.printStackTrace();
            fail();
        }
    }

}

		
