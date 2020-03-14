package test.java;

import main.java.com.projectBackEnd.*;
import main.java.com.projectBackEnd.Entities.Session.Session;
import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.ArrayList;
import java.util.List;
public class SessionManagerTest{
    public static ConnectionLeakUtil connectionLeakUtil = null;    
	public static SessionManagerInterface sessionManager = null;
	@BeforeClass
    public static void setUpDatabase() {
        HibernateUtility.setResource("test/resources/testhibernate.cfg.xml");
		sessionManager = SessionManager.getSessionManager();
        //connectionLeakUtil = new ConnectionLeakUtil();

    }

    @AfterClass
    public static void assertNoLeaks() {
        HibernateUtility.shutdown();
        //connectionLeakUtil.assertNoLeaks();
    }
    @Before
    public void setUp() {
	((SessionManager)sessionManager).deleteAll();
    }


	@Test
	public void testGetNewSession(){
		fill();
		String token = sessionManager.getNewSession("1",100);
		List<Session> sessions = (List<Session>)((EntityManager)sessionManager).getAll();
		assertEquals(1,sessions.stream().filter(s->(s.getToken().equals(token))).count());
	}
	@Test
	public void testVerifySession(){
		fill();
		String token = sessionManager.getNewSession("1",100);
		List<Session> sessions = (List<Session>)((EntityManager)sessionManager).getAll();
		assertEquals(1,sessions.stream().filter(s->(s.getToken().equals(token))).count());
		assertEquals(0,sessions.stream().filter(s->(s.getToken().equals(""))).count());
		assertEquals(0,sessions.stream().filter(s->(s.getToken().equals("random string"))).count());
		assertEquals(0,sessions.stream().filter(s->(s.getToken().equals(null))).count());
	}

	@Test
	public void testTimeout() throws InterruptedException{
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
	public void testTerminateSession(){
		String token = sessionManager.getNewSession("1",100);
		List<Session> sessions = (List<Session>)((EntityManager)sessionManager).getAll();
		assertEquals(1,sessions.size());
		assertEquals(token,sessions.get(0).getToken());
		sessionManager.terminateSession(token);
		assertEquals(0,((EntityManager)sessionManager).getAll().size());
	}
	private ArrayList<Session> getTestSessions(){
		ArrayList<Session> sessions = new ArrayList<Session>();
		sessions.add(new Session("1",100));
		sessions.add(new Session("2",100));
		sessions.add(new Session("3",100));

		return sessions;
	}
	private void fill(){
		for(Session s : getTestSessions()){
			((EntityManager)sessionManager).insertTuple(s);
		}
	}

}
