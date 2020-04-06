package test.java;


import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.core.type.Argument;
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
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


import main.java.com.projectBackEnd.HibernateUtility;
import main.java.com.projectBackEnd.EntityManager;
import java.util.List;
/**
 * The purpose of this class is to test the REST endpoints associated with the user entity through the user controller
 */
@MicronautTest
class UserControllerTest{
    

	
	@Inject
    @Client("/")
    private HttpClient client;
	private static final UserManagerInterface userManager = UserManager.getUserManager();
	private static final ResetLinkManagerInterface linkManager = ResetLinkManager.getResetLinkManager();	
	private static final SessionManagerInterface sessionManager = SessionManager.getSessionManager();
	/**
	 * Sets the config resource location and the user manager
	 */
    @BeforeAll
    static void setUpDatabase() {
        HibernateUtility.setResource("testhibernate.cfg.xml");

    }

	/**
	 * Closes the session factory
	 */
    @AfterAll
    static void closeDatabase() {
		((EntityManager)userManager).deleteAll();
		((EntityManager)linkManager).deleteAll();
		((EntityManager)sessionManager).deleteAll();
        HibernateUtility.shutdown();
    }

	/**
	 * Ensure that there are no pre-existing user, resetlink or session entities in the database
	 * before each test via the 'deleteAll()' method
	 */
    @BeforeEach
    void setUp() {
		((EntityManager)userManager).deleteAll();
		((EntityManager)linkManager).deleteAll();
		((EntityManager)sessionManager).deleteAll();
    }

	/**
	 * Tests that users can be created with unconventional characters in it's attributes (this varies depending
	 * on the attribute), expects success
	 */
	@Test
	void testCreateUser(){
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

	/**
	 * Tests the creation of users with empty and null passwords, expects HTTP errors to be thrown
	 */
	@Test
	void testCreateUserIncorrectPassword(){
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

	/**
	 * Tests the creation of users with empty and null names, expects HTTP errors to be thrown
	 */
	@Test
	void testCreateUserIncorrectName(){
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

	/**
	 * Tests the creation of users with an invalid email format, expects HTTP errors to be thrown
	 */
	@Test
	void testCreateUserIncorrectEmail(){
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
	/**
	 * Tests the creation of duplicate users, expects an exception
	 */
	@Test
	void testCreateDuplicateUser(){
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
	/**
	 * Tests that the endpoint is able to log a user in, expects success
	 */
	@Test
	void TestLoginCorrect(){
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
	/**
	 * Attempts to log in with non existing user credentials, expects UNAUTHORIZED HTTP errors thrown
	 */
	@Test
	void TestLoginIncorrect(){
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
	/**
	 * Tests that the endpoint is able to log an existing user out, expects success
	 */
	@Test
	void testLogout(){
		addUser("admin@admin.com","admin","not me");
		String token = getToken("admin@admin.com","admin");
		assertEquals(HttpStatus.OK, client.toBlocking().exchange(HttpRequest.GET("/user/logout").header("X-API-Key", token)).getStatus());
		assertFalse(sessionManager.verifySession(token));
	}
	/**
	* Test if loging out while not having a correct session token throws an http exception
	*/
	@Test
	void testLogoutUnauthorized(){
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.GET("/user/logout").header("X-API-Key", "321bYUdsd36782F14ASd3DSa12vbuyds"));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown.getStatus());

		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.GET("/user/logout").header("X-API-Key", ""));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown1.getStatus());
	}

	/**
	* Test if adding users and than getting it all creates matching result
	*/
	@Test
	void testGetAll(){

		assertEquals(HttpStatus.CREATED,addUser("admin@admin.com","admin","not me").getStatus());
		assertEquals(HttpStatus.CREATED,addUser("admin1@admin.com","admin","not me").getStatus());
		assertEquals(HttpStatus.CREATED,addUser("admin2@admin.com","admin","not me").getStatus());
		String token = getToken("admin@admin.com","admin");
		List<User> users = client.toBlocking().retrieve(HttpRequest.GET("/user").header("X-API-Key", token), Argument.of(List.class, User.class));
		assertEquals(users.size(),3);
		for(User u:users){
			assertEquals(u.getName(),"not me");
		}
		
	}

	/**
	* Test if getting all users while not having a correct session token returns an http error
	*/
	@Test
	void testGetAllUnauthorized(){
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.GET("/user").header("X-API-Key", ""));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown.getStatus());

		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
			client.toBlocking().exchange(HttpRequest.GET("/user").header("X-API-Key", "321bYUdsd36782F14ASd3DSa12vbuyds"));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown1.getStatus());
	}

	/**
	 * Tests that the endpoint is able to delete an existing user, expects success
	 */
 	@Test
	void testDeleteCorrect(){
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
	/**
	 * Attempts to delete non existing users from the database, expects NOT_FOUND HTTP errors to be thrown
	 */
	@Test
	void testDeleteIncorrect(){
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
	/**
	 * Tests that the endpoint is able to retrieve and reset an existing user's password, expects succeess
	 */
	@Test
	void testGetPasswordResetCorrect(){
		assertEquals(HttpStatus.CREATED,addUser("test@gmail.com","password","name").getStatus());
		assertTrue(userManager.verifyEmail("test@gmail.com"));

		HttpResponse response = client.toBlocking().exchange(HttpRequest.POST("/user/password_reset_request",new StringBody("test@gmail.com")));
		assertEquals(HttpStatus.OK , response.getStatus());
	}

	/**
	 * Attempts to reset the password of non existing users, expects HTTP NOT_FOUND errors to be thrown
	 */
	@Test
	void testGetPasswordResetIncorrect(){
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

	/**
	 * Tests that the endpoint is able to reset an existing user's password, expects success
	 */
	@Test
	void testPasswordResetCorrect(){
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

	/**
	 * Attempts to reset the password of non existing users, expects HTTP NOT_FOUND errors to be thrown
	 */
	@Test
	void testPasswordResetIncorrect(){
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

	/**
	 * Tests that the endpoint is able to retrieve the name of an existing user, expects success
	 */
	@Test
	void testGetUserDetailsCorrect(){
		assertEquals(HttpStatus.CREATED, addUser("email@email.com", "password","name").getStatus());
		String token = userManager.verifyUser("email@email.com", "password");
		UserBody u = getUserDetails(token);
		assertEquals("name",u.getName());
		assertEquals("email@email.com",u.getEmail());
		sessionManager.terminateSession(token);

		assertEquals(HttpStatus.CREATED, addUser("email1@email.com", "password","na me").getStatus());
		String token1 = userManager.verifyUser("email1@email.com", "password");
		UserBody u1 = getUserDetails(token1);
		assertEquals("na me",u1.getName());
		assertEquals("email1@email.com",u1.getEmail());
		sessionManager.terminateSession(token1);

		assertEquals(HttpStatus.CREATED, addUser("email2@email.com", "password","na-me").getStatus());
		String token2 = userManager.verifyUser("email2@email.com", "password");
		UserBody u2 = getUserDetails(token2);
		assertEquals("na-me",u2.getName());
		assertEquals("email2@email.com",u2.getEmail());
		sessionManager.terminateSession(token2);
	}

	/**
	 * Attempts to retrieve a user's name from a user that does not exist in the database via the GET request,
	 * expects an Http error to be thrown
	 */
	@Test
	void testGetUserDetailsIncorrect(){
			HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.GET("/user/user_details").header("X-API-Key","243fdsfdvqnSDFdsaSDF"));
			});
			assertEquals(HttpStatus.UNAUTHORIZED , thrown.getStatus());

			HttpClientResponseException thrown2 = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.GET("/user/user_details").header("X-API-Key",""));
			});
			assertEquals(HttpStatus.UNAUTHORIZED , thrown2.getStatus());	
	}

	/**
	 * Tests that the endpoint is able to change the email of an existing user, expects success
	 */
	@Test
	void testChangeEmailCorrect(){
		assertEquals(HttpStatus.CREATED, addUser("email@email.com", "password","name").getStatus());
		String token = userManager.verifyUser("email@email.com", "password");
		assertEquals(HttpStatus.OK,changeEmail(token,"newemail@email.com").getStatus());
		sessionManager.terminateSession(token);
		String token1 = userManager.verifyUser("newemail@email.com", "password");
		assertNotNull(token1);
		sessionManager.terminateSession(token1);
	}

	/**
	 * Attempts to update the email of a user that does not exist in the database via the PUT request,
	 * expects an Http error to be thrown
	 */
	@Test
	void testChangeEmailUserNotExist(){
			HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_email",new StringBody("email@email.com")).header("X-API-Key",""));
			});
			assertEquals(HttpStatus.UNAUTHORIZED , thrown.getStatus());	
			HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_email",new StringBody("email@email.com")).header("X-API-Key","verycorrecttoken"));
			});
			assertEquals(HttpStatus.UNAUTHORIZED , thrown1.getStatus());	
	}

	/**
	 * Attempts to change the email of existing user A to the email of existing user B, expects HTTP error to be thrown
	 */
	@Test
	void testChangeEmailEmailExist(){
		assertEquals(HttpStatus.CREATED, addUser("email@email.com", "password","name").getStatus());
		assertEquals(HttpStatus.CREATED, addUser("email1@email.com", "password","name").getStatus());
		String token = userManager.verifyUser("email@email.com", "password");
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_email",new StringBody("email1@email.com")).header("X-API-Key",token));
			});
		assertEquals(HttpStatus.BAD_REQUEST , thrown.getStatus());	
		sessionManager.terminateSession(token);
	}

	/**
	 * Tests that the endpoint is able to change the name of an existing user, expects success
	 */
	@Test
	void testChangeNameCorrect(){
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

	/**
	 * Attempts to change the user name of a user that does not exist in the database via the PUT request,
	 * expects an Http error to be thrown
	 */
	@Test
	void testChangeNameUserNotExist(){
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_name",new StringBody("name")).header("X-API-Key",""));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown.getStatus());	

		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_name",new StringBody("name")).header("X-API-Key","vERyCorreCtToken123456"));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown1.getStatus());	
	}

	/**
	 * Attempts to change the name of an existing user to an empty string or null, expects an HTTP error to be thrown
	 */
	@Test
	void testChangeNameInvalid(){
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

	/**
	 * Tests that the endpoint is able to change the password of an existing user, expects success
	 */
	@Test
	void testChangePasswordCorrect(){
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

	/**
	 * Attempts to change the password of a user that does not exist in the database via the PUT request,
	 * expects an Http error to be thrown
	 */
	@Test
	void testChangePasswordUserNotExist(){
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_password",new StringBody("pass")).header("X-API-Key",""));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown.getStatus());	

		HttpClientResponseException thrown1 = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().exchange(HttpRequest.PUT("/user/change_password",new StringBody("pass")).header("X-API-Key","312dsahdu32br23o87"));
		});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown1.getStatus());	
	}

	/**
	 * Attempts to change the password of an existing user to an empty string or null, expects an HTTP error to be thrown
	 */
	@Test
	void testChangePasswordInvalid(){
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

	/**
	 * Quality of life method for adding a user via the REST api
	 * @param email the email
	 * @param password the password
	 * @param name the name
	 * @return The HTTP response produced by the operation
	 */
	private HttpResponse addUser(String email, String password, String name){
		return client.toBlocking().exchange(HttpRequest.POST("/user/create",new UserBody(email,password,name)));
	}

	/**
	 * Quality of life method for logging a user in via the REST api
	 * @param email the email
	 * @param password the password
	 * @return The HTTP response produced by the operation
	 */
	private HttpResponse login(String email, String password){
		return client.toBlocking().exchange(HttpRequest.POST("/user/login",new UserBody(email,password)));

	}

	/**
	 * Quality of life method for retrieving a user-based token via the REST api
	 * @param email the email
	 * @param password the password
	 * @return The token retrieved
	 */
	private String getToken(String email, String password){
		return client.toBlocking().retrieve(HttpRequest.POST("/user/login",new UserBody(email,password)),String.class);

	}

	/**
	 * Quality of life method for deleting a user via the REST api
	 * @param email the email
	 * @param password the password
	 * @return The HTTP response produced by the operation
	 */
	private HttpResponse delete(String email, String password){
		return client.toBlocking().exchange(HttpRequest.DELETE("/user/delete",new UserBody(email,password)));
	}
	/**
	* creates a get request to get name of the user with given session token
	* @returns UserBody of the user from http response , containing user's name and email
	*/
	private UserBody getUserDetails(String token){
		return client.toBlocking().retrieve(HttpRequest.GET("/user/user_details").header("X-API-Key",token), UserBody.class);
	}

	/**
	 * Quality of life method for updating a user's email via the REST api
	 * @param token the token
	 * @param email the email
	 * @return The HTTP response produced by the operation
	 */
	private HttpResponse changeEmail(String token, String email){
		return client.toBlocking().exchange(HttpRequest.PUT("/user/change_email",new StringBody(email)).header("X-API-Key",token));
	}

	/**
	 * Quality of life method for updating a user's name via the REST api
	 * @param token the token
	 * @param name the name
	 * @return The HTTP response produced by the operation
	 */
	private HttpResponse changeName(String token, String name){
		return client.toBlocking().exchange(HttpRequest.PUT("/user/change_name",new StringBody(name)).header("X-API-Key",token));
	}

	/**
	 * Quality of life method for updating a user's password via the REST api
	 * @param token the token
	 * @param password the password
	 * @return The HTTP response produced by the operation
	 */
	private HttpResponse changePassword(String token, String password){
		return client.toBlocking().exchange(HttpRequest.PUT("/user/change_password",new StringBody(password)).header("X-API-Key",token));
	}

}
