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
		HttpRequest request = HttpRequest.POST("/user/create",new User("username@mail.com","password"));
        	HttpResponse response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.CREATED, response.getStatus());
		assertNotNull(userManager.verifyUser("username@mail.com","password"));
		request = HttpRequest.POST("/user/create",new User("name@mail.com","passw@/-ord"));
		response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.CREATED, response.getStatus());
		assertNotNull(userManager.verifyUser("name@mail.com","passw@/-ord"));
		request = HttpRequest.POST("/user/create",new User("nam123e@mail.com","pas321sw@/-ord"));
		response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.CREATED, response.getStatus());
		assertNotNull(userManager.verifyUser("nam123e@mail.com","pas321sw@/-ord"));
	}
	@Test
	public void testCreateSameUser(){
		try{
			userManager.addUser("username@mail.com","pa/?@ssword");
		}
		catch(InvalidEmailException|EmailExistsException e){
			fail();
		}
		HttpRequest request = HttpRequest.POST("/user/create",new User("username@mail.com","pass/?@word"));
        	HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().retrieve(request);
        	});
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
		assertEquals("user already exsits", thrown.getResponse().getBody().get());
	}
	@Test
	public void testCreateWrongUser(){
        	HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().retrieve(HttpRequest.POST("/user/create",new User("username@","password")));
        	});
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
		assertEquals("invalid email address", thrown.getResponse().getBody().get());
		assertNull(userManager.verifyUser("username@mail.com","password"));
        	thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().retrieve(HttpRequest.POST("/user/create",new User("username@","pas1234//--+sword")));
        	});
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
		assertEquals("invalid email address", thrown.getResponse().getBody().get());
		assertNull(userManager.verifyUser("username@mail.com","pas1234//--+sword"));
	}


	@Test
	public void testLoginCorrect(){

		assertEquals(HttpStatus.CREATED,client.toBlocking().exchange(HttpRequest.POST("/user/create",new User("username@mail.com","password"))).getStatus());
		HttpResponse response = client.toBlocking().exchange(HttpRequest.POST("/user/login",new User("username@mail.com","password")));
		assertEquals(HttpStatus.OK, response.getStatus());
		String token = client.toBlocking().retrieve(HttpRequest.POST("/user/login",new User("username@mail.com","password")), String.class);
		assertNotEquals("",token);
		assertEquals(HttpStatus.CREATED,client.toBlocking().exchange(HttpRequest.POST("/user/create",new User("username1@mail.com","pass32//#@{}][12wor\\d"))).getStatus());
		response = client.toBlocking().exchange(HttpRequest.POST("/user/login",new User("username1@mail.com","pass32//#@{}][12wor\\d")));
		assertEquals(HttpStatus.OK, response.getStatus());
		token = client.toBlocking().retrieve(HttpRequest.POST("/user/login",new User("username1@mail.com","pass32//#@{}][12wor\\d")), String.class);
		assertNotEquals("",token);
		

	}

	@Test
	public void testLoginIncorrect(){
		HttpRequest request = HttpRequest.POST("/user/login",new User("username@mail.com","password"));
        	HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().retrieve(request);
        	});
		assertEquals(HttpStatus.NOT_FOUND , thrown.getStatus());
		assertEquals("invalid credentials", thrown.getResponse().getBody().get());
	}

	@Test
	public void testDeleteUserExisting(){
		HttpResponse response = client.toBlocking().exchange(HttpRequest.POST("/user/create",new User("username@mail.com","password")));
		assertEquals(HttpStatus.CREATED,response.getStatus());
            	response = client.toBlocking().exchange(HttpRequest.DELETE("/user/delete_user",new User("username@mail.com","password")));
		assertEquals(HttpStatus.OK,response.getStatus());
		assertNull(userManager.verifyUser("username@mail.com","password"));
		
	}
	@Test
	public void testDeleteUserNotExisting(){
		HttpRequest request = HttpRequest.DELETE("/user/delete_user",new User("username@mail.com","password"));
        	HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().retrieve(request);
        	});
		assertEquals(HttpStatus.NOT_FOUND , thrown.getStatus());
		assertEquals("user does not exsist",thrown.getResponse().getBody().get());
		assertNull(userManager.verifyUser("username@mail.com","password"));
	}


	@Test
	public void testVerifySessionCorrect(){
		try{
			userManager.addUser("username@mail.com","password");
		}
		catch(InvalidEmailException|EmailExistsException e){
			fail();
		}
		String token = userManager.verifyUser("username@mail.com","password");
		assertNotNull(token);
		HttpRequest request = HttpRequest.POST("/user/verify_session",token);
		HttpResponse response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.OK , response.getStatus());

	}
	@Test
	public void testVerifySessionIncorrect(){
		try{
			userManager.addUser("username@mail.com","password");
		}
		catch(InvalidEmailException|EmailExistsException e){
			fail();
		}
		String token = userManager.verifyUser("username@mail.com","password");
		assertNotNull(token);
        	HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().exchange(HttpRequest.POST("/user/verify_session",token + "1"));
        	});
		assertEquals(HttpStatus.NOT_FOUND , thrown.getStatus());
		thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().exchange(HttpRequest.POST("/user/verify_session","1"));
        	});
		assertEquals(HttpStatus.NOT_FOUND , thrown.getStatus());

		thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().exchange(HttpRequest.POST("/user/verify_session","1234444444444444444444444222222222222222222222246"));
        	});
		assertEquals(HttpStatus.NOT_FOUND , thrown.getStatus());

		thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().exchange(HttpRequest.POST("/user/verify_session","12345678912345678912345"));
        	});
		assertEquals(HttpStatus.NOT_FOUND , thrown.getStatus());
	}


	
	@Test
	public void testChangeEmailExist(){
		try{
			userManager.addUser("username@mail.com","password");
		}
		catch(InvalidEmailException|EmailExistsException e){
			fail();
		}
		String a = userManager.verifyUser("username@mail.com","password");
		HttpRequest request = HttpRequest.PUT("/user/change_email",new ChangeEmailBody("username@mail.com","username1@mail.com",a));
		HttpResponse response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.OK , response.getStatus());
		assertNotNull(userManager.verifyUser("username1@mail.com","password"));
	}
	@Test
	public void testChangeEmailNotExist(){
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().exchange(HttpRequest.PUT("/user/change_email",new ChangeEmailBody("username@mail.com","username1@mail.com","")));
        	});
		assertEquals(HttpStatus.UNAUTHORIZED , thrown.getStatus());
	}
	@Test
	public void testChangeDuplicateEmail(){
		try{
			userManager.addUser("username@mail.com","password");
			userManager.addUser("username1@mail.com","password");
		}
		catch(InvalidEmailException|EmailExistsException e){
			fail();
		} 
		String a = userManager.verifyUser("username@mail.com","password");
		assertNotNull(a);
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().retrieve(HttpRequest.PUT("/user/change_email",new ChangeEmailBody("username@mail.com","username1@mail.com",a)));
        	});
		assertEquals(HttpStatus.BAD_REQUEST , thrown.getStatus());
		assertNotNull(userManager.verifyUser("username@mail.com","password"));
	}
	@Test
	public void testPasswordResetEmailNotExist(){
		HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            		client.toBlocking().retrieve(HttpRequest.POST("/user/password_reset",new StringBody("email@email.com")));
        	});
		assertEquals(HttpStatus.NOT_FOUND , thrown.getStatus());
		assertEquals("incorrect email",thrown.getResponse().getBody().get());
	}
	
	@Test  
	public void testPasswordReset(){
		String email = "test@gmail.com";
		try{
			userManager.addUser(email,"password");
		}
		catch(InvalidEmailException|EmailExistsException e){
			fail();
		}
		assertTrue(userManager.verifyEmail(email));
		HttpRequest request = HttpRequest.POST("/user/password_reset",new StringBody(email));
		HttpResponse response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.OK , response.getStatus());
		assertTrue(userManager.verifyEmail(email));
	}
	

	@Test
	public void testChangePasswordExisting(){
		try{
			userManager.addUser("mail@mail.com","password");
		}
		catch(InvalidEmailException|EmailExistsException e){
			fail();
		}



		try{
			String a = ResetLinkManager.getResetLinkManager().create("mail@mail.com");
			assertTrue(ResetLinkManager.getResetLinkManager().exist(a));
			HttpRequest request = HttpRequest.PUT("/user/change_password",new PasswordResetBody(a,"test"));
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
				client.toBlocking().retrieve(HttpRequest.PUT("/user/change_password",new PasswordResetBody("VeryRandomString1123423budsadvaswitvdycioy23271g","test")));
		 });
		assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());

		thrown = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().retrieve(HttpRequest.PUT("/user/change_password",new PasswordResetBody(null,"test")));
		 });
		assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());

		thrown = assertThrows(HttpClientResponseException.class, () -> {
				client.toBlocking().retrieve(HttpRequest.PUT("/user/change_password",new PasswordResetBody("","test")));
		 });
		assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());

	}
}
