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
import main.java.com.projectBackEnd.Entities.User.User;
import main.java.com.projectBackEnd.Entities.User.UserManager;
import main.java.com.projectBackEnd.Entities.User.UserManagerInterface;
import main.java.com.projectBackEnd.Entities.User.InvalidEmailException;
import main.java.com.projectBackEnd.Entities.User.EmailExistsException;
import main.java.com.projectBackEnd.Entities.Session.SessionManager;

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


@MicronautTest
public class UserControllerTest{
    

	@Inject
    @Client("/")
    HttpClient client;
	private static final UserManagerInterface userManager = UserManager.getUserManager();
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
	}
	@Test
	public void testCreateSameUser(){
		HttpRequest request = HttpRequest.POST("/user/create",new User("username@mail.com","password"));
        	HttpResponse response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
		String reason = client.toBlocking().retrieve(request,String.class);
		assertEquals("user already exsits", reason);
	}
	@Test
	public void testCreateWrongUser(){
		HttpRequest request = HttpRequest.POST("/user/create",new User("username@mail.com","password"));
        	HttpResponse response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
		String reason = client.toBlocking().retrieve(request,String.class);
		assertEquals("invalid email address", reason);
		assertNull(userManager.verifyUser("username@mail.com","password"));
	}


	@Test
	public void testLoginCorrect(){
		HttpRequest request = HttpRequest.POST("/user/create",new User("username@mail.com","password"));
        	HttpResponse response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.CREATED, response.getStatus());
		request = HttpRequest.POST("/user/login",new User("username@mail.com","password"));
		response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.OK, response.getStatus());
		String token = client.toBlocking().retrieve(request, String.class);
		assertNotEquals("",token);
	}

	@Test
	public void testLoginIncorrect(){
		HttpRequest request = HttpRequest.POST("/user/login",new User("username@mail.com","password"));
        	HttpResponse response = client.toBlocking().exchange(request);
		response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.NOT_FOUND , response.getStatus());

	}

	/**
	* TODO: I do not think this works as intended
	*/
	@Test
	public void testDeleteUserExisting(){
		try{
			userManager.addUser("username@mail.com","password");
		}
		catch(InvalidEmailException|EmailExistsException e){
			fail();
		}
		assertNotNull(userManager.verifyUser("username@mail.com","password"));
		HttpRequest request = HttpRequest.DELETE("/user/delete_user",new User("username@mail.com","password"));
		HttpResponse response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.OK , response.getStatus());
		assertNull(userManager.verifyUser("username@mail.com","password"));

	}
	@Test
	public void testDeleteUserNotExisting(){
		HttpRequest request = HttpRequest.DELETE("/user/delete_user",new User("username@mail.com","password"));
		HttpResponse response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.NOT_FOUND , response.getStatus());
		String reason = client.toBlocking().retrieve(request, String.class);
		assertEquals("user does not exsist",reason);
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
		HttpRequest request = HttpRequest.POST("/user/verify_token",token);
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
		HttpRequest request = HttpRequest.POST("/user/verify_token",token + "1");
		HttpResponse response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.NOT_FOUND , response.getStatus());
		request = HttpRequest.POST("/user/verify_token","1");
		response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.NOT_FOUND , response.getStatus());
		request = HttpRequest.POST("/user/verify_token","");
		response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.NOT_FOUND , response.getStatus());
		request = HttpRequest.POST("/user/verify_token","12345678912345678912345");
		response = client.toBlocking().exchange(request);
		assertEquals(HttpStatus.NOT_FOUND , response.getStatus());
	}

	/**
	@Test
	public void testChangePasswordExisting(){

	}
	@Test
	public void testChangePasswordNotExisting(){

	}
	*/

}
