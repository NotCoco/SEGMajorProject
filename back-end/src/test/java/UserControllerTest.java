package test.java;


import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;

import main.java.com.projectBackEnd.Entities.User.Hibernate.*;
import main.java.com.projectBackEnd.Entities.User.Micronaut.*;
import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.Session.SessionManagerInterface;
import main.java.com.projectBackEnd.Entities.ResetLinks.*;


import javax.inject.Inject;


import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


import main.java.com.projectBackEnd.HibernateUtility;
import main.java.com.projectBackEnd.EntityManager;

@MicronautTest
public class UserControllerTest{
    

	
	@Inject
    @Client("/")
    HttpClient client;
	private static final UserManagerInterface userManager = UserManager.getUserManager();
	private static final ResetLinkManagerInterface linkManager = ResetLinkManager.getResetLinkManager();	
	private static final SessionManagerInterface sessionManager = SessionManager.getSessionManager();
    @BeforeAll
    public static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");
    }

    @AfterAll
    public static void closeDatabase() {
        HibernateUtility.shutdown();
    }

    @BeforeEach
    public void setUp() {
		((EntityManager)userManager).deleteAll();
		((EntityManager)linkManager).deleteAll();
		((EntityManager)sessionManager).deleteAll();
    }
	@Test
	public void testCreateUser(){
		assertEquals(HttpStatus.CREATED,addUser("username@mail.com","password","na me").getStatus());
		String token1 = userManager.verifyUser("username@mail.com","password");
		assertNotNull(token1);
		sessionManager.terminateSession(token1);

		assertEquals(HttpStatus.CREATED,addUser("name@mail.com","passw@/-ord","name").getStatus());
		String token2 = userManager.verifyUser("name@mail.com","passw@/-ord");
		assertNotNull(token2);
		sessionManager.terminateSession(token2);

		assertEquals(HttpStatus.CREATED,addUser("nam123e@mail.com","pas321sw@/-ord","na-me").getStatus());
		String token3 = userManager.verifyUser("nam123e@mail.com","pas321sw@/-ord");
		assertNotNull(token3);
		sessionManager.terminateSession(token3);	
	
		assertEquals(HttpStatus.CREATED,addUser("nam123e2@mail.com","pa  s321sw@/-ord","nam e").getStatus());
		String token4 = userManager.verifyUser("nam123e2@mail.com","pa  s321sw@/-ord");
		assertNotNull(token4);
		sessionManager.terminateSession(token4);

	}

	@Test
	public void testCreateUserIncorrectPassword(){
		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class,()->{
			addUser("name@name.pl","","name");
		});
		assertEquals(HttpStatus.BAD_REQUEST, thrown1.getStatus());
	
		HttpClientResponseException thrown2 = assertThrows(HttpClientResponseException.class,()->{
			addUser("name@name.pl",null,"name");
		});
		assertEquals(HttpStatus.BAD_REQUEST, thrown2.getStatus());

		HttpClientResponseException thrown3 = assertThrows(HttpClientResponseException.class,()->{
			addUser("name@name.pl","	 ","name");
		});
		assertEquals(HttpStatus.BAD_REQUEST, thrown3.getStatus());

     	HttpClientResponseException thrown4 = assertThrows(HttpClientResponseException.class, () -> {
            	client.toBlocking().retrieve(HttpRequest.POST("/user/create",new UserBody("name@name.pl","","lmao")));
        });
		assertEquals(HttpStatus.BAD_REQUEST, thrown4.getStatus());
		assertEquals("invalid password", thrown4.getResponse().getBody().get());
	}

	
	@Test
	public void testCreateUserIncorrectName(){
		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class,()->{
			addUser("name@name.pl","password","");
		});
		assertEquals(HttpStatus.BAD_REQUEST, thrown1.getStatus());


		HttpClientResponseException thrown2 = assertThrows(HttpClientResponseException.class,()->{
			addUser("name@name.pl","password",null);
		});
		assertEquals(HttpStatus.BAD_REQUEST, thrown2.getStatus());

		HttpClientResponseException thrown4 = assertThrows(HttpClientResponseException.class,()->{
			addUser("name@name.pl","password"," 	");
		});
		assertEquals(HttpStatus.BAD_REQUEST, thrown4.getStatus());

     	HttpClientResponseException thrown3 = assertThrows(HttpClientResponseException.class, () -> {
            	client.toBlocking().retrieve(HttpRequest.POST("/user/create",new UserBody("name@name.pl","password",null)));
        });
		assertEquals(HttpStatus.BAD_REQUEST, thrown3.getStatus());
		assertEquals("invalid name", thrown3.getResponse().getBody().get());

	}


	@Test
	public void testCreateUserIncorrectEmail(){
		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class,()->{
			addUser("name","password","name");
		});
		assertEquals(HttpStatus.BAD_REQUEST, thrown1.getStatus());
		HttpClientResponseException thrown2 = assertThrows(HttpClientResponseException.class,()->{
			addUser("name@","password","name");
		});
		assertEquals(HttpStatus.BAD_REQUEST, thrown2.getStatus());
		HttpClientResponseException thrown3 = assertThrows(HttpClientResponseException.class,()->{
			addUser("@name","password","name");
		});
		assertEquals(HttpStatus.BAD_REQUEST, thrown3.getStatus());
		HttpClientResponseException thrown4 = assertThrows(HttpClientResponseException.class,()->{
			addUser("@","password","name");
		});
		assertEquals(HttpStatus.BAD_REQUEST, thrown4.getStatus());
		HttpClientResponseException thrown5 = assertThrows(HttpClientResponseException.class,()->{
			addUser("@","password","name");
		});
		assertEquals(HttpStatus.BAD_REQUEST, thrown5.getStatus());
		HttpClientResponseException thrown6 = assertThrows(HttpClientResponseException.class,()->{
			addUser("","password","name");
		});	
		assertEquals(HttpStatus.BAD_REQUEST, thrown6.getStatus());
		HttpClientResponseException thrown7 = assertThrows(HttpClientResponseException.class,()->{
			addUser(null,"password","name");
		});
		assertEquals(HttpStatus.BAD_REQUEST, thrown7.getStatus());


        HttpClientResponseException thrown8 = assertThrows(HttpClientResponseException.class, () -> {
            	client.toBlocking().retrieve(HttpRequest.POST("/user/create",new UserBody(null,"pas1234//--+sword","name")));
        });
		assertEquals(HttpStatus.BAD_REQUEST, thrown8.getStatus());
		assertEquals("invalid email address", thrown8.getResponse().getBody().get());

	}

	@Test
	public void testCreateDuplicateUser(){
		try{
			userManager.addUser("username@mail.com","password","name");
		}
		catch(InvalidEmailException|EmailExistsException|IncorrectNameException|InvalidPasswordException e){
			fail();
		}
		String token = userManager.verifyUser("username@mail.com","password");
		assertNotNull(token);
		sessionManager.terminateSession(token);
	
		

        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            	client.toBlocking().retrieve(HttpRequest.POST("/user/create",new UserBody("username@mail.com","password","name")));
        });
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
		assertEquals("user already exsits", thrown.getResponse().getBody().get());
	}

	@Test
	public void TestLoginCorrect(){
		assertEquals(HttpStatus.CREATED, addUser("mail@mail.com", "pass", "name").getStatus());
		HttpResponse<String> response = login("mail@mail.com", "pass");
		assertEquals(HttpStatus.OK, response.getStatus());

		assertEquals(HttpStatus.CREATED, addUser("mail1@mail.com", "pa  s321sw@/-ord", "name").getStatus());
		String response1 = getToken("mail1@mail.com", "pa  s321sw@/-ord");
		assertNotNull(response1);

		assertEquals(HttpStatus.CREATED, addUser("mail2@mail.com", "pas321sw@/-ord", "name").getStatus());
		String response2 = getToken("mail2@mail.com", "pas321sw@/-ord");
		assertNotNull(response2);

		((EntityManager)sessionManager).deleteAll();
	}

	@Test
	public void TestLoginIncorrect(){
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.POST("/user/login",new UserBody(null,null,"name")));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown.getStatus());

        HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.POST("/user/login",new UserBody("","","name")));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown1.getStatus());

        HttpClientResponseException thrown2 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.POST("/user/login",new UserBody(null,"pass","name")));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown2.getStatus());

	    HttpClientResponseException thrown3 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.POST("/user/login",new UserBody("pass",null,"name")));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown3.getStatus());

        HttpClientResponseException thrown4 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.POST("/user/login",new UserBody("","pass","name")));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown4.getStatus());

	    HttpClientResponseException thrown5 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.POST("/user/login",new UserBody("pass","","name")));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown5.getStatus());

	    HttpClientResponseException thrown6 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.POST("/user/login",new UserBody("email@email.com","password","name")));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown6.getStatus());

	}
	@Test
	public void testLogout(){
			addUser("admin@admin.com","admin","not me");
			String token = getToken("admin@admin.com","admin");
			assertEquals(HttpStatus.OK, client.toBlocking().exchange(HttpRequest.GET("/user/logout").header("X-API-Key", token)).getStatus());
			assertFalse(sessionManager.verifySession(token));
	}
 	@Test
	public void testDeleteCorrect(){
			addUser("username@mail.com","password","na me");
			assertEquals(HttpStatus.OK,delete("username@mail.com","password").getStatus());
			assertNull(userManager.verifyUser("username@mail.com","password"));

			addUser("name@mail.com","passw@/-ord","name");
			assertEquals(HttpStatus.OK,delete("name@mail.com","passw@/-ord").getStatus());
			assertNull(userManager.verifyUser("name@mail.com","passw@/-ord"));

			addUser("nam123e@mail.com","pas32sw@/-ord","na-me");
			assertEquals(HttpStatus.OK,delete("nam123e@mail.com","pas32sw@/-ord").getStatus());
			assertNull(userManager.verifyUser("nam123e@mail.com","pas32sw@/-ord"));

			addUser("nam123e@mail.com","pas32  1sw@/-ord","na-me");
			assertEquals(HttpStatus.OK,delete("nam123e@mail.com","pas32  1sw@/-ord").getStatus());
			assertNull(userManager.verifyUser("nam123e@mail.com","pas32  1sw@/-ord"));
	}
	@Test
	public void testDeleteIncorrect(){
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/user/delete",new UserBody("","")));
		});
		assertEquals(HttpStatus.NOT_FOUND , thrown.getStatus());

   	HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/user/delete",new UserBody(null,null)));
		});
		assertEquals(HttpStatus.NOT_FOUND , thrown1.getStatus());

   	HttpClientResponseException thrown2 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/user/delete",new UserBody("aa",null)));
		});
		assertEquals(HttpStatus.NOT_FOUND, thrown2.getStatus());

   	HttpClientResponseException thrown3 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/user/delete",new UserBody(null,"aa")));
		});
		assertEquals(HttpStatus.NOT_FOUND , thrown3.getStatus());

   	HttpClientResponseException thrown4 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/user/delete",new UserBody("","aa")));
		});
		assertEquals(HttpStatus.NOT_FOUND , thrown4.getStatus());

   HttpClientResponseException thrown5 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/user/delete",new UserBody("aa","")));
		});
		assertEquals(HttpStatus.NOT_FOUND , thrown5.getStatus());

   HttpClientResponseException thrown6 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.DELETE("/user/delete",new UserBody("valid@email.com","correct_password")));
		});
		assertEquals(HttpStatus.NOT_FOUND , thrown6.getStatus());

	}
	@Test
	public void testGetPasswordResetCorrect(){
		assertEquals(HttpStatus.CREATED,addUser("test@gmail.com","password","name").getStatus());
		assertTrue(userManager.verifyEmail("test@gmail.com"));

		HttpResponse response = client.toBlocking().exchange(HttpRequest.POST("/user/password_reset_request",new StringBody("test@gmail.com")));
		assertEquals(HttpStatus.OK , response.getStatus());
	}
	@Test
	public void testGetPasswordResetIncorrect(){
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.POST("/user/password_reset_request",new StringBody("")));
		});
		assertEquals(HttpStatus.NOT_FOUND , thrown.getStatus());

 		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.POST("/user/password_reset_request",new StringBody(null)));
		});
		assertEquals(HttpStatus.NOT_FOUND , thrown1.getStatus());

		 HttpClientResponseException thrown2 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.POST("/user/password_reset_request",new StringBody("test@gmail.com")));
		});
		assertEquals(HttpStatus.NOT_FOUND , thrown2.getStatus());
		
	 HttpClientResponseException thrown3 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.POST("/user/password_reset_request",new StringBody("test@")));
		});
		assertEquals(HttpStatus.NOT_FOUND , thrown3.getStatus());

	 HttpClientResponseException thrown4 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.POST("/user/password_reset_request",new StringBody("@gmail.com")));
		});
		assertEquals(HttpStatus.NOT_FOUND , thrown4.getStatus());
	}

	@Test
	public void testPasswordResetCorrect(){
		try{
			userManager.addUser("mail@mail.com","password","name");
		}
		catch(InvalidEmailException|EmailExistsException|IncorrectNameException|InvalidPasswordException e){
			fail();
		}
		try{
			String token = ResetLinkManager.getResetLinkManager().create("mail@mail.com");
			assertTrue(ResetLinkManager.getResetLinkManager().exist(token));
			HttpResponse response = client.toBlocking().exchange(HttpRequest.PUT("/user/password_reset_change",new PasswordResetBody(token,"test")));
			assertEquals(HttpStatus.OK , response.getStatus());
			assertFalse(ResetLinkManager.getResetLinkManager().exist(token));
			assertNotNull(userManager.verifyUser("mail@mail.com","test"));
		}
		catch(EmailNotExistException e){
			fail();
		}	
	}
	@Test
	public void testPasswordResetIncorrect(){
	 HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.PUT("/user/password_reset_change",new PasswordResetBody("","test")));
		});
		assertEquals(HttpStatus.NOT_FOUND , thrown.getStatus());

 	HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.PUT("/user/password_reset_change",new 			
				PasswordResetBody("veryCorreCstTokenveryCorreCstTokenveryCorreCstTokenveryCorreCstTokenveryCorreCstToken","test")));
		});
		assertEquals(HttpStatus.NOT_FOUND , thrown1.getStatus());

 	HttpClientResponseException thrown2 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.PUT("/user/password_reset_change",new PasswordResetBody(null,"test")));
		});
		assertEquals(HttpStatus.NOT_FOUND , thrown2.getStatus());

 	HttpClientResponseException thrown3 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.PUT("/user/password_reset_change",new PasswordResetBody("veryCorreCstToken","test")));
		});
		assertEquals(HttpStatus.NOT_FOUND , thrown3.getStatus());



		try{
			userManager.addUser("mail@mail.com","password","name");
		}
		catch(InvalidEmailException|EmailExistsException|IncorrectNameException|InvalidPasswordException e){
			fail();
		}

		try{
			String token = ResetLinkManager.getResetLinkManager().create("mail@mail.com");
			assertTrue(ResetLinkManager.getResetLinkManager().exist(token));
 			HttpClientResponseException thrown4 = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/password_reset_change",new PasswordResetBody(token,"")));
			});
			assertEquals(HttpStatus.BAD_REQUEST , thrown4.getStatus());
			assertNull(userManager.verifyUser("mail@mail.com","test"));
			HttpClientResponseException thrown5 = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/password_reset_change",new PasswordResetBody(token,null)));
			});
			assertEquals(HttpStatus.BAD_REQUEST , thrown5.getStatus());
			HttpClientResponseException thrown6 = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/password_reset_change",new PasswordResetBody(token,"	 ")));
			});
			assertEquals(HttpStatus.BAD_REQUEST , thrown6.getStatus());
			assertNull(userManager.verifyUser("mail@mail.com","test"));


		}
		catch(EmailNotExistException e){
			fail();
		}	
	}
	@Test
	public void testGetNameCorrect(){
		assertEquals(HttpStatus.CREATED, addUser("email@email.com", "password","name").getStatus());
		String token = userManager.verifyUser("email@email.com", "password");
		assertEquals("name",getName(token));
		sessionManager.terminateSession(token);

		assertEquals(HttpStatus.CREATED, addUser("email1@email.com", "password","na me").getStatus());
		String token1 = userManager.verifyUser("email1@email.com", "password");
		assertEquals("na me",getName(token1));
		sessionManager.terminateSession(token1);

		assertEquals(HttpStatus.CREATED, addUser("email2@email.com", "password","na-me").getStatus());
		String token2 = userManager.verifyUser("email2@email.com", "password");
		assertEquals("na-me",getName(token2));
		sessionManager.terminateSession(token2);
	}
	@Test
	public void testGetNameIncorrect(){
			HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.GET("/user/name").header("X-API-Key","243fdsfdvqnSDFdsaSDF"));
			});
			assertEquals(HttpStatus.UNAUTHORIZED , thrown.getStatus());

			HttpClientResponseException thrown2 = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.GET("/user/name").header("X-API-Key",""));
			});
			assertEquals(HttpStatus.UNAUTHORIZED , thrown2.getStatus());	
	}

	@Test
	public void testChangeEmailCorrect(){
		assertEquals(HttpStatus.CREATED, addUser("email@email.com", "password","name").getStatus());
		String token = userManager.verifyUser("email@email.com", "password");
		assertEquals(HttpStatus.OK,changeEmail(token,"newemail@email.com").getStatus());
		sessionManager.terminateSession(token);
		String token1 = userManager.verifyUser("newemail@email.com", "password");
		assertNotNull(token1);
		sessionManager.terminateSession(token1);

	}	
	@Test
	public void testChangeEmailUserNotExist(){
			HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_email",new StringBody("email@email.com")).header("X-API-Key",""));
			});
			assertEquals(HttpStatus.UNAUTHORIZED , thrown.getStatus());	
			HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_email",new StringBody("email@email.com")).header("X-API-Key","verycorrecttoken"));
			});
			assertEquals(HttpStatus.UNAUTHORIZED , thrown1.getStatus());	
	}
	@Test
	public void testChangeEmailEmailExist(){
		assertEquals(HttpStatus.CREATED, addUser("email@email.com", "password","name").getStatus());
		assertEquals(HttpStatus.CREATED, addUser("email1@email.com", "password","name").getStatus());
		String token = userManager.verifyUser("email@email.com", "password");
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_email",new StringBody("email1@email.com")).header("X-API-Key",token));
			});
		assertEquals(HttpStatus.BAD_REQUEST , thrown.getStatus());	
		sessionManager.terminateSession(token);
	}


	@Test
	public void testChangeNameCorrect(){
		assertEquals(HttpStatus.CREATED, addUser("email@email.com", "password","name").getStatus());
		String token = userManager.verifyUser("email@email.com", "password");
		try{
			assertEquals(HttpStatus.OK,changeName(token,"na me").getStatus());
			assertEquals("na me",userManager.getName("email@email.com"));

			assertEquals(HttpStatus.OK,changeName(token,"na-me").getStatus());
			assertEquals("na-me",userManager.getName("email@email.com"));
		}
		catch(UserNotExistException e){
			fail();
		}

		sessionManager.terminateSession(token);
	}
	@Test
	public void testChangeNameUserNotExist(){
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_name",new StringBody("name")).header("X-API-Key",""));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown.getStatus());	

		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_name",new StringBody("name")).header("X-API-Key","vERyCorreCtToken123456"));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown1.getStatus());	
	}
	@Test
	public void testChangeNameInvalid(){
		assertEquals(HttpStatus.CREATED, addUser("email@email.com", "password","name").getStatus());
		String token = userManager.verifyUser("email@email.com", "password");

		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_name",new StringBody("")).header("X-API-Key",token));
		});
		assertEquals(HttpStatus.BAD_REQUEST , thrown.getStatus());	


		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_name",new StringBody(null)).header("X-API-Key",token));
		});
		assertEquals(HttpStatus.BAD_REQUEST , thrown1.getStatus());

		HttpClientResponseException thrown2 = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_name",new StringBody(" 	")).header("X-API-Key",token));
		});
		assertEquals(HttpStatus.BAD_REQUEST , thrown2.getStatus());		
		sessionManager.terminateSession(token);
	}

	@Test
	public void testChangePasswordCorrect(){
		assertEquals(HttpStatus.CREATED, addUser("email@email.com", "password","name").getStatus());
		String token = userManager.verifyUser("email@email.com", "password");

		assertEquals(HttpStatus.OK,changePassword(token,"newPassoword").getStatus());
		sessionManager.terminateSession(token);
		
		String token1 = userManager.verifyUser("email@email.com","newPassoword");
		assertNotNull(token1);

		assertEquals(HttpStatus.OK,changePassword(token1,"newPassoword").getStatus());
		sessionManager.terminateSession(token1);

		String token2 = userManager.verifyUser("email@email.com","newPassoword");
		assertNotNull(token1);

		assertEquals(HttpStatus.OK,changePassword(token2,"newPas   :}{?><\\|soword").getStatus());
		sessionManager.terminateSession(token2);

		String token3 = userManager.verifyUser("email@email.com","newPas   :}{?><\\|soword");
		assertNotNull(token3);
		sessionManager.terminateSession(token3);


	}
	@Test
	public void testChangePasswordUserNotExist(){
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_password",new StringBody("pass")).header("X-API-Key",""));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown.getStatus());	

		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_password",new StringBody("pass")).header("X-API-Key","312dsahdu32br23o87"));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown1.getStatus());	
	}
	@Test
	public void testChangePasswordInvalid(){
		assertEquals(HttpStatus.CREATED, addUser("email@email.com", "password","name").getStatus());
		String token = userManager.verifyUser("email@email.com", "password");

		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_password",new StringBody(null)).header("X-API-Key",token));
		});
		assertEquals(HttpStatus.BAD_REQUEST , thrown.getStatus());	

		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_password",new StringBody("")).header("X-API-Key",token));
		});
		assertEquals(HttpStatus.BAD_REQUEST , thrown1.getStatus());	


		HttpClientResponseException thrown2 = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_password",new StringBody(" 	")).header("X-API-Key",token));
		});
		assertEquals(HttpStatus.BAD_REQUEST , thrown2.getStatus());	
		sessionManager.terminateSession(token);
	}

	private HttpResponse addUser(String email, String password, String name){
		return client.toBlocking().exchange(HttpRequest.POST("/user/create",new UserBody(email,password,name)));

	}
	private HttpResponse login(String email, String password){
		return client.toBlocking().exchange(HttpRequest.POST("/user/login",new UserBody(email,password)));

	}
	private String getToken(String email, String password){
		return client.toBlocking().retrieve(HttpRequest.POST("/user/login",new UserBody(email,password)),String.class);

	}
	private HttpResponse delete(String email, String password){
		return client.toBlocking().exchange(HttpRequest.DELETE("/user/delete",new UserBody(email,password)));
	}
	private String getName(String token){
		return client.toBlocking().retrieve(HttpRequest.GET("/user/name").header("X-API-Key",token), String.class);
	}
	private HttpResponse changeEmail(String token, String email){
		return client.toBlocking().exchange(HttpRequest.PUT("/user/change_email",new StringBody(email)).header("X-API-Key",token));
	}
	private HttpResponse changeName(String token, String name){
		return client.toBlocking().exchange(HttpRequest.PUT("/user/change_name",new StringBody(name)).header("X-API-Key",token));
	}
	private HttpResponse changePassword(String token, String password){
		return client.toBlocking().exchange(HttpRequest.PUT("/user/change_password",new StringBody(password)).header("X-API-Key",token));
	}

}
