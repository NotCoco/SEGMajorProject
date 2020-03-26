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
import main.java.com.projectBackEnd.*;
import main.java.com.projectBackEnd.Entities.User.*;
import main.java.com.projectBackEnd.Entities.Session.SessionManager;
import main.java.com.projectBackEnd.Entities.ResetLinks.*;


import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@MicronautTest
public class UserControllerTest{
    

	/**
	*	TODO:
 	* 		 full revamp of this test class
	*/

	@Inject
    @Client("/")
    HttpClient client;
	private static final UserManagerInterface userManager = UserManager.getUserManager();
	private static final ResetLinkManagerInterface linkManager = ResetLinkManager.getResetLinkManager();
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
    }

	@Test
	public void testCreateCorrectUser(){
		HttpRequest request = HttpRequest.POST("/user/create",new User("username@mail.com","password","name"));
        	HttpResponse response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.CREATED, response.getStatus());
		assertNotNull(userManager.verifyUser("username@mail.com","password"));
		request = HttpRequest.POST("/user/create",new User("name@mail.com","passw@/-ord","name"));
		response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.CREATED, response.getStatus());
		assertNotNull(userManager.verifyUser("name@mail.com","passw@/-ord"));
		request = HttpRequest.POST("/user/create",new User("nam123e@mail.com","pas321sw@/-ord","name"));
		response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.CREATED, response.getStatus());
		assertNotNull(userManager.verifyUser("nam123e@mail.com","pas321sw@/-ord"));
	}
	@Test
	public void testCreateSameUser(){
		try{
			userManager.addUser("username@mail.com","pa/?@ssword","name");
		}
		catch(InvalidEmailException|EmailExistsException e){
			fail();
		}
		HttpRequest request = HttpRequest.POST("/user/create",new User("username@mail.com","pass/?@word","name"));
        	HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().retrieve(request);
        	});
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
		assertEquals("user already exsits", thrown.getResponse().getBody().get());
	}
	@Test
	public void testCreateWrongUser(){
        	HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().retrieve(HttpRequest.POST("/user/create",new User("username@","password","name")));
        	});
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
		assertEquals("invalid email address", thrown.getResponse().getBody().get());
		assertNull(userManager.verifyUser("username@","password"));
        	thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().retrieve(HttpRequest.POST("/user/create",new User("user name@","pas1234//--+sword","name")));
        	});
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
		assertEquals("invalid email address", thrown.getResponse().getBody().get());
		assertNull(userManager.verifyUser("user name@","pas1234//--+sword"));
	}


	@Test
	public void testLoginCorrect(){

		assertEquals(HttpStatus.CREATED,client.toBlocking().exchange(HttpRequest.POST("/user/create",new User("username@mail.com","password","name"))).getStatus());
		HttpResponse response = client.toBlocking().exchange(HttpRequest.POST("/user/login",new User("username@mail.com","password","name")));
		assertEquals(HttpStatus.OK, response.getStatus());
		String token = client.toBlocking().retrieve(HttpRequest.POST("/user/login",new User("username@mail.com","password","name")), String.class);
		assertNotEquals("",token);
		assertEquals(HttpStatus.CREATED,client.toBlocking().exchange(HttpRequest.POST("/user/create",new User("username1@mail.com","pass32//#@{}][12wor\\d","name"))).getStatus());
		response = client.toBlocking().exchange(HttpRequest.POST("/user/login",new User("username1@mail.com","pass32//#@{}][12wor\\d","name")));
		assertEquals(HttpStatus.OK, response.getStatus());
		token = client.toBlocking().retrieve(HttpRequest.POST("/user/login",new User("username1@mail.com","pass32//#@{}][12wor\\d","name")), String.class);
		assertNotEquals("",token);
		

	}

	@Test
	public void testLoginIncorrect(){
		HttpRequest request = HttpRequest.POST("/user/login",new User("username@mail.com","password","name"));
        	HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().retrieve(request);
        	});
		assertEquals(HttpStatus.NOT_FOUND , thrown.getStatus());
		assertEquals("invalid credentials", thrown.getResponse().getBody().get());
	}

	@Test
	public void testDeleteUserExisting(){
		HttpResponse response = client.toBlocking().exchange(HttpRequest.POST("/user/create",new User("username@mail.com","password","name")));
		assertEquals(HttpStatus.CREATED,response.getStatus());
            	response = client.toBlocking().exchange(HttpRequest.DELETE("/user/delete_user",new User("username@mail.com","password","name")));
		assertEquals(HttpStatus.OK,response.getStatus());
		assertNull(userManager.verifyUser("username@mail.com","password"));
		
	}
	@Test
	public void testDeleteUserNotExisting(){
		HttpRequest request = HttpRequest.DELETE("/user/delete_user",new User("username@mail.com","password","name"));
        	HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().retrieve(request);
        	});
		assertEquals(HttpStatus.NOT_FOUND , thrown.getStatus());
		assertEquals("user does not exsist",thrown.getResponse().getBody().get());
		assertNull(userManager.verifyUser("username@mail.com","password"));
	}

	
	@Test
	public void testChangeEmailExist(){
		try{
			userManager.addUser("username@mail.com","password","name");
		}
		catch(InvalidEmailException|EmailExistsException e){
			fail();
		}
		String a = userManager.verifyUser("username@mail.com","password");
		HttpRequest request = HttpRequest.PUT("/user/change_email",new ChangeEmailBody("username@mail.com","username1@mail.com")).header("X-API-Key", a);
		HttpResponse response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.OK , response.getStatus());
		assertNotNull(userManager.verifyUser("username1@mail.com","password"));
	}
	@Test
	public void testChangeEmailNotExist(){
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().exchange(HttpRequest.PUT("/user/change_email",new ChangeEmailBody("username@mail.com","username1@mail.com")).header("X-API-Key", ""));
        	});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown.getStatus());
	}
	@Test
	public void testChangeDuplicateEmail(){
		try{
			userManager.addUser("username@mail.com","password","name");
			userManager.addUser("username1@mail.com","password","name");
		}
		catch(InvalidEmailException|EmailExistsException e){
			fail();
		} 
		String a = userManager.verifyUser("username@mail.com","password");
		assertNotNull(a);
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().retrieve(HttpRequest.PUT("/user/change_email",new ChangeEmailBody("username@mail.com","username1@mail.com")).header("X-API-Key", a));
        	});
		assertEquals(HttpStatus.BAD_REQUEST , thrown.getStatus());
		assertNotNull(userManager.verifyUser("username@mail.com","password"));
	}
	@Test
	public void testPasswordResetEmailNotExist(){
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().retrieve(HttpRequest.POST("/user/password_reset_request",new StringBody("email@email.com")));
        	});
		assertEquals(HttpStatus.NOT_FOUND , thrown.getStatus());
		assertEquals("incorrect email",thrown.getResponse().getBody().get());
	}
	
	@Test  
	public void testPasswordReset(){
		String email = "test@gmail.com";
		try{
			userManager.addUser(email,"password","name");
		}
		catch(InvalidEmailException|EmailExistsException e){
			fail();
		}
		assertTrue(userManager.verifyEmail(email));
		HttpRequest request = HttpRequest.POST("/user/password_reset_request",new StringBody(email));
		HttpResponse response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.OK , response.getStatus());


	}
	

	@Test
	public void testChangePasswordExisting(){
		try{
			userManager.addUser("mail@mail.com","password","name");
		}
		catch(InvalidEmailException|EmailExistsException e){
			fail();
		}



		try{
			String a = ResetLinkManager.getResetLinkManager().create("mail@mail.com");
			assertTrue(ResetLinkManager.getResetLinkManager().exist(a));
			HttpRequest request = HttpRequest.PUT("/user/password_reset_change",new PasswordResetBody(a,"test"));
			HttpResponse response = client.toBlocking().exchange(request);
			assertEquals(HttpStatus.OK , response.getStatus());
			assertFalse(ResetLinkManager.getResetLinkManager().exist(a));
			assertNotNull(userManager.verifyUser("mail@mail.com","test"));
		}
		catch(EmailNotExistException e){
			fail();
		}	
		
	}
	@Test
	public void testChangePasswordNotExisting(){
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().retrieve(HttpRequest.PUT("/user/password_reset_change",new PasswordResetBody("VeryRandomString1123423budsadvaswitvdycioy23271g","test")));
		 });
		assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());

		thrown = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().retrieve(HttpRequest.PUT("/user/password_reset_change",new PasswordResetBody(null,"test")));
		 });
		assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());

		thrown = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().retrieve(HttpRequest.PUT("/user/password_reset_change",new PasswordResetBody("","test")));
		 });
		assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());

	}
}
