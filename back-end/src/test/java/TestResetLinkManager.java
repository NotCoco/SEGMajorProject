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
class TestResetLinkManager{
    private static ConnectionLeakUtil connectionLeakUtil = null;
    private static ResetLinkManagerInterface linkManager = null;
    private static UserManagerInterface userManager = null;

    /**
     * Prior to running, database information is set and a singleton manager is created for testing.
     */
    @BeforeAll
    static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
        linkManager = ResetLinkManager.getResetLinkManager();
        userManager = UserManager.getUserManager();
        connectionLeakUtil = new ConnectionLeakUtil();
    }

    /**
     * After the test, the factory is shut down and the LeakUtil can tell us whether any connections leaked.
     */
    @AfterAll
    static void assertNoLeaks() {
            HibernateUtility.shutdown();
            connectionLeakUtil.assertNoLeaks();
    }

    /**
     * Prior to each test, we'll delete all the users in the users table.
     */
    @BeforeEach
    void setUp() {
        ((EntityManager)linkManager).deleteAll();
        ((EntityManager)userManager).deleteAll();
    }

    /**
     * Test the copy method in links
     */
    @Test
    void testLinkCopy() {
        Link link1 = Link("email");
        Link link2 = Link("email2");
        link1.copy(link2);
        assertThat(link1, samePropertyValuesAs(link2));
    }

    /**
     * Tests that the manager is able to create a new token for a link being reset
     */
    @Test
    void testCreate(){
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
    void testCreateEmailNotExist() throws EmailNotExistException{
        fill();
        assertThrows(EmailNotExistException.class,() -> {linkManager.create("test2@test.com");});
    }

    /**
     * Tests that the manager is able to retrieve an email via it's token, also tests that retrieval attempts
     * with an invalid token are unsuccessful, expects success
     */
    @Test
    void testGetEmail(){
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
    void testExist(){
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

		
