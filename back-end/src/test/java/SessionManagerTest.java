package test.java;

import main.java.com.projectBackEnd.*;
import main.java.com.projectBackEnd.Entities.Session.Session;
import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;
import main.java.com.projectBackEnd.Entities.Session.NoSessionException;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
public class SessionManagerTest {

    public static ConnectionLeakUtil connectionLeakUtil = null;    
	public static SessionManagerInterface sessionManager = null;


	@BeforeAll
    public static void setUpDatabase() {
		HibernateUtility.setResource("testhibernate.cfg.xml");
		sessionManager = SessionManager.getSessionManager();
        connectionLeakUtil = new ConnectionLeakUtil();
    }


    @AfterAll
    public static void assertNoLeaks() {
        HibernateUtility.shutdown();
        connectionLeakUtil.assertNoLeaks();
    }

    @BeforeEach
    public void setUp() {
	((SessionManager)sessionManager).deleteAll();
    }


	@Test
	public void testGetNewSession() {
		fill();
		String token = sessionManager.getNewSession("1",100);
		List<Session> sessions = (List<Session>)((EntityManager)sessionManager).getAll();
		assertEquals(1,sessions.stream().filter(s->(s.getToken().equals(token))).count());
	}


	@Test
	public void testVerifySession() {
		fill();
		String token = sessionManager.getNewSession("1",100);
		List<Session> sessions = (List<Session>)((EntityManager)sessionManager).getAll();
		assertEquals(1,sessions.stream().filter(s->(s.getToken().equals(token))).count());
		assertEquals(0,sessions.stream().filter(s->(s.getToken().equals(""))).count());
		assertEquals(0,sessions.stream().filter(s->(s.getToken().equals("random string"))).count());
		assertEquals(0,sessions.stream().filter(s->(s.getToken().equals(null))).count());
	}

	@Test
	public void testTimeout() throws InterruptedException {
		fill();
		String token = sessionManager.getNewSession("1",2);
		List<Session> sessions = (List<Session>)((EntityManager)sessionManager).getAll();
		assertEquals(1,sessions.stream().filter(s->(s.getToken().equals(token))).count());
		Thread.sleep(2005);
		sessionManager.verifySession(token);
		sessions = (List<Session>)((EntityManager)sessionManager).getAll();
		assertEquals(0,sessions.stream().filter(s->(s.getToken().equals(token))).count());
	}


	@Test
	public void testTerminateSession() {
		String token = sessionManager.getNewSession("1",100);
		List<Session> sessions = (List<Session>)((EntityManager)sessionManager).getAll();
		assertEquals(1,sessions.size());
		assertEquals(token,sessions.get(0).getToken());
		sessionManager.terminateSession(token);
		assertEquals(0,((EntityManager)sessionManager).getAll().size());
	}

	@Test
	public void testAddGetEmail(){
		try{
			String token = sessionManager.getNewSession("email@email.com",100);
			assertEquals("email@email.com", sessionManager.getEmail(token));
			sessionManager.terminateSession(token);
		}
		catch(NoSessionException e){
			fail();
		}	
	}
	@Test
	public void testGetEmailNotExistEmpty() throws NoSessionException{
		assertThrows(NoSessionException.class,() -> {sessionManager.getEmail("");});
	}
	@Test
	public void testGetEmailNotExistIncorrect() throws NoSessionException{

		assertThrows(NoSessionException.class,() -> {sessionManager.getEmail("very incorrect token that does not work");});
	}
	@Test
	public void testGetEmailNotExistNull() throws NoSessionException{
		assertThrows(NoSessionException.class,() -> {sessionManager.getEmail(null);});
	}



	private ArrayList<Session> getTestSessions() {
		ArrayList<Session> sessions = new ArrayList<Session>();
		sessions.add(new Session("1",100));
		sessions.add(new Session("2",100));
		sessions.add(new Session("3",100));

		return sessions;
	}


	private void fill() {
		for(Session s : getTestSessions()){
			((EntityManager)sessionManager).insertTuple(s);
		}
	}


}
